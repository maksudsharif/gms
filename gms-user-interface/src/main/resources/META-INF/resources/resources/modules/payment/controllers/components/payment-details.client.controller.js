'use strict';

angular.module('payment').controller('Payment.DetailsController', ['$scope', '$translate', 'UtilService', 'Payment.InfoService', 'MessageService',
    function ($scope, $translate, Util, PaymentInfoService, MessageService) {
        $scope.$emit('req-component-config', 'details');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('details' == componentId) {
                $scope.config = config;
            }
        });

        $scope.$on('payment-updated', function (e, data) {
            $scope.paymentInfo = data;
        });

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