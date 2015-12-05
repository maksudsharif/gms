'use strict';

angular.module('award').config(['$stateProvider', '$urlRouterProvider',
	function ($stateProvider, $urlRouterProvider) {
		// For any unmatched url redirect to  /agents
		$urlRouterProvider.otherwise('/award');

		$stateProvider.
		state('award', {
			url: '/award',
			templateUrl: 'modules/award/views/award.client.view.html',
			resolve: {
				translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
					$translatePartialLoader.addPart('award');
					$translatePartialLoader.addPart('common');
					return $translate.refresh();
				}]
			}
		})
			.state('award.id', {
				url: '/:id',
				templateUrl: 'modules/award/views/award.client.view.html'
			})

			.state('award.main', {
				url: '/:id/main',
				templateUrl: 'modules/award/views/components/award-main.client.view.html'
			})

			.state('award.Viewer', {
				url: '/viewer/:id/:containerId/:containerType/:name/:selectedIds',
				templateUrl: 'modules/award/views/components/award-viewer.client.view.html'
			})
			
			.state('award.calendar', {
				url: '/:id/calendar',
				templateUrl: 'modules/award/views/components/award-calendar.client.view.html'
			})

			.state('award.correspondence', {
				url: '/:id/correspondence',
				templateUrl: 'modules/award/views/components/award-correspondence.client.view.html'
			})

			.state('award.cost', {
				url: '/:id/cost',
				templateUrl: 'modules/award/views/components/award-cost.client.view.html'
			})

			.state('award.details', {
				url: '/:id/details',
				templateUrl: 'modules/award/views/components/award-details.client.view.html'
			})

			.state('award.documents', {
				url: '/:id/documents',
				templateUrl: 'modules/award/views/components/award-documents.client.view.html'
			})

			.state('award.history', {
				url: '/:id/history',
				templateUrl: 'modules/award/views/components/award-history.client.view.html'
			})

			.state('award.notes', {
				url: '/:id/notes',
				templateUrl: 'modules/award/views/components/award-notes.client.view.html'
			})

			.state('award.participants', {
				url: '/:id/participants',
				templateUrl: 'modules/award/views/components/award-participants.client.view.html'
			})

			.state('award.people', {
				url: '/:id/people',
				templateUrl: 'modules/award/views/components/award-people.client.view.html'
			})

			.state('award.references', {
				url: '/:id/references',
				templateUrl: 'modules/award/views/components/award-references.client.view.html'
			})

			.state('award.tasks', {
				url: '/:id/tasks',
				templateUrl: 'modules/award/views/components/award-tasks.client.view.html'
			})

			.state('award.time', {
				url: '/:id/time',
				templateUrl: 'modules/award/views/components/award-time.client.view.html'
			})
		;
	}
]);