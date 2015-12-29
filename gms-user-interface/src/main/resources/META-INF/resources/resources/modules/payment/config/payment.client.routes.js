'use strict';

angular.module('payment').config(['$stateProvider',
	function($stateProvider) {
		$stateProvider.
			state('payment', {
				url: '/payment',
				templateUrl: 'modules/payment/views/payment.client.view.html',
				params :{
					costsheetType: 'COSTSHEET_PAYMENT'
				},
				resolve: {
					translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
						$translatePartialLoader.addPart('common');
						$translatePartialLoader.addPart('payment');
						return $translate.refresh();
					}],
					costsheetType: ['$stateParams', function ($stateParams) {
						return $stateParams.costsheetType;
					}]
				}
			})


			.state('payment.main', {
				url: '/:id/main',
				templateUrl: 'modules/payment/views/components/payment-main.client.view.html'
			})

			.state('payment.details', {
				url: '/:id/details',
				templateUrl: 'modules/payment/views/components/payment-details.client.view.html'
			})

			.state('payment.person', {
				url: '/:id/person',
				templateUrl: 'modules/payment/views/components/payment-person.client.view.html'
			})

			.state('payment.summary', {
				url: '/:id/summary',
				templateUrl: 'modules/payment/views/components/payment-summary.client.view.html'
			});

	}
]);