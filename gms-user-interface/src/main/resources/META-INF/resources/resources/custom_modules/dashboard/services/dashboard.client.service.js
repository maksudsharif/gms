'use strict';
/**
 * @ngdoc service
 * @name dashboard.service:Dashboard.DashboardService
 *
 * @description
 *
 * {@link https://github.com/Armedia/gms/blob/develop/gms-user-interface/src/main/resources/META-INF/resources/resources/modules/dashboard/services/dashboard.client.service.js dashboard/services/dashboard.client.service.js}
 *
 * The Dashboard service provides dashboard widgets functionality
 */
angular.module('dashboard').factory('Dashboard.DashboardService', ['$resource',
    function ($resource) {
        return $resource('', {}, {

            /**
             * @ngdoc method
             * @name getConfig
             * @methodOf dashboard.service:Dashboard.DashboardService
             *
             * @description
             * Retrieve dashboard widgets configurations
             *
             * @returns {Object} Configurations of dashboard widgets
             *
             */

            getConfig: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/dashboard/get',
                data: ''
            },

            /**
             * @ngdoc method
             * @name queryAwardedGrants
             * @methodOf dashboard.service:Dashboard.DashboardService
             *
             * @param {String} userId Current user ID
             *
             * @description
             * Retrieves list of grants awarded to current user
             *
             * @returns {Object} List of grants awarded to current user
             *
             */

            queryAwardedGrants: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/search/advancedSearch?q=assignee_id_lcs\\::userId+' +
                'AND+object_type_s\\:CASE_FILE+' + 'AND+object_sub_type_s\\:AWARD+' + 'AND+grant_type_s\\:AWARDED_GRANT'+
                '&start=:startWith&n=:pageSize&s=:sortBy :sortDir',
                isArray: false,
                data: ''
            },

            /**
             * @ngdoc method
             * @name queryUnawardedGrants
             * @methodOf dashboard.service:Dashboard.DashboardService
             *
             * @param {String} userId Current user ID
             *
             * @description
             * Retrieves list of un-awarded grants for the current user
             *
             * @returns {Object} List of grants not awarded to current user
             */

            queryUnawardedGrants: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/search/advancedSearch?q=assignee_id_lcs\\::userId+' +
                'AND+object_type_s\\:CASE_FILE+' + 'AND+object_sub_type_s\\:GRANT+' + 'AND+grant_type_s\\:UNAWARDED_GRANT'+
                '&start=:startWith&n=:pageSize&s=:sortBy :sortDir',
                isArray: false,
                data: ''
            },

            /**
             * @ngdoc method
             * @name saveConfig
             * @methodOf dashboard.service:Dashboard.DashboardService
             *
             * @description
             * Save dashboard widgets configurations
             *
             */
            saveConfig: {
                method: 'POST',
                url: 'proxy/arkcase/api/v1/plugin/dashboard/set'
            },

            queryMyTasks: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/task/forUser/:userId',
                isArray: true,
                data: ''
            },

            queryMyComplaints: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/plugin/complaint/forUser/:userId',
                isArray: true,
                data: ''
            }
        })
    }
]);