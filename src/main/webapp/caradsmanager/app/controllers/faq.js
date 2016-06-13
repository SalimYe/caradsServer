app.controller('faq', function ($scope, $routeParams, $http, $location, $translate) {

    $scope.isOpen = [];

    $http.get('../../data/faq.json').
            success(function (data, status, headers, config) {
                $scope.faqs = data;
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
    
    $scope.isDriverFaq = function (roles) {
        if(roles.indexOf('driver') >= 0)
            return true;
        return false;
    };
    
    $scope.isAdvertiserFaq = function (roles) {
        if(roles.indexOf('advertiser') >= 0)
            return true;
        return false;
    };
    
    $scope.isDriver = true;

});