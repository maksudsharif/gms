'use strict';

/**
 * @ngdoc controller
 * @name payment.controller:Payment.ActionsController
 *
 * @description
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/payment/controllers/components/payment-actions.client.controller.js modules/payment/controllers/components/payment-actions.client.controller.js}
 *
 * The Payment actions controller
 */
angular.module('payment').controller('Payment.ActionsController', ['$scope', '$state',
    function ($scope, $state) {
        $scope.$emit('req-component-config', 'actions');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('actions' == componentId) {
                $scope.config = config;
            }
        });

        $scope.paymentInfo = null;

        $scope.$on('payment-updated', function (e, data) {
            $scope.paymentInfo = data;
        });

        /**
         * @ngdoc method
         * @name loadNewPaymentFrevvoForm
         * @methodOf payment.controller:Payment.ActionsController
         *
         * @description
         * Displays the create new payment Frevvo form for the user
         */
        $scope.loadNewPaymentFrevvoForm = function () {
            $state.go('newPaymentSheet');
        };

        /**
         * @ngdoc method
         * @name loadExistingPaymentFrevvoForm
         * @methodOf payment.controller:Payment.ActionsController
         *
         * @description
         * Displays the existing payment Frevvo form for the user
         */
        $scope.loadExistingPaymentFrevvoForm = function () {
            $state.go('editPaymentSheet', { id : $scope.paymentInfo.id});
        };

    }
]);