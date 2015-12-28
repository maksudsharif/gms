'use strict';

angular.module('award').controller('Award.ReferencesController', ['$scope', 'UtilService', 'Helper.UiGridService', 'Case.InfoService', 'ConfigService','Helper.ObjectBrowserService'
    , function ($scope, Util, HelperUiGridService, CaseInfoService, ConfigService, HelperObjectBrowserService) {

        var gridHelper = new HelperUiGridService.Grid({scope: $scope});

        ConfigService.getComponentConfig("award", "references").then(function (config) {
            gridHelper.setColumnDefs(config);
            gridHelper.setBasicOptions(config);
            gridHelper.disableGridScrolling(config);
            return config;
        });

        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            CaseInfoService.getCaseInfo(currentObjectId).then(function (caseInfo) {
                $scope.caseInfo = caseInfo;
                $scope.gridOptions = $scope.gridOptions || {};
                $scope.gridOptions.data = $scope.caseInfo.references;
                //gridHelper.hidePagingControlsIfAllDataShown($scope.caseInfo.references.length);
                return caseInfo;
            });
        }

        $scope.onClickObjLink = function (event, rowEntity) {
            event.preventDefault();

            var targetType = Util.goodMapValue(rowEntity, "targetType");
            var targetId = Util.goodMapValue(rowEntity, "targetId");
            gridHelper.showObject(targetType, targetId);
        };

	}
]);