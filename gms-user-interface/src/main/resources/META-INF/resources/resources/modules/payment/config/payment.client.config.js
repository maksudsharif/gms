'use strict';

angular.module('payment').run(['Menus', 'ConfigService',
	function(Menus, ConfigService){
		var config = ConfigService.getModule({moduleId: 'payment'});
		config.$promise.then(function(config){
			if (config.menus) {
				Menus.addMenuItems(config.menus);
			}
		});
	}
]);