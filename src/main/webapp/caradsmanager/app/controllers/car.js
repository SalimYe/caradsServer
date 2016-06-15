app.controller('car', function ($scope, $routeParams, $rootScope, $http, $location, $translate) {

    var driverId = $routeParams.driverId;
    var carId = $routeParams.carId;
    $scope.driverId = driverId;

    if (driverId === undefined || carId === undefined) {
        $location.path('/');
    }

    $http.get('../api/drivers/' + driverId + '/cars/' + carId).
            success(function (data, status, headers, config) {
                $scope.car = data;
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

    $scope.editCar = function () {
        $location.path('/driver/' + driverId + '/carEdit/' + carId);
    };

});