'use strict';

angular.module('budget').controller('Budget.DetailsController', ['$scope', '$translate', 'UtilService', 'Budget.InfoService', 'MessageService',
    function ($scope, $translate, Util, BudgetInfoService, MessageService) {
        $scope.$emit('req-component-config', 'details');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('details' == componentId) {
                $scope.config = config;
            }
        });

        $scope.$on('budget-updated', function (e, data) {
            $scope.budgetInfo = data;
        });

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