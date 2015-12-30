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
angular.module('budget').controller('BudgetController', ['$scope', '$state','$stateParams', 'Budget.InfoService', 'ObjectService', 'Helper.ObjectBrowserService',
	function($scope, $state, $stateParams, BudgetInfoService, ObjectService, HelperObjectBrowserService) {

		new HelperObjectBrowserService.Content({
			scope: $scope
			, state: $state
			, stateParams: $stateParams
			, moduleId: "budget"
			, getObjectInfo: BudgetInfoService.getBudgetInfo
			, updateObjectInfo: BudgetInfoService.saveBudgetInfo
			, initComponentLinks: function (config) {
				return HelperObjectBrowserService.createComponentLinks(config, ObjectService.ObjectTypes.COSTSHEET);
			}
		});
	}
]);