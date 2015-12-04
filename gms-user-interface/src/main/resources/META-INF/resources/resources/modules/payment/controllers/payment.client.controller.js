'use strict';

/**
 * @ngdoc controller
 * @name payment.controller:PaymentController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/payment/controllers/payment.client.controller.js modules/payment/controllers/payment.client.controller.js}
 *
 * The Payment module main controller
 */
angular.module('payment').controller('PaymentController', ['$scope', '$stateParams', '$translate', 'ConfigService', 'Payment.InfoService', 'UtilService',
	function($scope, $stateParams, $translate, ConfigService, PaymentInfoService, Util) {
		var promiseGetModuleConfig = ConfigService.getModuleConfig("payment").then(function (config) {
			$scope.config = config;
			return config;
		});
		$scope.$on('req-component-config', function (e, componentId) {
			promiseGetModuleConfig.then(function (config) {
				var componentConfig = _.find(config.components, {id: componentId});
				$scope.$broadcast('component-config', componentId, componentConfig);
			});
		});

		$scope.progressMsg = $translate.instant("payment.progressNoCostsheet");
		$scope.$on('req-select-payment', function (e, selectedPaymentSheet) {
			$scope.$broadcast('payment-selected', selectedPaymentSheet);

			var id = Util.goodMapValue(selectedPaymentSheet, "nodeId", null);
			loadPaymentSheet(id);
		});

		var loadPaymentSheet = function (id) {
			if (id) {
				if ($scope.paymentInfo && $scope.paymentInfo.id != id) {
					$scope.paymentInfo = null;
				}
				$scope.progressMsg = $translate.instant("payment.progressLoading") + " " + id + "...";



				PaymentInfoService.getPaymentInfo(id).then(
					function (paymentInfo) {
						$scope.progressMsg = null;
						$scope.paymentInfo = paymentInfo;
						$scope.$broadcast('payment-updated', paymentInfo);
						return paymentInfo;
					}
					, function (errorData) {
						$scope.paymentInfo = null;
						$scope.progressMsg = $translate.instant("payment.progressError") + " " + id;
						return errorData;
					}
				);
			}
		};
		loadPaymentSheet($stateParams.id);
	}
]);