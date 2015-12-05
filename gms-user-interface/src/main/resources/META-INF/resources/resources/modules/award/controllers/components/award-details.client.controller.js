'use strict';

angular.module('award').controller('Award.DetailsController', ['$scope', '$stateParams', '$translate'
    , 'UtilService', 'Case.InfoService', 'MessageService'
    , function ($scope, $stateParams, $translate, Util, CaseInfoService, MessageService) {

		$scope.$emit('req-component-config', 'details');
        $scope.$on('component-config', function (e, componentId, config) {
            if ('details' == componentId) {
				$scope.config = config;
			}
        });

        $scope.$on('award-updated', function (e, data) {
            if (CaseInfoService.validateCaseInfo(data)) {
                $scope.caseInfo = data;
            }
		});


		$scope.options = {
            focus: true
		};

        $scope.saveDetails = function() {
			var caseInfo = Util.omitNg($scope.caseInfo);
            CaseInfoService.saveCaseInfo(caseInfo).then(
                function (caseInfo) {
                    MessageService.info($translate.instant("cases.comp.details.informSaved"));
                    return caseInfo;
                }
            );
        };
	}
]);