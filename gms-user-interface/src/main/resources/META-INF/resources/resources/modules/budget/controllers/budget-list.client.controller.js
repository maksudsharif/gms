'use strict';

angular.module('budget').controller('BudgetListController', ['$scope', '$state', '$stateParams', '$q', '$translate'
    , 'ConfigService', 'Authentication', 'UtilService', 'ObjectService', 'Helper.ObjectTreeService'
    , 'Budget.ListService', 'Budget.InfoService'
    , function ($scope, $state, $stateParams, $q, $translate
        , ConfigService, Authentication, Util, ObjectService, HelperObjectTreeService
        , BudgetListService, BudgetInfoService) {

        ConfigService.getModuleConfig("budget").then(function (config) {
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

        $scope.onLoad = function (start, n, sort, filters) {
            treeHelper.onLoad(start, n, sort, filters);
        };

        $scope.onSelect = function (selectedBudgetSheet) {
            $scope.$emit('req-select-budget', selectedBudgetSheet);
            var components = Util.goodArray(selectedBudgetSheet.components);
            var componentType = (1 == components.length) ? components[0] : "main";
            $state.go('budget.' + componentType, {
                id: selectedBudgetSheet.nodeId
            });
        };
    }
]);