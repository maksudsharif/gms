'use strict';

/**
 * @ngdoc service
 * @name service:Payment.InfoService
 *
 * @description
 *
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/payment/services/payment-info.client.service.js modules/payment/services/payment-info.client.service.js}

 * Payment.InfoService provides functions for Payment database data
 */
angular.module('services').factory('Payment.InfoService', ['$resource', '$translate', 'UtilService',
    function ($resource, $translate, Util) {
        var Service = $resource('proxy/arkcase/api/v1/service/costsheet', {}, {

            /**
             * @ngdoc method
             * @name get
             * @methodOf service:Payment.InfoService
             *
             * @description
             * Query payment data by given id
             *
             * @param {Object} params Map of input parameter.
             * @param {Number} params.id  Payment ID
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
             * @methodOf service:Payment.InfoService
             *
             * @description
             * Save payment data
             *
             * @param {Object} params Map of input parameter.
             * @param {Number} params.id  Payment ID
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
         * @name getPaymentInfo
         * @methodOf service:Payment.InfoService
         *
         * @description
         * Query payment data
         *
         * @param {Number} id  Payment ID
         *
         * @returns {Object} Promise
         */
        Service.getPaymentInfo =  function (id) {
            return Util.serviceCall({
                service: Service.get
                , param: {id: id}
                , onSuccess: function (data) {
                    if (Service.validatePayment(data)) {
                        return data;
                    }
                }
            });
        };

        /**
         * @ngdoc method
         * @name savePaymentInfo
         * @methodOf service:Payment.InfoService
         *
         * @description
         * Save payment data
         *
         * @param {Object} paymentInfo  Payment sheet data
         *
         * @returns {Object} Promise
         */
        Service.savePaymentInfo = function (paymentInfo) {
            if (!Service.validatePayment(paymentInfo)) {
                return Util.errorPromise($translate.instant("common.service.error.invalidData"));
            }
            return Util.serviceCall({
                service: Service.save
                , data: paymentInfo
                , onSuccess: function (data) {
                    if (Service.validatePayment(data)) {
                        return data;
                    }
                }
            });
        };

        /**
         * @ngdoc method
         * @name validatePayment
         * @methodOf service:Payment.InfoService
         *
         * @description
         * Validate payment
         *
         * @param {Object} data  Data to be validated
         *
         * @returns {Boolean} Return true if data is valid
         */
        Service.validatePayment = function(data) {
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