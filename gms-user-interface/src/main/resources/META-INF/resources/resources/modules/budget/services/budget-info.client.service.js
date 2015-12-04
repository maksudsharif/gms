'use strict';

/**
 * @ngdoc service
 * @name service:Budget.InfoService
 *
 * @description
 *
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/budget/services/budget-info.client.service.js modules/budget/services/budget-info.client.service.js}

 * Budget.InfoService provides functions for Budget database data
 */
angular.module('services').factory('Budget.InfoService', ['$resource', '$translate', 'UtilService',
    function ($resource, $translate, Util) {
        var Service = $resource('proxy/arkcase/api/v1/service/costsheet', {}, {

            /**
             * @ngdoc method
             * @name get
             * @methodOf service:Budget.InfoService
             *
             * @description
             * Query budget data by given id
             *
             * @param {Object} params Map of input parameter.
             * @param {Number} params.id  Budget ID
             * @param {Function} onSuccess (Optional)Callback function of success query.
             * @param {Function} onError (Optional) Callback function when fail.
             *
             * @returns {Object} Object returned by $resource
             */
            get: {
                method: 'GET',
                url: 'proxy/arkcase/api/v1/service/costsheet/:id',
                cache: false,
                isArray: false
            },

            /**
             * @ngdoc method
             * @name save
             * @methodOf service:Budget.InfoService
             *
             * @description
             * Save budget data
             *
             * @param {Object} params Map of input parameter.
             * @param {Number} params.id  Budget ID
             * @param {Function} onSuccess (Optional)Callback function of success query.
             * @param {Function} onError (Optional) Callback function when fail.
             *
             * @returns {Object} Object returned by $resource
             */
            save: {
                method: 'POST',
                url: 'proxy/arkcase/api/v1/service/costsheet',
                cache: false
            }
        });

        /**
         * @ngdoc method
         * @name getBudgetInfo
         * @methodOf service:Budget.InfoService
         *
         * @description
         * Query budget data
         *
         * @param {Number} id  Budget ID
         *
         * @returns {Object} Promise
         */
        Service.getBudgetInfo =  function (id) {
            return Util.serviceCall({
                service: Service.get
                , param: {id: id}
                , onSuccess: function (data) {
                    if (Service.validateBudget(data)) {
                        return data;
                    }
                }
            });
        };

        /**
         * @ngdoc method
         * @name saveBudgetSheetInfo
         * @methodOf service:Budget.InfoService
         *
         * @description
         * Save budget data
         *
         * @param {Object} budgetInfo  Budget sheet data
         *
         * @returns {Object} Promise
         */
        Service.saveBudgetInfo = function (budgetInfo) {
            if (!Service.validateBudget(budgetInfo)) {
                return Util.errorPromise($translate.instant("common.service.error.invalidData"));
            }
            return Util.serviceCall({
                service: Service.save
                , data: budgetInfo
                , onSuccess: function (data) {
                    if (Service.validateBudget(data)) {
                        return data;
                    }
                }
            });
        };

        /**
         * @ngdoc method
         * @name validateBudget
         * @methodOf service:Budget.InfoService
         *
         * @description
         * Validate budget
         *
         * @param {Object} data  Data to be validated
         *
         * @returns {Boolean} Return true if data is valid
         */
        Service.validateBudget = function(data) {
            if (Util.isEmpty(data)) {
                return false;
            }
            if (Util.isEmpty(data.id)) {
                return false;
            }
            if (Util.isEmpty(data.user)) {
                return false;
            }
            if (Util.isEmpty(data.user.userId)) {
                return false;
            }
            if (Util.isEmpty(data.parentId)) {
                return false;
            }
            if (Util.isEmpty(data.parentType)) {
                return false;
            }
            if (Util.isEmpty(data.parentNumber)) {
                return false;
            }
            if (Util.isEmpty(data.costs)) {
                return false;
            }
            if (Util.isEmpty(data.status)) {
                return false;
            }
            if (Util.isEmpty(data.creator)) {
                return false;
            }
            return true;
        };

        return Service;
    }
]);