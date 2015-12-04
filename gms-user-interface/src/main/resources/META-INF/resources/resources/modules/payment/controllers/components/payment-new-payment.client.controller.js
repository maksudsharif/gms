'use strict';

/**
 * @ngdoc controller
 * @name payment.controller:Payment.NewPaymentSheetController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/payment/controllers/components/payment-new-payment.client.controller.js modules/payment/controllers/components/payment-new-payment.client.controller.js}
 *
 * The NewPaymentSheet Controller
 */
angular.module('payment').controller('Payment.NewPaymentSheetController', ['$scope', '$stateParams', '$sce', '$log', '$q', 'TicketService', 'LookupService', 'Frevvo.FormService',
    function($scope, $stateParams, $sce, $log, $q, TicketService, LookupService, FrevvoFormService) {
        $scope.$emit('req-component-config', 'newpayment');

        $scope.acmTicket = '';
        $scope.acmFormsProperties = {};
        $scope.frevvoFormUrl = '';


        $scope.openCreateCaseFrevvoForm = openCreateCaseFrevvoForm;

        /**
         * @ngdoc method
         * @name openCreateCaseFrevvoForm
         * @methodOf payment.controller:Payment.NewPaymentSheetController
         *
         * @description
         * This method generates the create new case Frevvo form url and loads the form
         * into an iframe as a trusted resource.  It can only be called after the
         * acm-forms.properties config and the acmTicket have been obtained.
         */
        function openCreateCaseFrevvoForm() {
            var formUrl = FrevvoFormService.buildFrevvoUrl($scope.acmFormsProperties, "costsheet_payment", $scope.acmTicket);
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

                // Opens the new payment Frevvo form for the user
                openCreateCaseFrevvoForm();
            });

        $scope.config = null;
        $scope.$on('component-config', applyConfig);
        function applyConfig(e, componentId, config) {
            if (componentId == 'newpayment') {
                $scope.config = config;
            }
        }
    }
]);