app.controller('driver', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    var driverId = $routeParams.id;
    var isNewDriver = (driverId === undefined);
    $scope.driver;
    $scope.isNewDriver = isNewDriver;

    if (!isNewDriver) {
        $http.get('/api/drivers/' + driverId).
                success(function (data, status, headers, config) {
                    $scope.driver = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('driver/');
                });
    }

    var updateDriver = function () {
        $http.put('/api/drivers/' + driverId, $scope.driver).
                success(function (data, status, headers, config) {
                    $location.path('driver/' + $scope.driver.id);
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    var createDriver = function () {
        $http.post('/api/drivers/', $scope.driver).
                success(function (data, status, headers, config) {
                    alert("Fahrer angelegt", "Der Fahrer wurde erfolgreich registriert!", "success");
                }).
                error(function (data, status) {
                    if (status === 409) {
                        alert("Speichern fehlgeschlagen", "Es ist bereits ein Fahrer mit der Mail \""
                                + $scope.driver.email + "\" registiert!", "danger");
                    } else {
                        alert("Speichern fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " +
                                "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                                "den Administrator.", "danger");
                    }

                });
    };

    $scope.saveDriver = function () {
        if (isNewDriver) {
            createDriver();
        } else {
            updateDriver();
        }
    };
    
    $scope.deleteDriver = function () {
        $http.delete('/api/drivers/' + driverId).
                success(function (data, status, headers, config) {
                    $location.path('/home/');
                }).
                error(function (data, status, headers, config) {
                    alert("Löschen fehlgeschlagen", "Der Fahrer konnte nicht gelöscht werden.");
                });
    };

    $scope.exitDriver = function () {
        $location.path('/home/');
    };

    $scope.addCar = function () {
        $location.path('/driver/' + driverId + '/car/');
    };

    $scope.editCar = function (carId) {
        $location.path('/driver/' + driverId + '/car/' + carId);
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
}).directive("fileread", [function () {
        return {
            scope: {
                fileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    scope.$apply(function () {
                        scope.fileread = changeEvent.target.files[0];
                        // or all selected files:
                        // scope.fileread = changeEvent.target.files;
                    });
                });
            }
        }
    }]);