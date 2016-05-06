app.controller('advertiser', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate, $timeout) {

    var advertiserId = $routeParams.id;
    if (advertiserId === undefined) {
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

        $http.get('../api/advertisers/' + advertiserId).
                success(function (data, status, headers, config) {
                    $scope.advertiser = data;
                }).
                error(function (data, status, headers, config) {
                    $location.path('/');
                });


    $scope.updateAdvertiser = function () {
        $http.put('../api/advertisers/' + advertiserId, $scope.advertiser).
                success(function (data, status, headers, config) {
                    // TODO
                }).
                error(function (data, status, headers, config) {
                    //TODO
                });
    };

    $scope.exitAdvertiser = function () {
        $location.path('/');
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