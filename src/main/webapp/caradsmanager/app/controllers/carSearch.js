app.controller('carSearch', function ($scope, $cookieStore, $route, $routeParams, $http, $location, $modal, $document, $timeout, $window, $translate, $filter, $modal) {

    var advertiserId = $routeParams.advertiserId;
    var campaignId = $routeParams.campaignId;
    if (advertiserId === undefined ||
            campaignId === undefined) {
        $location.path('/');
    }

    var getArrayElementByKeyValue = function (key, value, array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i][key] === value) {
                return array[i];
            }
        }
    };

    var removeArrayElement = function (element, array) {
        var index = array.indexOf(element);
        if (index > -1) {
            array.splice(index, 1);
        }
    };

    var getCarSelectionCookie = function (campaignId) {
        var carSelection = $cookieStore.get('cs' + campaignId);
        if (carSelection === undefined) {
            return [];
        } else {
            return carSelection;
        }
    };

    var setSelectedFlag = function (cars, carSelection) {
        for (var i = 0; i < cars.length; i++) {
            var car = cars[i];
            var index = carSelection.indexOf(car.id);
            if (index > -1) {
                car.isSelected = true;
            } else {
                car.isSelected = false;
            }

        }
    };

    var deleteSelectedFlag = function (cars) {
        for (var i = 0; i < cars.length; i++) {
            var car = cars[i];
            car.isSelected = false;
        }
    };

    var updateCarSelectionCookie = function (campaignId) {
        $cookieStore.put('cs' + campaignId, $scope.carSelection);
    };

    $scope.updateCarSelection = function (carId) {
        var currentCar = getArrayElementByKeyValue('id', carId, $scope.cars);
        if (currentCar.isSelected === undefined ||
                currentCar.isSelected === false) {
            currentCar.isSelected = true;
            $scope.carSelection.push(carId);
        } else {
            currentCar.isSelected = false;
            removeArrayElement(carId, $scope.carSelection);
        }
        updateCarSelectionCookie($scope.campaignId);
    };

    $scope.deleteCarSelection = function () {
        $scope.carSelection = [];
        deleteSelectedFlag($scope.cars);
        deleteSelectedFlag($scope.filteredCars);
        updateCarSelectionCookie(campaignId);
    };
    
    $scope.showCampaignDetails = function () {
        $location.path('advertiser/' + advertiserId + '/campaign/' + campaignId);
    };

    var init = function () {
        $scope.sortOptions = [{'label': 'Marke', 'value': 'brand'},
            {'label': 'Modell', 'value': 'model'},
            {'label': 'Farbe', 'value': 'color'}];
        $scope.sequenceOptions = [{label: 'aufsteigend', value: false},
            {label: 'absteigend', value: true}];
        $scope.reverse = false;
        $scope.predicate = 'brand';
        $scope.campaignId = campaignId;
        $scope.carSelection = getCarSelectionCookie(campaignId);
    };

    var orderBy = $filter('orderBy');

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    $scope.order = function (predicate) {
        if (predicate !== undefined) {
            $scope.predicate = predicate;
        } else {
            predicate = $scope.predicate;
        }
        $scope.cars = orderBy($scope.cars, predicate, $scope.reverse);
        $scope.filteredCars = orderBy($scope.filteredCars, predicate, $scope.reverse);
    };

    $http.get('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId + '/availableCars/').
            success(function (data, status, headers, config) {
                init();
                $scope.cars = data;
                $scope.filteredCars = $scope.cars = data;
                setSelectedFlag($scope.cars, $scope.carSelection);
                $scope.order();
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });

    $scope.sendCarRequests = function () {
        var fellows = [];
        
        for(var i = 0; i < $scope.carSelection.length; i++) {
            var carId = $scope.carSelection[i];
            fellows[i] = {};
            fellows[i].carId = carId;
        }
        
        var url = '../api/advertisers/' + advertiserId + '/campaigns/' + campaignId + '/cars';
        $http.post(url, fellows).
                success(function (data, status, headers, config) {
                    $scope.deleteCarSelection();
                    showModal($modal, "An die ausgewählten Fahrzeuge wurden Anfragen verschickt!\n\
                        Der Status einzelner Anfragen ist innerhalb der Detailansicht einer Kampagne\n\
                        einsehbar.", "Anfragen verschickt", "Zurück zur Kampagnen", "weitere Fahrzeuge auswählen", $scope.showCampaignDetails(), $route.reload(), angular);
                }).
                error(function (data, status, headers, config) {
                    // TODO
                });
    };

    $scope.hasImage = function (carIndex) {
        if ($scope.cars[carIndex].images === undefined)
            return false;
        if ($scope.cars[carIndex].images.length >= 1)
            return true;
        return false;
    };

    $scope.getProfileImageId = function (carIndex) {
        return $scope.cars[carIndex].images[0].id;
    };

    var filterCars = function (searchParam) {
        $scope.cars = $filter('filter')($scope.filteredCars, $scope[searchParam]);
    };

    $scope.setFilterParamValue = function (filterParam, searchParam) {
        if ($scope[searchParam] === undefined) {
            $scope[searchParam] = [];
        }
        $scope[searchParam][filterParam] = [];
        $scope[searchParam][filterParam] = $scope.filterType[filterParam];

        filterCars(searchParam);
    };

    $scope.filterReset = function (searchParam) {
        delete $scope[searchParam];
        delete $scope.filterType;
        filterCars(searchParam);
    };

});