app.controller('advertiserEdit', function ($scope, $routeParams, $http, $location, $modal, $timeout){

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