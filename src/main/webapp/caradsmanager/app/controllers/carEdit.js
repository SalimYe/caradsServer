app.controller('carEdit', function ($scope, $routeParams, $http, $location, $modal, $document, $timeout, $window, $translate) {

    var driverId = $routeParams.driverId;
    var carId = $routeParams.carId;
    $scope.driverId = driverId;

    $scope.colorRange = ["schwarz", "rot", "grün", "blau", "silbern",
        "gelb", "weiß"];

    if (driverId === undefined) {
        $location.path('/');
    }

    var redirectToCarOverview = function () {
        $location.path('driver/' + driverId + "/cars");
    };

    var isNewCar = ($routeParams.carId === undefined);
    $scope.car;
    $scope.isNewCar = isNewCar;

    if (!isNewCar) {
        $http.get('../api/drivers/' + driverId + '/cars/' + carId).
                success(function (data, status, headers, config) {
                    $scope.car = data;
                    $scope.car.buildYear = parseInt($scope.car.buildYear);
                }).
                error(function (data, status, headers, config) {
                    var title = 'alert.loadingError';
                    var description = 'alert.loadingErrorText';
                    var button = 'button.back';
                    var buttonFunction = function () {
                        $location.path('/driver/' + driverId + '/car/' + carId);
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);
                });
    }

    var updateCar = function () {
        $scope.sendRequest = true;
        if ($scope.carForm.$valid) {
            $http.put('../api/drivers/' + driverId + '/cars/' + carId, $scope.car).
                    success(function (data, status, headers, config) {
                        var title = 'alert.update';
                        var description = 'alert.updateText';
                        var button = 'button.next';
                        var buttonFunction = function () {
                            $location.path('/driver/' + driverId + '/car/' + carId);
                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }).
                    error(function (data, status, headers, config) {
                        var title = 'alert.updateError';
                        var description = 'alert.updateErrorText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    });
        }
    };

    $scope.deleteCar = function () {
        $http.delete('../api/drivers/' + driverId + '/cars/' + carId).
                success(function (data, status, headers, config) {
                    var title = 'alert.deleteSuccess';
                    var description = 'alert.deleteSuccessText';
                    var button = 'button.next';
                    var buttonFunction = function () {
                        redirectToCarOverview();
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);
                }).
                error(function (data, status, headers, config) {
                    if (status === 406) {
                        var title = 'alert.deleteErrorConstraint';
                        var description = 'alert.deleteErrorConstraintText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    } else {
                        var title = 'alert.deleteError';
                        var description = 'alert.deleteErrorText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }
                });
    };

    var createCar = function () {
        $scope.sendRequest = true;
        if ($scope.carForm.$valid) {
            $http.post('../api/drivers/' + driverId + '/cars/', $scope.car).
                    success(function (data, status, headers, config) {
                        var title = 'alert.createCar';
                        var description = 'alert.createCarText';
                        var button = 'button.next';
                        var buttonFunction = function () {
                            redirectToCarOverview();
                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                                            }).
                    error(function (data, status) {
                        var title = 'alert.creatFailed';
                        var description = 'alert.creatFailedText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    });
        }
    };

    $scope.saveCar = function () {
        if (isNewCar) {
            createCar();
        } else {
            updateCar();
        }
    };

    $scope.saveImages = function () {
        $timeout(function () {
            if ($scope.image !== undefined) {
                for (var i = 0; i < $scope.image.length; i++) {
                    var fd = new FormData();
                    fd.append('file', $scope.image[i]);
                    $http.post("../api/images/", fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    })
                            .success(function (data, status) {
                                var image = {id: data.id, isTitle: true, altText: ""};
                                if ($scope.car.images === undefined) {
                                    $scope.car.images = [];
                                }
                                $scope.car.images.push(image);
                            })
                            .error(function (data, status) {

                            });
                }
            }
        }, 400);

    };

    $scope.deleteImage = function (imageId) {
        $scope.car.images = $scope.car.images.filter(function (obj) {
            return obj.id !== imageId;
        });
    };

    $scope.datePicker = (function () {
        var method = {};
        method.instances = [];

        method.open = function ($event, instance) {
            $event.preventDefault();
            $event.stopPropagation();

            method.instances[instance] = true;
        };

        var formats = ['MM/dd/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        method.format = formats[3];

        return method;
    }());
});