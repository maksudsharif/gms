'use strict';

angular.module('budget').controller('Budget.SummaryController', ['$scope', 'UtilService', 'Helper.UiGridService', 'ConfigService', 'Budget.InfoService', 'Helper.ObjectBrowserService',
    function ($scope, Util, HelperUiGridService, ConfigService, BudgetInfoService, HelperObjectBrowserService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        ConfigService.getComponentConfig("budget", "summary").then(function (config) {
            gridHelper.setColumnDefs(config);
            gridHelper.setBasicOptions(config);
            return config;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            BudgetInfoService.getBudgetInfo(currentObjectId).then(function (budgetInfo) {
                $scope.budgetInfo = budgetInfo;
                var parentNumber = {parentNumber: $scope.budgetInfo.parentNumber};
                var parentType = {parentType: $scope.budgetInfo.parentType};
                var parentId = {parentId: $scope.budgetInfo.parentId};

                var costs = angular.copy($scope.budgetInfo.costs);
                costs = costs.map(function (obj) {
                    return angular.extend(obj, parentNumber, parentType, parentId);
                });
                $scope.gridOptions = $scope.gridOptions || {};
                $scope.gridOptions.data = costs;
                return budgetInfo;
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