app.controller('campaign', function ($scope, $rootScope, $routeParams, $http, $location, $filter, ngTableParams, $translate, $modal) {

    var advertiserId = $routeParams.advertiserId;
    var campaignId = $routeParams.campaignId;
    $scope.advertiserId = advertiserId;

    if (advertiserId === undefined ||
            campaignId === undefined) {
        $location.path('/');
    }

    $http.get('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId).
            success(function (data, status, headers, config) {
                $scope.campaign = data;
                $scope.fellows = $scope.campaign.enrichedFellows;
                if ($scope.fellows !== undefined) {
                    $scope.fillTable($scope.fellows);
                    $scope.tableLoaded = true;
                }
            }).
            error(function (data, status, headers, config) {
                var title = 'alert.loadingError';
                var description = 'alert.loadingErrorText';
                var button = 'button.back';
                var buttonFunction = function () {
                    $location.path('/home');
                };
                showModal($modal, description, title, button, null, buttonFunction, null, angular);
            });


    $scope.editCampaign = function () {
        $location.path('advertiser/' + advertiserId + '/campaignEdit/' + campaignId);
    };

    $scope.showAvailableCars = function () {
        $location.path('advertiser/' + advertiserId + '/campaign/' + campaignId + '/availableCars/');
    };

    $scope.fillTable = function (data) {
        $scope.tableParams = new ngTableParams({
            page: 1, // show first page
            count: 10          // count per page
        }, {
            total: data.length, // length of data
            getData: function ($defer, params) {
                // use build-in angular filter
                var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) :
                        data;

                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    };

    $scope.getStateLabel = function (state) {
        return getStateLabel(state);
    };

    $scope.getStateLabelBoostrapState = function (state) {
        return getStateLabelBoostrapState(state);
    };

    $scope.openCarDetails = function (driverId, carId) {
        $location.path('driver/' + driverId + '/car/' + carId);
    };

    $scope.openDriverDetails = function (driverId, carId) {
        $location.path('driver/' + driverId);
    };
});