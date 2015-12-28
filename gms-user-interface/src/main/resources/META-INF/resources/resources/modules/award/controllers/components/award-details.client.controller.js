'use strict';

angular.module('award').controller('Award.DetailsController', ['$scope', '$stateParams', '$translate'
    , 'UtilService', 'ConfigService', 'Case.InfoService', 'MessageService', 'Helper.ObjectBrowserService'
    , function ($scope, $stateParams, $translate, Util, ConfigService, CaseInfoService, MessageService, HelperObjectBrowserService) {

        ConfigService.getComponentConfig("award", "details").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });


        var currentObjectId = HelperObjectBrowserService.getCurrentObjectId();
        if (Util.goodPositive(currentObjectId, false)) {
            CaseInfoService.getCaseInfo(currentObjectId).then(function (caseInfo) {
                $scope.caseInfo = caseInfo;
                return caseInfo;
            });
        }


		$scope.options = {
            focus: true
		};

        $scope.saveDetails = function() {
			var caseInfo = Util.omitNg($scope.caseInfo);
            CaseInfoService.saveCaseInfo(caseInfo).then(
                function (caseInfo) {
                    MessageService.info($translate.instant("award.comp.details.informSaved"));
                    return caseInfo;
                }
            );
        };
	}
]);