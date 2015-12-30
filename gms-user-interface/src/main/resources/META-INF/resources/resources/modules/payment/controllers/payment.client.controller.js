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
angular.module('payment').controller('PaymentController', ['$scope', '$state', '$stateParams', '$translate'
	, 'ObjectService', 'Payment.InfoService', 'Helper.ObjectBrowserService',
	function($scope, $state, $stateParams, $translate
		, ObjectService, PaymentInfoService, HelperObjectBrowserService) {

		new HelperObjectBrowserService.Content({
			scope: $scope
			, state: $state
			, stateParams: $stateParams
			, moduleId: "payment"
			, getObjectInfo: PaymentInfoService.getPaymentInfo
			, updateObjectInfo: PaymentInfoService.savePaymentInfo
			, initComponentLinks: function (config) {
				return HelperObjectBrowserService.createComponentLinks(config, ObjectService.ObjectTypes.COSTSHEET);
			}
		});

	}
]);