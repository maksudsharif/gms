'use strict';

/**
 * @ngdoc controller
 * @name payment.controller:Payment.ActionsController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/payment/controllers/components/payment-actions.client.controller.js modules/payment/controllers/components/payment-actions.client.controller.js}
 *
 * The Payment actions controller
 */
angular.module('payment').controller('Payment.ActionsController', ['$scope', '$state', 'ConfigService', 'Payment.InfoService',
    function ($scope, $state, ConfigService, PaymentInfoService) {

        ConfigService.getComponentConfig("payment", "actions").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        $scope.$on('object-updated', function (e, data) {
            if (PaymentInfoService.validatePayment(data)) {
                $scope.paymentInfo = data;
            }
        });

        $scope.createNew = function () {
            $state.go("frevvo", {
                name: "new-costsheet-payment"
            });
        };

        $scope.edit = function (paymentInfo) {
            $state.go("frevvo", {
                name: "edit-costsheet-payment",
                arg: {
                    id: paymentInfo.id
                }
            });
        };

    }
]);