app.controller('driver', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    $scope.driver;
    var isNewDriver = ($routeParams.id === undefined);

    if (!isNewDriver) {
        $http.get('/api/drivers/' + $routeParams.id).
                success(function (data, status, headers, config) {
                    $scope.driver = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('driver/');
                });
    }

    var updateDriver = function () {
        $http.put('/api/drivers/' + $routeParams.id, $scope.driver).
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
                    $location.path('driver/' + data.id);
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    $scope.saveDriver = function () {
        if (isNewDriver) {
            createDriver();
        } else {
            updateDriver();
        }
    };

    $scope.exitDriver = function () {
        $location.path('/saveDriver');
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