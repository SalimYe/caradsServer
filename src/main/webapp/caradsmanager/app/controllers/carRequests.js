app.controller('carRequests', function ($scope, $route, $rootScope, ngTableParams, $filter, $location, $http, $modal, $translate) {
    
    var driverId = $rootScope.realm.driverId;
    
    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };
    
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
                        alert("Fehler", "Fahrzeug Anfragen konnten aufgrund technischer Probleme\n\
                            nicht geladen werden.", "danger");
                    });
    }
    
    $scope.openCampaignDetails = function (carId, advertiserId, campaignId) {
        $location.path('/advertiser/' + advertiserId + '/campaign/' 
                 + campaignId + '/carRequest/' + carId);
    };
    
    var sendResponse = function (response) {
        var url = '../api/drivers/' + $rootScope.realm.driverId + '/response';
        
        $http.put(url, response).
                    success(function (data, status, headers, config) {
                        $route.reload();
                    }).
                    error(function (data, status, headers, config) {
                        alert("Fehler", "Fahrzeug Anfragen konnten aufgrund technischer Probleme\n\
                            nicht beantwortet werden.", "danger");
                    });
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
        
        var actionOne = function () {
            
        };
        
        var actionTwo = function () {
            sendResponse(response);
        };
        
        showModal($modal, "Bist Du sicher, dass Du verbindlich auf das Angebot\n\
            antworten willst? Alle anderen Anfragen, die sich zeitlich mit dieser\n\
            Kampagne überschneiden, werden automatisch abgelehnt.",
            "Anwort senden", "Abbrechen", "Senden", actionOne, actionTwo, angular);
        
    };
    
    $scope.getStateLabel = function (state) {
        return getStateLabel(state);
    };
    
    $scope.getStateLabelBoostrapState = function (state) {
        return getStateLabelBoostrapState(state);
    };
       
});