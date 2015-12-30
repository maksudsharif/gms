'use strict';

angular.module('payment').controller('Payment.MainController', ['$scope', 'ConfigService',
    function($scope, ConfigService) {

        $scope.$emit('main-component-started');

        ConfigService.getComponentConfig("payment", "main").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        ConfigService.getModuleConfig("payment").then(function (moduleConfig) {
            $scope.components = moduleConfig.components;
            return moduleConfig;
        });

    }
]);