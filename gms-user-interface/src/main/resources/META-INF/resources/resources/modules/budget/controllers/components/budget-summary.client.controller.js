'use strict';

angular.module('budget').controller('Budget.SummaryController', ['$scope', 'UtilService', 'Helper.UiGridService',
    function ($scope, Util, HelperUiGridService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        $scope.$emit('req-component-config', 'summary');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('summary' == componentId) {
                gridHelper.setColumnDefs(config);
                gridHelper.setBasicOptions(config);
            }
        });

        $scope.$on('budget-updated', function (e, data) {
            $scope.budgetInfo = data;
            var parentNumber = {parentNumber: $scope.budgetInfo.parentNumber};
            var parentType = {parentType: $scope.budgetInfo.parentType};
            var parentId = {parentId: $scope.budgetInfo.parentId};
            var costs = angular.copy($scope.budgetInfo.costs);
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