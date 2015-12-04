'use strict';

angular.module('budget').controller('Budget.PersonController', ['$scope', 'Helper.UiGridService',
    function ($scope, HelperUiGridService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        $scope.$emit('req-component-config', 'person');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('person' == componentId) {
                gridHelper.setColumnDefs(config);
                gridHelper.setBasicOptions(config);
            }
        });

        $scope.$on('budget-updated', function (e, data) {
            $scope.budgetInfo = data;
            $scope.gridOptions = $scope.gridOptions || {};
            $scope.gridOptions.data = [$scope.budgetInfo.user];
        });



    }
]);