app.controller('carSearch', function ($scope, $cookieStore, $routeParams, $http, $location, $modal, $document, $timeout, $window, $translate, $filter) {

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
        for(var i = 0; i < cars.length; i++) {
            var car = cars[i];
            var index = carSelection.indexOf(car.id);
            if(index > -1) {
                car.isSelected = true;
            } else {
                car.isSelected = false;
            }
            
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
    };

    var init = function () {
        $scope.sortOptions = [{'label': 'Marke', 'value': 'brand'},
            {'label': 'Modell', 'value': 'model'},
            {'label': 'Farbe', 'value': 'color'}];
        $scope.sequenceOptions = [{label: 'aufsteigend', value: false},
            {label: 'absteigend', value: true}];
        $scope.reverse = false;
        $scope.predicate = 'brand';
        $scope.driverId = driverId;
        var fakeCampaignId = '894hf3p94hf39phf48';
        $scope.campaignId = fakeCampaignId;
        $scope.carSelection = getCarSelectionCookie($scope.campaignId);
    };

    var orderBy = $filter('orderBy');
    var driverId = $routeParams.driverId;
    if (driverId === undefined) {
        $location.path('/');
    }

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

    $http.get('../api/drivers/' + driverId + '/cars').
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