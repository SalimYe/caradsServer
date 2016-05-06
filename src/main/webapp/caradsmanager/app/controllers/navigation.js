app.controller('navigation', function ($scope, $http) {
    $http.get('../api/realms/').
            success(function (data, status, headers, config) {
                $scope.username = data.username;
                var roles = data.roles;
                for (var i = 0; i < roles.length; i++) {
                    if(roles[i].name == 'driver') {
                        $scope.driverId = roles[i].roleId;
                    } else if (roles[i].name == 'advertiser') {
                        $scope.advertiserId = roles[i].roleId;
                    } else if (roles[i].name == 'admin') {
                        $scope.isAdmin = true;
                    }
                };
            }).
            error(function (data, status, headers, config) {
                // TODO
            });
});