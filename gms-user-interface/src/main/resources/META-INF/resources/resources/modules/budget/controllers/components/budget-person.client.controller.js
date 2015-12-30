'use strict';

angular.module('budget').controller('Budget.PersonController', ['$scope', 'UtilService', 'Helper.UiGridService', 'ConfigService', 'Budget.InfoService', 'Helper.ObjectBrowserService',
    function ($scope, Util, HelperUiGridService, ConfigService, BudgetInfoService, HelperObjectBrowserService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        ConfigService.getComponentConfig("budget", "person").then(function (config) {
            gridHelper.setColumnDefs(config);
            gridHelper.setBasicOptions(config);
            return config;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            BudgetInfoService.getBudgetInfo(currentObjectId).then(function (budgetInfo) {
                $scope.budgetInfo = budgetInfo;
                $scope.gridOptions = $scope.gridOptions || {};
                $scope.gridOptions.data = [$scope.budgetInfo.user];
                return budgetInfo;
            });
        }

    }
]);