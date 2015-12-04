'use strict';

/**
 * @ngdoc controller
 * @name budget.controller:Budget.NewBudgetSheetController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/budget/controllers/components/budget-new-budget.client.controller.js modules/budget/controllers/components/budget-new-budget.client.controller.js}
 *
 * The NewBudgetSheet Controller
 */
angular.module('budget').controller('Budget.NewBudgetSheetController', ['$scope', '$stateParams', '$sce', '$log', '$q', 'TicketService', 'LookupService', 'Frevvo.FormService',
    function($scope, $stateParams, $sce, $log, $q, TicketService, LookupService, FrevvoFormService) {
        $scope.$emit('req-component-config', 'newbudget');

        $scope.acmTicket = '';
        $scope.acmFormsProperties = {};
        $scope.frevvoFormUrl = '';


        $scope.openCreateCaseFrevvoForm = openCreateCaseFrevvoForm;

        /**
         * @ngdoc method
         * @name openCreateCaseFrevvoForm
         * @methodOf budget.controller:Budget.NewBudgetSheetController
         *
         * @description
         * This method generates the create new case Frevvo form url and loads the form
         * into an iframe as a trusted resource.  It can only be called after the
         * acm-forms.properties config and the acmTicket have been obtained.
         */
        function openCreateCaseFrevvoForm() {
            var formUrl = FrevvoFormService.buildFrevvoUrl($scope.acmFormsProperties, "costsheet_budget", $scope.acmTicket);
            $scope.frevvoFormUrl = $sce.trustAsResourceUrl(formUrl);
        }

        // Obtains authentication token for ArkCase
        var ticketInfo = TicketService.getArkCaseTicket();

        // Retrieves the properties from the acm-forms.properties file (including Frevvo configuration)
        var acmFormsInfo = LookupService.getConfig({name: 'acm-forms'});

        $q.all([ticketInfo, acmFormsInfo.$promise])
            .then(function(data) {
                $scope.acmTicket = data[0].data;
                $scope.acmFormsProperties = data[1];

                // Opens the new budget Frevvo form for the user
                openCreateCaseFrevvoForm();
            });

        $scope.config = null;
        $scope.$on('component-config', applyConfig);
        function applyConfig(e, componentId, config) {
            if (componentId == 'newbudget') {
                $scope.config = config;
            }
        }
    }
]);