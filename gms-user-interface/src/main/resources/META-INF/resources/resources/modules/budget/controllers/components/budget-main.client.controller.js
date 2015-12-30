'use strict';

angular.module('budget').controller('Budget.MainController', ['$scope', 'ConfigService',
    function($scope, ConfigService) {

        $scope.$emit('main-component-started');

        ConfigService.getComponentConfig("budget", "main").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        ConfigService.getModuleConfig("budget").then(function (moduleConfig) {
            $scope.components = moduleConfig.components;
            return moduleConfig;
        });
    }
]);