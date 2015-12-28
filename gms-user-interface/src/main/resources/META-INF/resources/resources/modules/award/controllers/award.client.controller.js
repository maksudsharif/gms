'use strict';

angular.module('award').controller('AwardController', ['$scope', '$stateParams', '$state', '$translate'
    , 'UtilService', 'ConfigService', 'Case.InfoService', 'ObjectService', 'Helper.ObjectBrowserService'
    , function ($scope, $stateParams, $state, $translate
        , Util, ConfigService, CaseInfoService, ObjectService, HelperObjectBrowserService) {

        new HelperObjectBrowserService.Content({
            scope: $scope
            , state: $state
            , stateParams: $stateParams
            , moduleId: "award"
            , getObjectInfo: CaseInfoService.getCaseInfo
            , updateObjectInfo: CaseInfoService.updateCaseInfo
            , initComponentLinks: function (config) {
                return HelperObjectBrowserService.createComponentLinks(config, ObjectService.ObjectTypes.CASE_FILE);
            }
        });

        /*var promiseGetModuleConfig = ConfigService.getModuleConfig("award").then(function (config) {
            $scope.config = config;
            $scope.componentLinks = HelperObjectTreeService.createComponentLinks(config, ObjectService.ObjectTypes.CASE_FILE);
            $scope.activeLinkId = "main";
            return config;
        });
        $scope.$on('req-component-config', function (e, componentId) {
            promiseGetModuleConfig.then(function (config) {
                var componentConfig = _.find(config.components, {id: componentId});
                $scope.$broadcast('component-config', componentId, componentConfig);
                return config;
            });
        });
        $scope.$on('report-award-updated', function (e, caseInfo) {
            CaseInfoService.updateCaseInfo(caseInfo);
            $scope.$broadcast('case-updated', caseInfo);
        });

        $scope.$on('req-select-award', function (e, selectedCase) {
            var components = Util.goodArray(selectedCase.components);
            $scope.activeLinkId = (1 == components.length) ? components[0] : "main";
        });

        $scope.getActive = function (linkId) {
            return ($scope.activeLinkId == linkId) ? "active" : ""
        };

        $scope.onClickComponentLink = function (linkId) {
            $scope.activeLinkId = linkId;
            $state.go('award.' + linkId, {
                id: $stateParams.id
            });
        };

        $scope.linksShown = false;
        $scope.toggleShowLinks = function () {
            $scope.linksShown = !$scope.linksShown;
        };

        $scope.progressMsg = $translate.instant("award.progressNoCase");
        $scope.$on('req-select-award', function (e, selectedCase) {
            $scope.$broadcast('award-selected', selectedCase);

            var id = Util.goodMapValue(selectedCase, "nodeId", null);
            loadCase(id);
        });


        var loadCase = function (id) {
            if (Util.goodPositive(id)) {
                if ($scope.caseInfo && $scope.caseInfo.id != id) {
                    $scope.caseInfo = null;
                }
                $scope.progressMsg = $translate.instant("award.progressLoading") + " " + id + "...";

                CaseInfoService.getCaseInfo(id).then(
                    function (caseInfo) {
                        $scope.progressMsg = null;
                        $scope.caseInfo = caseInfo;
                        $scope.$broadcast('award-updated', caseInfo);
                        return caseInfo;
                    }
                    , function (error) {
                        $scope.caseInfo = null;
                        $scope.progressMsg = $translate.instant("award.progressError") + " " + id;
                        return error;
                    }
                );
            }
        };

        loadCase($stateParams.id);*/
    }
]);