'use strict';

angular.module('award').controller('Award.MainController', ['$scope', 'ConfigService',
	function($scope, ConfigService) {
		$scope.$emit('req-component-config', 'main');
		$scope.$on('component-config', function applyConfig(e, componentId, config) {
			if (componentId == 'main') {
				$scope.config = config;
			}
		});


		ConfigService.getModuleConfig("award").then(function (moduleConfig) {
			$scope.components = moduleConfig.components;
			return moduleConfig;
		});
	}
]);