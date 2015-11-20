'use strict';

angular.module('dashboard').factory('Dashboard.DashboardService', ['$resource',
    function ($resource) {
        return $resource('', {}, {
            getConfig: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/dashboard/get',
                data: ''
            },
            //need to change when GMS coding is a bit further
            queryAwardedGrants: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/search/advancedSearch?q=assignee_id_lcs\\::userId+' +
                      'AND+object_type_s\\:CASE_FILE+' +
                      'AND+NOT+status_lcs\\:DRAFT&start=:startWith&n=:pageSize&s=:sortBy :sortDir',
                isArray: false,
                data: ''
            },
            //need to change when GMS coding is a bit further
            queryUnawardedGrants: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/search/advancedSearch?q=assignee_id_lcs\\::userId+' +
                'AND+object_type_s\\:CASE_FILE+' +
                'AND+NOT+status_lcs\\:DRAFT&start=:startWith&n=:pageSize&s=:sortBy :sortDir',
                isArray: false,
                data: ''
            },
            saveConfig: {
                method: 'POST',
                url: 'proxy/arkcase/api/v1/plugin/dashboard/set'
            }
        })
    }
]);