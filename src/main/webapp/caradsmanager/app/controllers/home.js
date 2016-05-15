app.controller('home', function ($scope, $rootScope, $translate, ngTableParams, $filter, $location, $http) {
    
    var driverId = $rootScope.realm.driverId;
    
    $scope.fillTable = function (data) {
        $scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10          // count per page
    }, {
        total: data.length, // length of data
        getData: function($defer, params) {
            // use build-in angular filter
            var orderedData = params.sorting() ?
                                $filter('orderBy')( data, params.orderBy()) :
                                 data;

            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });};
    
    if($rootScope.realm.isDriver) {
        $http.get('../api/drivers/' + driverId + '/requests').
                    success(function (data, status, headers, config) {
                        $scope.carRequests = data;
                        $scope.fillTable($scope.carRequests);
                        $scope.tableLoaded = true;
                    }).
                    error(function (data, status, headers, config) {
                        $location.path('/');
                    });
    }
    
    $scope.openCampaignDetails = function (carId, advertiserId, campaignId) {
        $location.path('/advertiser/' + advertiserId + '/campaign/' 
                 + campaignId + '/carRequest/' + carId);
    };
    
    $scope.sendResponse = function (carId, advertiserId, campaignId, commitment) {
        
        var response = {};
        response.carId = carId;
        response.advertiserId = advertiserId;
        response.campaignId = campaignId;
        
        if(commitment) {
            response.response = 'ACCEPTED';
        } else {
            response.response = 'REJECTED';
        }
        
        var url = '../api/drivers/' + $rootScope.realm.driverId + '/response';
        
        $http.put(url, response).
                    success(function (data, status, headers, config) {
                        $location.path('/');
                    }).
                    error(function (data, status, headers, config) {
                        $location.path('/');
                    });
    };
    
    $scope.getStateLabel = function (state) {
        return getStateLabel(state);
    };
    
    $scope.getStateLabelBoostrapState = function (state) {
        return getStateLabelBoostrapState(state);
    };
       
});