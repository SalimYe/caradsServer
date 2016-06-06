app.controller('accountSettings', function ($scope, $rootScope, $http, $location, $modal) {

    $scope.credentials = {};
    $scope.credentials.newPassword;
    $scope.credentials.oldPassword;

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
    };

    $scope.updatePassword = function () {

        if ($scope.accountForm.$valid) {

            var accountId = "";

            if ($rootScope.realm.isAdvertiser) {
                accountId = $rootScope.realm.advertiserId;
            } else if ($rootScope.realm.isDriver) {
                accountId = $rootScope.realm.driverId;
            }

            $http.put('../api/realms/' + accountId + '/password', $scope.credentials).
                    success(function (data, status, headers, config) {
                        alert("Passwort ge&auml;ndert", "Das Password wurde erfolgreich aktualisiert!", "success");
                    }).
                    error(function (data, status, headers, config) {
                        alert("Änderungen fehlgeschlagen", "Das Passwort konnte nicht aktualisiert werden.\n\
                        Sollte dieser Fehler öfters erscheinen, wenden Sie sich an den Administrator.", "danger");
                    });
        }
        ;

    };

});