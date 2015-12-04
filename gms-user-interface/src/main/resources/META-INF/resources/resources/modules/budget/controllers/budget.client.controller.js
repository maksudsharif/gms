'use strict';

/**
 * @ngdoc controller
 * @name budget.controller:BudgetController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/budget/controllers/budget.client.controller.js modules/budget/controllers/budget.client.controller.js}
 *
 * The Budget module main controller
 */
angular.module('budget').controller('BudgetController', ['$scope', '$stateParams', '$translate', 'ConfigService', 'Budget.InfoService', 'UtilService',
	function($scope, $stateParams, $translate, ConfigService, BudgetInfoService, Util) {
		var promiseGetModuleConfig = ConfigService.getModuleConfig("budget").then(function (config) {
			$scope.config = config;
			return config;
		});
		$scope.$on('req-component-config', function (e, componentId) {
			promiseGetModuleConfig.then(function (config) {
				var componentConfig = _.find(config.components, {id: componentId});
				$scope.$broadcast('component-config', componentId, componentConfig);
			});
		});

		$scope.progressMsg = $translate.instant("budget.progressNoCostsheet");
		$scope.$on('req-select-budget', function (e, selectedBudgetSheet) {
			$scope.$broadcast('budget-selected', selectedBudgetSheet);

			var id = Util.goodMapValue(selectedBudgetSheet, "nodeId", null);
			loadBudgetSheet(id);
		});

		var loadBudgetSheet = function (id) {
			if (id) {
				if ($scope.budgetInfo && $scope.budgetInfo.id != id) {
					$scope.budgetInfo = null;
				}
				$scope.progressMsg = $translate.instant("budget.progressLoading") + " " + id + "...";



				BudgetInfoService.getBudgetInfo(id).then(
					function (budgetInfo) {
						$scope.progressMsg = null;
						$scope.budgetInfo = budgetInfo;
						$scope.$broadcast('budget-updated', budgetInfo);
						return budgetInfo;
					}
					, function (errorData) {
						$scope.budgetInfo = null;
						$scope.progressMsg = $translate.instant("budget.progressError") + " " + id;
						return errorData;
					}
				);
			}
		};
		loadBudgetSheet($stateParams.id);
	}
]);