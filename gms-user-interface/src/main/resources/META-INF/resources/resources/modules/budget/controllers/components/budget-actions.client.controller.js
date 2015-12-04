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
angular.module('budget').controller('Budget.ActionsController', ['$scope', '$state',
    function ($scope, $state) {
        $scope.$emit('req-component-config', 'actions');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('actions' == componentId) {
                $scope.config = config;
            }
        });

        $scope.budgetInfo = null;

        $scope.$on('budget-updated', function (e, data) {
            $scope.budgetInfo = data;
        });

        /**
         * @ngdoc method
         * @name loadNewBudgetFrevvoForm
         * @methodOf budget.controller:Budget.ActionsController
         *
         * @description
         * Displays the create new budget Frevvo form for the user
         */
        $scope.loadNewBudgetFrevvoForm = function () {
            $state.go('newBudgetSheet');
        };

        /**
         * @ngdoc method
         * @name loadExistingBudgetFrevvoForm
         * @methodOf budget.controller:Budget.ActionsController
         *
         * @description
         * Displays the existing budget Frevvo form for the user
         */
        $scope.loadExistingBudgetFrevvoForm = function () {
            $state.go('editBudgetSheet', { id : $scope.budgetInfo.id});
        };

    }
]);