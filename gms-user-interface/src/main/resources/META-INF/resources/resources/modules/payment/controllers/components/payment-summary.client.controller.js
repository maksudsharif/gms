'use strict';

angular.module('payment').controller('Payment.SummaryController', ['$scope', 'UtilService', 'Helper.UiGridService', 'ConfigService', 'Payment.InfoService', 'Helper.ObjectBrowserService',
    function ($scope, Util, HelperUiGridService, ConfigService, PaymentInfoService, HelperObjectBrowserService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        ConfigService.getComponentConfig("payment", "summary").then(function (config) {
            gridHelper.setColumnDefs(config);
            gridHelper.setBasicOptions(config);
            return config;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            PaymentInfoService.getPaymentInfo(currentObjectId).then(function (paymentInfo) {
                $scope.paymentInfo = paymentInfo;
                var parentNumber = {parentNumber: $scope.paymentInfo.parentNumber};
                var parentType = {parentType: $scope.paymentInfo.parentType};
                var parentId = {parentId: $scope.paymentInfo.parentId};

                var costs = angular.copy($scope.paymentInfo.costs);
                costs = costs.map(function (obj) {
                    return angular.extend(obj, parentNumber, parentType, parentId);
                });
                $scope.gridOptions = $scope.gridOptions || {};
                $scope.gridOptions.data = costs;
                return paymentInfo;
            });
        }

        $scope.onClickObjectType = function (event, rowEntity) {
            event.preventDefault();

            var targetType = Util.goodMapValue(rowEntity, "parentType");
            var targetId = Util.goodMapValue(rowEntity, "parentId");
            gridHelper.showObject(targetType, targetId);
        };

    }
]);