startapp.controller('faq', function ($scope, $routeParams, $http, $location, $translate, $sce, $filter) {
    $scope.sce = $sce;
    $scope.isOpen = [];

    $http.get('./data/faq.json').
            success(function (data, status, headers, config) {
                var faqs = data;
                $scope.faqs = $filter('filter')(faqs, function (o){
                    return o.roles.indexOf('all') >= 0;
                });
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