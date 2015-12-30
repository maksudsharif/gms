'use strict';

angular.module('budget').controller('BudgetListController', ['$scope', '$state', '$stateParams', '$q', '$translate'
    , 'ConfigService', 'Authentication', 'UtilService', 'ObjectService'
    , 'Budget.ListService', 'Budget.InfoService', 'Helper.ObjectBrowserService'
    , function ($scope, $state, $stateParams, $q, $translate
        , ConfigService, Authentication, Util, ObjectService
        , BudgetListService, BudgetInfoService, HelperObjectBrowserService) {

        //"treeConfig", "treeData", "onLoad", and "onSelect" will be set by Tree Helper
        new HelperObjectBrowserService.Tree({
            scope: $scope
            , state: $state
            , stateParams: $stateParams
            , moduleId: "budget"
            , getTreeData: function (start, n, sort, filters) {
                var dfd = $q.defer();
                Authentication.queryUserInfo().then(
                    function (userInfo) {
                        var userId = userInfo.userId;
                        BudgetListService.queryBudgetTreeData(userId, start, n, sort, filters).then(
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
            , getNodeData: function (budgetId) {
                return BudgetInfoService.getBudgetInfo(budgetId);
            }
            , makeTreeNode: function (budgetId) {
                return {
                    nodeId: Util.goodValue(budgetId.id, 0)
                    , nodeType: ObjectService.ObjectTypes.COSTSHEET
                    , nodeTitle: Util.goodValue(budgetId.title)
                    , nodeToolTip: Util.goodValue(budgetId.title)
                };
            }
        });
    }
]);