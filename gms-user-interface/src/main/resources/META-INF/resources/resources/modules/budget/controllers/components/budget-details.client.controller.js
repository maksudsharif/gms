'use strict';

angular.module('budget').controller('Budget.DetailsController', ['$scope', '$translate', 'UtilService', 'Budget.InfoService', 'MessageService', 'ConfigService', 'Helper.ObjectBrowserService',
    function ($scope, $translate, Util, BudgetInfoService, MessageService, ConfigService, HelperObjectBrowserService) {

        ConfigService.getComponentConfig("budget", "details").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            BudgetInfoService.getBudgetInfo(currentObjectId).then(function (budgetInfo) {
                $scope.budgetInfo = budgetInfo;
                return budgetInfo;
            });
        }

        $scope.saveDetails = function() {
            var budgetInfo = Util.omitNg($scope.budgetInfo);
            BudgetInfoService.saveBudgetInfo(budgetInfo).then(
                function (budgetInfo) {
                    MessageService.info($translate.instant("budget.comp.details.informSaved"));
                    return budgetInfo;
                }
            )
        };

    }
]);