app.controller('campaigns', function ($scope, $routeParams, $http, $location, $translate, $modal) {

    var advertiserId = $routeParams.advertiserId;
    $scope.advertiserId = advertiserId;

    if (advertiserId === undefined) {
        $location.path('/');
    }

    $http.get('../api/advertisers/' + advertiserId + '/campaigns').
            success(function (data, status, headers, config) {
                $scope.campaigns = data;
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

    $scope.hasImage = function (index) {
        if ($scope.campaigns[index].images === undefined)
            return false;
        if ($scope.campaigns[index].images.length >= 1)
            return true;
        return false;
    };

    $scope.getProfileImageId = function (index) {
        return $scope.campaigns[index].images[0].id;
    };

});