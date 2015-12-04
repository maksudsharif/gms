'use strict';

angular.module('budget').controller('Budget.MainController', ['$scope', 'ConfigService',
    function($scope, ConfigService) {
        $scope.$on('component-config', applyConfig);
        $scope.$emit('req-component-config', 'main');
        $scope.components = null;
        $scope.config = null;

        function applyConfig(e, componentId, config) {
            if (componentId == 'main') {
                $scope.config = config;
            }
        }

        ConfigService.getModule({moduleId: 'budget'}, function(moduleConfig){
            $scope.components = moduleConfig.components;
        });
    }
]);