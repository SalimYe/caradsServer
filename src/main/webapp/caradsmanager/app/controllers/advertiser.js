app.controller('advertiser', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate, $timeout) {

    $scope.advertiser = {};

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };


    var isNewAdvertiser = ($routeParams.id === undefined);

    if (!isNewAdvertiser) {
        $http.get('../api/advertisers/' + $routeParams.id).
                success(function (data, status, headers, config) {
                    $scope.advertiser = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('advertiser/');
                });
    }

    var updateAdvertiser = function () {
        $http.put('../api/advertisers/' + $routeParams.id, $scope.advertiser).
                success(function (data, status, headers, config) {
                    // TODO
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    var createAdvertiser = function () {
        $http.post('../api/advertisers/', $scope.advertiser).
                success(function (data, status, headers, config) {
                    alert("Werbender angelegt", "Der Werbende wurde erfolgreich registriert!", "success");
                    $routeParams.id = data.id;
                }).
                error(function (data, status) {
                    if (status === 409) {
                        alert("Speichern fehlgeschlagen", "Es ist bereits ein Werbender mit der Mail \""
                                + $scope.advertiser.email + "\" registiert!", "danger");
                    } else {
                        alert("Speichern fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " + 
                                "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                                "den Administrator.", "danger");
                    }

                });
    };

    $scope.saveAdvertiser = function () {
        if (isNewAdvertiser) {
            createAdvertiser();
        } else {
            updateAdvertiser();
        }
    };

    $scope.exitAdvertiser = function () {
        $location.path('/home/');
    };

    $scope.saveImage = function () {
        $timeout(function () {
            var fd = new FormData();
            fd.append('file', $scope.image[0]);
            $http.post("../api/images/", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                    .success(function (data, status) {
                        var image = {id: data.id, isTitle: true, altText: ""};
                        $scope.advertiser.logo = image;
                    })
                    .error(function (data, status) {

                    });
        }, 200);
    };

    $scope.deleteImage = function () {
        delete $scope.advertiser.logo;
    };
});