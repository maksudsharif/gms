'use strict';

angular.module('payment').controller('Payment.SummaryController', ['$scope', 'UtilService', 'Helper.UiGridService',
    function ($scope, Util, HelperUiGridService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        $scope.$emit('req-component-config', 'summary');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('summary' == componentId) {
                gridHelper.setColumnDefs(config);
                gridHelper.setBasicOptions(config);
            }
        });

        $scope.$on('payment-updated', function (e, data) {
            $scope.paymentInfo = data;
            var parentNumber = {parentNumber: $scope.paymentInfo.parentNumber};
            var parentType = {parentType: $scope.paymentInfo.parentType};
            var parentId = {parentId: $scope.paymentInfo.parentId};
            var costs = angular.copy($scope.paymentInfo.costs);
            costs = costs.map(function (obj){
                return angular.extend(obj, parentNumber, parentType, parentId);
            });
            $scope.gridOptions = $scope.gridOptions || {};
            $scope.gridOptions.data = costs;
        });

        $scope.onClickObjectType = function (event, rowEntity) {
            event.preventDefault();

            var targetType = Util.goodMapValue(rowEntity, "parentType");
            var targetId = Util.goodMapValue(rowEntity, "parentId");
            gridHelper.showObject(targetType, targetId);
        };

    }
]);