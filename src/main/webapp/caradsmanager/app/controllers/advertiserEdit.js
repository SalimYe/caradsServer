app.controller('advertiserEdit', function ($scope, $routeParams, $http, $location, $modal, $timeout, $translate) {

    var advertiserId = $routeParams.id;
    if (advertiserId === undefined) {
        $location.path('/');
    }

    $http.get('../api/advertisers/' + advertiserId).
            success(function (data, status, headers, config) {
                $scope.advertiser = data;
            }).
            error(function (data, status, headers, config) {
                var title = 'alert.loadingError';
                var description = 'alert.loadingErrorText';
                var button = 'button.back';
                var buttonFunction = function () {
                    window.history.back();
                };
                showModal($modal, description, title, button, null, buttonFunction, null, angular);
            });


    $scope.updateAdvertiser = function () {
        $scope.sendRequest = true;
        if ($scope.advertiserForm.$valid) {
            $http.put('../api/advertisers/' + advertiserId, $scope.advertiser).
                    success(function (data, status, headers, config) {
                        var title = 'alert.update';
                        var description = 'alert.updateText';
                        var button = 'button.next';
                        var buttonFunction = function () {
                            $location.path('/advertiser/' + advertiserId);
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
        }
    };

    $scope.exitAdvertiser = function () {
        $location.path('/advertiser/' + advertiserId);
    };

    $scope.editDetails = function () {
        $location.path($location.path() + '/edit');
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

    var logout = function () {
        $http.get('../api/realms/logout');
    };

    $scope.deleteAdvertiser = function () {
        $http.delete('../api/advertisers/' + advertiserId).
                success(function (data, status, headers, config) {
                    var title = 'alert.deleteSuccess';
                    var description = 'alert.deleteSuccessText';
                    var button = 'button.next';
                    var buttonFunction = function () {
                        logout();
                        window.location.href = '../';
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);
                }).
                error(function (data, status, headers, config) {
                    if (status === 406) {
                        var title = 'alert.deleteErrorConstraint';
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