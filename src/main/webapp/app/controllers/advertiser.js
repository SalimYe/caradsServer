startapp.controller('advertiser', function ($scope, $routeParams, $http, $timeout, $translate) {

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

    $scope.registerAdvertiser = function () {
        $http.post('./api/advertisers/', $scope.advertiser).
                success(function (data, status, headers, config) {
                    alert("Werbender angelegt", "Der Werbende wurde erfolgreich registriert!", "success");
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
                        $scope.advertiser.logo = image;
                    })
                    .error(function (data, status) {

                    });
        }, 200);
    };

    $scope.deleteImage = function () {
        delete $scope.advertiser.logo;
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