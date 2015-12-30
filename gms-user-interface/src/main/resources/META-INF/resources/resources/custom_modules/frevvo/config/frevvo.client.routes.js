'use strict';

angular.module('frevvo').config(['$stateProvider',
    function ($stateProvider) {
        $stateProvider.
            state('frevvo', {
                url: '/frevvo',
                params: {name: '', arg: null},
                templateUrl: 'modules/frevvo/views/frevvo.client.view.html',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('frevvo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('frevvo-new-case', {
                url: '/frevvo-new-case',
                params: {name: 'new-case'},
                templateUrl: 'modules/frevvo/views/frevvo.client.view.html'
            })
            .state('frevvo-new-complaint', {
                url: '/frevvo-new-complaint',
                params: {name: 'new-complaint'},
                templateUrl: 'modules/frevvo/views/frevvo.client.view.html'
            })
            .state('frevvo-new-costsheet-payment', {
                url: '/new-costsheet-payment',
                params: {name: 'new-costsheet-payment'},
                templateUrl: 'modules/frevvo/views/frevvo.client.view.html'
            })
            .state('frevvo-new-costsheet-budget', {
                url: '/new-costsheet-budget',
                params: {name: 'new-costsheet-budget'},
                templateUrl: 'modules/frevvo/views/frevvo.client.view.html'
            })
        ;
    }
]);
