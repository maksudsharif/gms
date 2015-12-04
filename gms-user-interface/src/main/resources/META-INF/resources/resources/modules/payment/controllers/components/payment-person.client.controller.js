'use strict';

angular.module('payment').controller('Payment.PersonController', ['$scope', 'Helper.UiGridService',
    function ($scope, HelperUiGridService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        $scope.$emit('req-component-config', 'person');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('person' == componentId) {
                gridHelper.setColumnDefs(config);
                gridHelper.setBasicOptions(config);
            }
        });

        $scope.$on('payment-updated', function (e, data) {
            $scope.paymentInfo = data;
            $scope.gridOptions = $scope.gridOptions || {};
            $scope.gridOptions.data = [$scope.paymentInfo.user];
        });



    }
]);