app.controller('campaigns', function ($scope, $routeParams, $http, $location) {

    var advertiserId = $routeParams.advertiserId;
    $scope.advertiserId = advertiserId;

    if (advertiserId === undefined) {
        $location.path('/');
    }

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $http.get('../api/advertisers/' + advertiserId + '/campaigns').
            success(function (data, status, headers, config) {
                $scope.campaigns = data;
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });

    $scope.hasImage = function (index) {
        if ($scope.campaigns[index].images === undefined)
            return false;
        if ($scope.campaigns[index].images.length >= 1)
            return true;
        return false;
    };

    $scope.getProfileImageId = function (index) {
        return $scope.campaigns[index].images[0].id;
    };

});