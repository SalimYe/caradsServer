app.controller('campaign', function ($scope, $rootScope, $routeParams, $http, $location, $timeout) {

    var advertiserId = $routeParams.advertiserId;
    var campaignId = $routeParams.campaignId;
    $scope.advertiserId = advertiserId;

    if (advertiserId === undefined ||
            campaignId === undefined) {
        $location.path('/');
    }

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    var redirectToCampaignOverview = function () {
        $location.path('advertiser/' + advertiserId + "/campaigns");
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };
    
    var getCarsWithState = function (fellows) {
        var cars = [];
        if (fellows === undefined ||
                fellows.length === 0) {
                return cars;
        }
        for(var i = 0; i < fellows.length; i++) {
            var fellow = fellows[i];
            var car = cars[i] = [];
            car.id = fellow.carId;
            car.state = fellow.state;
        }
        return cars;        
    };

        $http.get('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId).
                success(function (data, status, headers, config) {
                    $scope.campaign = data;
                    $scope.cars = getCarsWithState($scope.campaign.fellows);
                }).
                error(function (data, status, headers, config) {
                    redirectToCampaignOverview();
                });

    $scope.exitCampaign = function () {
        redirectToCampaignOverview();
    };
    
    $scope.editCampaign = function () {
        $location.path('advertiser/' + advertiserId + '/campaignEdit/' + campaignId);
    };
    
    $scope.showAvailableCars = function () {
        $location.path('advertiser/' + advertiserId + '/campaign/' + campaignId + '/availableCars/');
    };

});