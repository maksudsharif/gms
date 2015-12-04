'use strict';

angular.module('payment').controller('PaymentListController', ['$scope', '$state', '$stateParams', '$q', '$translate'
    , 'ConfigService', 'Authentication', 'UtilService', 'ObjectService', 'Helper.ObjectTreeService'
    , 'Payment.ListService', 'Payment.InfoService'
    , function ($scope, $state, $stateParams, $q, $translate
        , ConfigService, Authentication, Util, ObjectService, HelperObjectTreeService
        , PaymentListService, PaymentInfoService) {

        ConfigService.getModuleConfig("payment").then(function (config) {
            $scope.treeConfig = config.tree;
            $scope.componentsConfig = config.components;
            return config;
        });

        var treeHelper = new HelperObjectTreeService.Tree({
            scope: $scope
            , nodeId: $stateParams.id
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

        $scope.onLoad = function (start, n, sort, filters) {
            treeHelper.onLoad(start, n, sort, filters);
        };

        $scope.onSelect = function (selectedPaymentSheet) {
            $scope.$emit('req-select-payment', selectedPaymentSheet);
            var components = Util.goodArray(selectedPaymentSheet.components);
            var componentType = (1 == components.length) ? components[0] : "main";
            $state.go('payment.' + componentType, {
                id: selectedPaymentSheet.nodeId
            });
        };
    }
]);