app.controller('advertiser', function ($scope, $routeParams, $http, $location, $modal, $timeout) {

    var advertiserId = $routeParams.id;
    if (advertiserId === undefined) {
        $location.path('/');
    }

    $http.get('../api/advertisers/' + advertiserId).
            success(function (data, status, headers, config) {
                $scope.advertiser = data;
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });

    $scope.exitAdvertiser = function () {
        $location.path('/');
    };

    $scope.editDetails = function () {
        $location.path($location.path() + '/edit');
    };


});