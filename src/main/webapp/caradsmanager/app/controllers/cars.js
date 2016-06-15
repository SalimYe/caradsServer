app.controller('cars', function ($scope, $routeParams, $http, $location, $modal, $document, $timeout, $window, $translate) {

    var driverId = $routeParams.driverId;
    $scope.driverId = driverId;

    if (driverId === undefined) {
        $location.path('/');
    }

    $http.get('../api/drivers/' + driverId + '/cars').
            success(function (data, status, headers, config) {
                $scope.cars = data;
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

    $scope.hasImage = function (carIndex) {
        if ($scope.cars[carIndex].images === undefined)
            return false;
        if ($scope.cars[carIndex].images.length >= 1)
            return true;
        return false;
    };

    $scope.getProfileImageId = function (carIndex) {
        return $scope.cars[carIndex].images[0].id;
    };

});