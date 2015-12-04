'use strict';

angular.module('award').config(['$stateProvider',
	function($stateProvider) {
		$stateProvider.
		state('award', {
			url: '/award',
			templateUrl: 'modules/award/views/award.client.view.html',
			resolve: {
				translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
					$translatePartialLoader.addPart('award');
					return $translate.refresh();
				}]
			}
		});
	}
]);