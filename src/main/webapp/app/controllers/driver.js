startapp.controller('driver', function ($scope, $routeParams, $http, $timeout, $translate, $modal) {

    $scope.driver = {};

    $scope.registerDriver = function () {
        $scope.sendRequest = true;
        if ($scope.driverForm.$valid) {
            $http.post('./api/drivers/', $scope.driver).
                    success(function (data, status, headers, config) {
                        var title = 'alert.registrationSuccess';
                        var description = 'alert.registrationSuccessText';
                        var button = 'nav.login';
                        var buttonFunction = function () {
                            window.location = "../../../caradsmanager/";
                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }).
                    error(function (data, status) {
                        if (status === 409) {
                            var title = 'alert.registrationError';
                            var description = 'alert.registrationErrorDuplicateText';
                            var button = 'button.back';
                            var buttonFunction = function () {

                            };
                            showModal($modal, description, title, button, null, buttonFunction, null, angular);
                        } else {
                            var title = 'alert.registrationError';
                            var description = 'alert.registrationErrorText';
                            var button = 'button.back';
                            var buttonFunction = function () {

                            };
                            showModal($modal, description, title, button, null, buttonFunction, null, angular);
                        }
                    });
        }
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
                        if (status === 406) {
                            var title = 'alert.imageFormatError';
                            var description = 'alert.imageFormatErrorText';
                            var button = 'button.back';
                            var buttonFunction = function () {

                            };
                            showModal($modal, description, title, button, null, buttonFunction, null, angular);
                        } else {
                            var title = 'alert.imageError';
                            var description = 'alert.imageErrorText';
                            var button = 'button.back';
                            var buttonFunction = function () {

                            };
                            showModal($modal, description, title, button, null, buttonFunction, null, angular);
                        }
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