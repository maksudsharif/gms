'use strict';

angular.module('payment').controller('Payment.PersonController', ['$scope', 'UtilService', 'Helper.UiGridService', 'ConfigService', 'Payment.InfoService', 'Helper.ObjectBrowserService',
    function ($scope, Util, HelperUiGridService, ConfigService, PaymentInfoService, HelperObjectBrowserService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        ConfigService.getComponentConfig("payment", "person").then(function (config) {
            gridHelper.setColumnDefs(config);
            gridHelper.setBasicOptions(config);
            return config;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            PaymentInfoService.getPaymentInfo(currentObjectId).then(function (paymentInfo) {
                $scope.paymentInfo = paymentInfo;
                $scope.gridOptions = $scope.gridOptions || {};
                $scope.gridOptions.data = [$scope.paymentInfo.user];
                return paymentInfo;
            });
        }

    }
]);