'use strict';

angular.module('payment').controller('Payment.DetailsController', ['$scope', '$translate', 'UtilService'
    , 'Payment.InfoService', 'MessageService', 'ConfigService', 'Helper.ObjectBrowserService',
    function ($scope, $translate, Util, PaymentInfoService, MessageService, ConfigService, HelperObjectBrowserService) {

        ConfigService.getComponentConfig("payment", "details").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            PaymentInfoService.getPaymentInfo(currentObjectId).then(function (paymentInfo) {
                $scope.paymentInfo = paymentInfo;
                return paymentInfo;
            });
        }

        $scope.saveDetails = function() {
            var paymentInfo = Util.omitNg($scope.paymentInfo);
            PaymentInfoService.savePaymentInfo(paymentInfo).then(
                function (paymentInfo) {
                    MessageService.info($translate.instant("payment.comp.details.informSaved"));
                    return paymentInfo;
                }
            )
        };

    }
]);