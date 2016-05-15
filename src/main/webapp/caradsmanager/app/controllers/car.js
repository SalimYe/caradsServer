app.controller('car', function ($scope, $routeParams, $rootScope, $http, $location) {

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
                $location.path('/');
            });
            
    $scope.editCar = function () {
        $location.path('/driver/' + driverId + '/carEdit/' + carId);
    };

});