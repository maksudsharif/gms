'use strict';

angular.module('budget').run(['Menus', 'ConfigService',
	function(Menus, ConfigService){
		var config = ConfigService.getModule({moduleId: 'budget'});
		config.$promise.then(function(config){
			if (config.menus) {
				Menus.addMenuItems(config.menus);
			}
		});
	}
]);