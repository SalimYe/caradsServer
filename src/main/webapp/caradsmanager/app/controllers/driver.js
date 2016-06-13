app.controller('driver', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate, $timeout) {

    var driverId = $routeParams.id;

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

    $http.get('../api/drivers/' + driverId).
            success(function (data, status, headers, config) {
                $scope.driver = data;
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });

    $scope.updateDriver = function () {
        $http.put('../api/drivers/' + driverId, $scope.driver).
                success(function (data, status, headers, config) {
                    $location.path('driver/' + $scope.driver.id);
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    $scope.exitDriver = function () {
        $location.path('/');
    };
    
    $scope.editDetails = function () {
        $location.path($location.path() + '/edit');
    };
    
    var logout = function () {
        $http.get('../api/realms/logout');
    };
    
    $scope.deleteDriver = function () {
        $http.delete('../api/drivers/' + driverId).
                success(function (data, status, headers, config) {
                    logout();
                }).
                error(function (data, status, headers, config) {
                    alert("Löschen fehlgeschlagen", "Fahrer konnte nicht gelöscht werden.", "danger");
                });
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
                        $scope.driver.profilePicture = image;
                    })
                    .error(function (data, status) {

                    });
        }, 200);

    };

    $scope.deleteImage = function () {
        delete $scope.driver.profilePicture;
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