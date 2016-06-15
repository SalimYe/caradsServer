app.controller('driver', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate, $timeout) {

    var driverId = $routeParams.id;

    if (driverId === undefined) {
        $location.path('/');
    }

    $http.get('../api/drivers/' + driverId).
            success(function (data, status, headers, config) {
                $scope.driver = data;
            }).
            error(function (data, status, headers, config) {
                var title = 'alert.loadingError';
                var description = 'alert.loadingErrorText';
                var button = 'button.back';
                var buttonFunction = function () {
                    $location.path('/home');
                };
                showModal($modal, description, title, button, null, buttonFunction, null, angular);
            });

    $scope.updateDriver = function () {
        $http.put('../api/drivers/' + driverId, $scope.driver).
                success(function (data, status, headers, config) {
                    var title = 'alert.update';
                    var description = 'alert.updateText';
                    var button = 'button.next';
                    var buttonFunction = function () {
                        $location.path('driver/' + $scope.driver.id);
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
                    var title = 'alert.deleteSuccess';
                    var description = 'alert.deleteSuccessText';
                    var button = 'button.next';
                    var buttonFunction = function () {
                        logout();
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);  
                }).
                error(function (data, status, headers, config) {
                    if (status === 406) {
                            var title = 'alert.deleteError';
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