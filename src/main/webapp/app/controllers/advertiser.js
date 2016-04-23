app.controller('advertiser', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    $scope.advertiser;
    var isNewAdvertiser = ($routeParams.id === undefined);

    if (!isNewAdvertiser) {
        $http.get('/api/advertisers/' + $routeParams.id).
                success(function (data, status, headers, config) {
                    $scope.advertiser = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('advertiser/');
                });
    }

    var updateAdvertiser = function () {
        $http.put('/api/advertisers/' + $routeParams.id, $scope.driver).
                success(function (data, status, headers, config) {
                    // TODO
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    var createAdvertiser = function () {
        $http.post('/api/advertisers/', $scope.advertiser).
                success(function (data, status, headers, config) {
                    alert("Fahrer angelegt", "Der Fahrer wurde erfolgreich registriert!", "success");
                    $routeParams.id = data.id;
                }).
                error(function (data, status) {
                    if (status === 409) {
                        alert("Speichern fehlgeschlagen", "Es ist bereits ein Fahrer mit der Mail \""
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

    };

    $scope.saveImage = function () {
        var fd = new FormData();
        fd.append('file', $scope.image);
        $http.post("http://localhost:8080/api/images/", fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
                .success(function (data, status) {
                    var image = {id: data.id, isTitle: true, altText: $scope.advertiser.firstname + " " + $scope.advertiser.lastname};
                    $scope.advertiser.profilePicture = image;

                })
                .error(function (data, status) {

                });
    };

    $scope.deleteImage = function () {
        delete $scope.advertiser.profilePicture;
    };

    $scope.getImageUrl = function () {
        return '../api/images/' + $scope.advertiser.profilePicture.id;
    };
});