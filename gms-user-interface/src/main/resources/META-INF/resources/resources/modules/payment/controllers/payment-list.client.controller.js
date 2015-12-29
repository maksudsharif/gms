'use strict';

angular.module('payment').controller('PaymentListController', ['$scope', '$state', '$stateParams', '$q', '$translate'
    , 'ConfigService', 'Authentication', 'UtilService', 'ObjectService', 'Helper.ObjectBrowserService'
    , 'Payment.ListService', 'Payment.InfoService'
    , function ($scope, $state, $stateParams, $q, $translate
        , ConfigService, Authentication, Util, ObjectService, HelperObjectBrowserService
        , PaymentListService, PaymentInfoService) {

        //"treeConfig", "treeData", "onLoad", and "onSelect" will be set by Tree Helper
        new HelperObjectBrowserService.Tree({
            scope: $scope
            , state: $state
            , stateParams: $stateParams
            , moduleId: "payment"
            , getTreeData: function (start, n, sort, filters) {
                var dfd = $q.defer();
                Authentication.queryUserInfo().then(
                    function (userInfo) {
                        var userId = userInfo.userId;
                        PaymentListService.queryPaymentTreeData(userId, start, n, sort, filters).then(
                            function (treeData) {
                                dfd.resolve(treeData);
                                return treeData;
                            }
                            , function (error) {
                                dfd.reject(error);
                                return error;
                            }
                        );
                        return userInfo;
                    }
                    , function (error) {
                        dfd.reject(error);
                        return error;
                    }
                );
                return dfd.promise;
            }
            , getNodeData: function (paymentId) {
                return PaymentInfoService.getPaymentInfo(paymentId);
            }
            , makeTreeNode: function (paymentId) {
                return {
                    nodeId: Util.goodValue(paymentId.id, 0)
                    , nodeType: ObjectService.ObjectTypes.COSTSHEET
                    , nodeTitle: Util.goodValue(paymentId.title)
                    , nodeToolTip: Util.goodValue(paymentId.title)
                };
            }
        });

    }
]);