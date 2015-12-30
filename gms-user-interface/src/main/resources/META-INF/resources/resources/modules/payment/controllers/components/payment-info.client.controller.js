'use strict';

angular.module('payment').controller('Payment.InfoController', ['$scope', 'UtilService'
    , 'ConfigService', 'Object.InfoService', 'ObjectService'
    , function ($scope, Util, ConfigService, ObjectInfoService, ObjectService) {

        ConfigService.getComponentConfig("payment", "info").then(function (componentConfig) {
            $scope.config = componentConfig;
            return componentConfig;
        });

        $scope.parentInfo = {};

        $scope.$on('object-updated', function (e, data) {
            $scope.paymentInfo = data;

            if ($scope.paymentInfo.parentType == ObjectService.ObjectTypes.CASE_FILE) {
                ObjectInfoService.get({
                        type: "casefile",
                        id: $scope.paymentInfo.parentId
                    },
                    function (data) {
                        $scope.parentInfo.title = data.title;
                        $scope.parentInfo.incidentDate = moment(data.created).format($scope.config.parentDateFormat);
                        $scope.parentInfo.priortiy = data.priority;
                        $scope.parentInfo.type = data.caseType;
                        $scope.parentInfo.status = data.status;
                    });
            } else if ($scope.paymentInfo.parentType == ObjectService.ObjectTypes.COMPLAINT) {
                ObjectInfoService.get({
                        type: "complaint",
                        id: $scope.paymentInfo.parentId
                    },
                    function (data) {
                        $scope.parentInfo.title = data.complaintTitle;
                        $scope.parentInfo.incidentDate = moment(data.incidentDate).format($scope.config.parentDateFormat);
                        $scope.parentInfo.priortiy = data.priority;
                        $scope.parentInfo.type = data.complaintType;
                        $scope.parentInfo.status = data.status;
                    });
            }
        });

        $scope.onClickTitle = function () {
            ObjectService.gotoUrl($scope.paymentInfo.parentType, $scope.paymentInfo.parentId);
        }

    }
]);