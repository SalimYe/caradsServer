app.controller('drivers', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    $scope.drivers;

    $http.get('../api/drivers/').
            success(function (data, status, headers, config) {
                $scope.drivers = data;
            }).
            error(function (data, status, headers, config) {
                // TODO
            });
            
    $scope.getProfileImage = function (imageId) {
        if(imageId === undefined) {
            return "../../img/symbols/empty_profilepicture.jpg";
        }
        return '../../api/images/' + imageId;
    };
});