app.controller('navigation', function ($rootScope, $scope, $http, $location, $translate) {

    $rootScope.realm = [];
    
    var getProfilDetails = function () {
        if ($rootScope.realm.isDriver) {
            $http.get('../api/drivers/' + $rootScope.realm.driverId).
                    success(function (data, status, headers, config) {
                        $rootScope.driver = data;
                    }).
                    error(function (data, status, headers, config) {
                        $location.path('/');
                    });
        }

        if ($rootScope.realm.isAdvertiser) {
            $http.get('../api/advertisers/' + $rootScope.realm.advertiserId).
                    success(function (data, status, headers, config) {
                        $rootScope.advertiser = data;
                    }).
                    error(function (data, status, headers, config) {
                        $location.path('/');
                    });
        }
    };

    $http.get('../api/realms/').
            success(function (data, status, headers, config) {
                $rootScope.realm.username = data.username;
                var roles = data.roles;
                for (var i = 0; i < roles.length; i++) {
                    if (roles[i].name == 'driver') {
                        $rootScope.realm.driverId = roles[i].roleId;
                        $rootScope.realm.isDriver = true;
                    } else if (roles[i].name == 'advertiser') {
                        $rootScope.realm.advertiserId = roles[i].roleId;
                        $rootScope.realm.isAdvertiser = true;
                    } else if (roles[i].name == 'carads-admin') {
                        $rootScope.realm.isAdmin = true;
                    }
                }
                ;
                getProfilDetails();
            }).
            error(function (data, status, headers, config) {
                // TODO
            });

});