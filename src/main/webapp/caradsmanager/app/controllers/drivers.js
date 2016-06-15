app.controller('drivers', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    $scope.drivers;

    $http.get('../api/drivers/').
            success(function (data, status, headers, config) {
                $scope.drivers = data;
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

    $scope.getProfileImage = function (imageId) {
        if (imageId === undefined) {
            return "./img/symbols/empty_profilepicture.jpg";
        }
        return '../../api/images/' + imageId;
    };
});