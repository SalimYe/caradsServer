app.controller('faq', function ($scope, $rootScope, $routeParams, $http, $location, $translate, $sce, $filter) {
    $scope.sce = $sce;
    $scope.isOpen = [];

    var filterFaqs = function (faqs) {
        return $filter('filter')(faqs, function (o) {
            if ($rootScope.realm.isAdvertiser)
                return o.roles.indexOf('all') >= 0 || o.roles.indexOf('advertiser') >= 0;
            if ($rootScope.realm.isDriver)
                return o.roles.indexOf('all') >= 0 || o.roles.indexOf('driver') >= 0;
            if ($rootScope.realm.isAdmin)
                return o.roles.indexOf('all') >= 0 || o.roles.indexOf('driver') >= 0 || o.roles.indexOf('advertiser') >= 0;
            return o.roles.indexOf('all') >= 0;
        });
    };

    $http.get('../../data/faq.json').
            success(function (data, status, headers, config) {
                $scope.faqs = filterFaqs(data);
            });

    $scope.changeItem = function (itemId) {
        $scope.selectedItem = itemId;
        if ($scope.isOpen[itemId] === undefined) {
            $scope.isOpen[itemId] = true;
        } else {
            $scope.isOpen[itemId] = !$scope.isOpen[itemId];
        }
        ;
    };

    $scope.selectItem = function (itemId) {
        $scope.selectedItem = itemId;
        $scope.isOpen[itemId] = true;
        $('html, body').animate({
            scrollTop: $("#faq" + itemId).offset().top
        }, 1500);
    };

});