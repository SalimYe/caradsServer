app.controller('navigation', function ($rootScope, $http) {
    $rootScope.realm = [];
    $http.get('../api/realms/').
            success(function (data, status, headers, config) {
                $rootScope.realm.username = data.username;
                var roles = data.roles;
                for (var i = 0; i < roles.length; i++) {
                    if(roles[i].name == 'driver') {
                        $rootScope.realm.driverId = roles[i].roleId;
                        $rootScope.realm.isDriver = true;
                    } else if (roles[i].name == 'advertiser') {
                        $rootScope.realm.advertiserId = roles[i].roleId;
                        $rootScope.realm.isAdvertiser = true;
                    } else if (roles[i].name == 'carads-admin') {
                        $rootScope.realm.isAdmin = true;
                    }
                };
            }).
            error(function (data, status, headers, config) {
                // TODO
            });
});