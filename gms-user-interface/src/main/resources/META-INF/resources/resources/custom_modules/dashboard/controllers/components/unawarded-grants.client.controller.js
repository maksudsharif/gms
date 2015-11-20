'use strict';

/**
 * @ngdoc controller
 * @name dashboard.unawarded-grants.controller:Dashboard.MyCasesController
 *
 * @description
 *
 * {@link https://github.com/Armedia/ACM3/blob/develop/acm-user-interface/ark-web/src/main/webapp/resources/modules/dashboard/controllers/components/my-cases.client.controller.js modules/dashboard/controllers/components/my-cases.client.controller.js}
 *
 * Loads grants in the "Un-Awarded Grants" widget.
 */
angular.module('dashboard.unawarded-grants', ['adf.provider'])
    .config(function (dashboardProvider) {
        dashboardProvider
            .widget('unawardedGrants', {
                title: 'Un-Awarded Grants',
                description: 'Displays Un-Awarded Grants',
                controller: 'Dashboard.UnawardedGrantsController',
                reload: true,
                templateUrl: 'modules/dashboard/views/components/unawarded-grants.client.view.html'
            });
    })
    .controller('Dashboard.UnawardedGrantsController', ['$scope', '$translate', 'Authentication', 'Dashboard.DashboardService',
        function ($scope, $translate, Authentication, DashboardService) {

            $scope.$on('component-config', applyConfig);
            $scope.$emit('req-component-config', 'unawardedGrants');
            $scope.config = null;
            var userInfo = null;

            var paginationOptions = {
                pageNumber: 1,
                pageSize: 25,
                sortBy: 'id',
                sortDir: 'desc'
            };

            $scope.gridOptions = {
                enableColumnResizing: true,
                enableRowSelection: true,
                enableSelectAll: false,
                enableRowHeaderSelection: false,
                useExternalPagination: true,
                useExternalSorting: true,
                multiSelect: false,
                noUnselect: false,
                columnDefs: [],
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;

                    gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            paginationOptions.sort = null;
                        } else {
                            paginationOptions.sortBy = sortColumns[0].name;
                            paginationOptions.sortDir = sortColumns[0].sort.direction;
                        }
                        getPage();
                    });
                    gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        paginationOptions.pageNumber = newPage;
                        paginationOptions.pageSize = pageSize;
                        getPage();
                    });
                }
            };

            function applyConfig(e, componentId, config) {
                if (componentId == 'unawardedGrants') {
                    $scope.config = config;
                    $scope.gridOptions.columnDefs = config.columnDefs;
                    $scope.gridOptions.enableFiltering = config.enableFiltering;
                    $scope.gridOptions.paginationPageSizes = config.paginationPageSizes;
                    $scope.gridOptions.paginationPageSize = config.paginationPageSize;
                    paginationOptions.pageSize = config.paginationPageSize;

                    Authentication.queryUserInfoNew().then(function (responseUserInfo) {
                        userInfo = responseUserInfo;
                        getPage();
                        return userInfo;
                    });
                }
            }


            function getPage() {
                DashboardService.queryUnawardedGrants({
                        userId: userInfo.userId,
                        sortBy: paginationOptions.sortBy,
                        sortDir: paginationOptions.sortDir,
                        startWith: (paginationOptions.pageNumber - 1) * paginationOptions.pageSize,
                        pageSize: paginationOptions.pageSize
                    },
                    function (data) {
                        $scope.gridOptions.data = data.response.docs;
                        $scope.gridOptions.totalItems = data.response.numFound;
                    }
                );
            }
        }
    ]);