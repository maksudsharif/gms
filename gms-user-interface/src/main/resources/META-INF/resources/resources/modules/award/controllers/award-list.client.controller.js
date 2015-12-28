'use strict';

angular.module('award').controller('AwardListController', ['$scope', '$state', '$stateParams', '$translate', 'UtilService', 'ObjectService', 'Award.ListService', 'Case.InfoService', 'ConfigService', 'Helper.ObjectBrowserService',
    function ($scope, $state, $stateParams, $translate, Util, ObjectService, CaseListService, CaseInfoService, ConfigService, HelperObjectBrowserService) {

        //"treeConfig", "treeData", "onLoad", and "onSelect" will be set by Tree Helper
        new HelperObjectBrowserService.Tree({
            scope: $scope
            , state: $state
            , stateParams: $stateParams
            , moduleId: "award"
            , getTreeData: function (start, n, sort, filters) {
                return CaseListService.queryCasesTreeData(start, n, sort, filters);
            }
            , getNodeData: function (caseId) {
                return CaseInfoService.getCaseInfo(caseId);
            }
            , makeTreeNode: function (caseInfo) {
                return {
                    nodeId: Util.goodValue(caseInfo.id, 0)
                    , nodeType: ObjectService.ObjectTypes.CASE_FILE
                    , nodeTitle: Util.goodValue(caseInfo.title)
                    , nodeToolTip: Util.goodValue(caseInfo.title)
                };
            }
        });

        /*var treeHelper = new HelperObjectTreeService.Tree({
            scope: $scope
            , nodeId: $stateParams.id
            , getTreeData: function (start, n, sort, filters) {
                return CaseListService.queryCasesTreeData(start, n, sort, filters);
            }
            , getNodeData: function (caseId) {
                return CaseInfoService.getCaseInfo(caseId);
            }
            , makeTreeNode: function (caseInfo) {
                return {
                    nodeId: Util.goodValue(caseInfo.id, 0)
                    , nodeType: ObjectService.ObjectTypes.CASE_FILE
                    , nodeTitle: Util.goodValue(caseInfo.title)
                    , nodeToolTip: Util.goodValue(caseInfo.title)
                };
            }
        });
        $scope.onLoad = function (start, n, sort, filters) {
            treeHelper.onLoad(start, n, sort, filters);
        };

        $scope.onSelect = function (selectedCase) {
            $scope.$emit('req-select-award', selectedCase);
            var components = Util.goodArray(selectedCase.components);
            var componentType = (1 == components.length) ? components[0] : "main";
            $state.go('award.' + componentType, {
                id: selectedCase.nodeId
            });
        };*/
    }
]);