app.controller('advertiser', function ($scope, $routeParams, $http, $location, $modal, $timeout, $translate) {

    var advertiserId = $routeParams.id;
    if (advertiserId === undefined) {
        $location.path('/');
    }

    $http.get('../api/advertisers/' + advertiserId).
            success(function (data, status, headers, config) {
                $scope.advertiser = data;
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

    $scope.exitAdvertiser = function () {
        $location.path('/');
    };

    $scope.editDetails = function () {
        $location.path($location.path() + '/edit');
    };


});