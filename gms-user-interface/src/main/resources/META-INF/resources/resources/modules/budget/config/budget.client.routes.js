'use strict';

angular.module('budget').config(['$stateProvider',
	function($stateProvider) {
		$stateProvider.
			state('budget', {
				url: '/budget',
				templateUrl: 'modules/budget/views/budget.client.view.html',
				params :{
					costsheetType: 'COSTSHEET_BUDGET'
				},
				resolve: {
					translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
						$translatePartialLoader.addPart('common');
						$translatePartialLoader.addPart('budget');
						return $translate.refresh();
					}],
					costsheetType: ['$stateParams', function ($stateParams) {
						return $stateParams.costsheetType;
					}]
				}
			})

			.state('budget.main', {
				url: '/:id/main',
				templateUrl: 'modules/budget/views/components/budget-main.client.view.html'
			})

			.state('budget.details', {
				url: '/:id/details',
				templateUrl: 'modules/budget/views/components/budget-details.client.view.html'
			})

			.state('budget.person', {
				url: '/:id/person',
				templateUrl: 'modules/budget/views/components/budget-person.client.view.html'
			})

			.state('budget.summary', {
				url: '/:id/summary',
				templateUrl: 'modules/budget/views/components/budget-summary.client.view.html'
			})

	}
]);