'use strict';

/**
 * @ngdoc controller
 * @name budget.controller:Payment.ActionsController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/budget/controllers/components/budget-actions.client.controller.js modules/budget/controllers/components/budget-actions.client.controller.js}
 *
 * The Budget actions controller
 */
angular.module('budget').controller('Budget.ActionsController', ['$scope', '$state', 'ConfigService', 'Budget.InfoService',
    function ($scope, $state, ConfigService, BudgetInfoService) {

        ConfigService.getComponentConfig("budget", "actions").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        $scope.$on('object-updated', function (e, data) {
            if (BudgetInfoService.validateBudget(data)) {
                $scope.budgetInfo = data;
            }
        });

        $scope.createNew = function () {
            $state.go("frevvo", {
                name: "new-costsheet-budget"
            });
        };

        $scope.edit = function (budgetInfo) {
            $state.go("frevvo", {
                name: "edit-costsheet-budget",
                arg: {
                    id: budgetInfo.id
                }
            });
        };

    }
]);