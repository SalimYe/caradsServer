app.controller('advertisers', function ($scope, $routeParams, $http, $location, $modal, $document, $window, $translate) {

    $scope.advertisers;

    $http.get('../api/advertisers/').
            success(function (data, status, headers, config) {
                $scope.advertisers = data;
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
        if(imageId === undefined) {
            return "./img/symbols/company_placeholder.png";
        }
        return '../../api/images/' + imageId;
    };
});