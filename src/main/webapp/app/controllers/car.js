app.controller('car', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    var driverId = $routeParams.driverId;
    var carId = $routeParams.carId;

    if (driverId === undefined) {
        $location.path('/');
    }

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    $scope.car;

    var isNewCar = ($routeParams.carId === undefined);

    if (!isNewCar) {
        $http.get('/api/drivers/' + driverId + '/cars/' + carId).
                success(function (data, status, headers, config) {
                    $scope.car = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('driver/' + driverId);
                });
    }

    var updateCar = function () {
        $http.put('/api/drivers/' + driverId + '/cars/' + carId, $scope.car).
                success(function (data, status, headers, config) {
                    //TODO
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    var createCar = function () {
        console.log($scope.car);
        $http.post('/api/drivers/' + driverId + '/cars/', $scope.car).
                success(function (data, status, headers, config) {
                    alert("Fahrzeug angelegt", "Das Fahrzeug wurde erfolgreich angelegt!", "success");
                }).
                error(function (data, status) {
                    alert("Speichern fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " +
                            "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                            "den Administrator.", "danger");
                });
    };

    $scope.saveCar = function () {
        if (isNewCar) {
            createCar();
        } else {
            updateCar();
        }
    };

    $scope.exitCar = function () {

    };

    $scope.saveImage = function () {
        var fd = new FormData();
        fd.append('file', $scope.image);
        $http.post("http://localhost:8080/api/images/", fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
                .success(function (data, status) {
                    var image = {id: data.id, isTitle: true, altText: $scope.driver.firstname + " " + $scope.driver.lastname};
                    $scope.driver.profilePicture = image;

                })
                .error(function (data, status) {

                });
    };

    $scope.deleteImage = function () {
        delete $scope.driver.profilePicture;
    };

    $scope.getImageUrl = function () {
        return '../api/images/' + $scope.driver.profilePicture.id;
    };
}).directive("carimage", [function () {
        return {
            scope: {
                carimage: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    scope.$apply(function () {
                        scope.carimage = changeEvent.target.files[0];
                        // or all selected files:
                        // scope.fileread = changeEvent.target.files;
                    });
                });
            }
        }
    }]);