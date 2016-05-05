startapp.controller('driver', function ($scope, $routeParams, $http, $timeout) {
    
    $scope.driver = {};

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    $scope.registerDriver = function () {
        $http.post('./api/drivers/', $scope.driver).
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

    $scope.saveImage = function () {
        $timeout(function () {
            var fd = new FormData();
            fd.append('file', $scope.image[0]);
            $http.post("./api/images/", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                    .success(function (data, status) {
                        var image = {id: data.id, isTitle: true, altText: ""};
                        $scope.driver.profilePicture = image;
                    })
                    .error(function (data, status) {

                    });
        }, 200);

    };

    $scope.deleteImage = function () {
        delete $scope.driver.profilePicture;
    };
});