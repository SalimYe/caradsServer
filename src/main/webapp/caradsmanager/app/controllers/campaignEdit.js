app.controller('campaignEdit', function ($scope, $routeParams, $http, $location, $timeout, $translate, $modal) {

    var advertiserId = $routeParams.advertiserId;
    var campaignId = $routeParams.campaignId;
    $scope.advertiserId = advertiserId;

    var redirectToCampaignView = function () {
        $location.path('advertiser/' + advertiserId + "/campaign/" + campaignId);
    };

    var isNewCampaign = ($routeParams.campaignId === undefined);
    $scope.campaign;
    $scope.isNewCampaign = isNewCampaign;

    if (!isNewCampaign) {
        $http.get('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId).
                success(function (data, status, headers, config) {
                    $scope.campaign = data;
                }).
                error(function (data, status, headers, config) {
                    var title = 'alert.loadingError';
                    var description = 'alert.loadingErrorText';
                    var button = 'button.back';
                    var buttonFunction = function () {
                        $location.path('/home');
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);
                });
    }

    var updateCampaign = function () {
        if ($scope.campaignForm.$valid) {
            $http.put('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId, $scope.campaign).
                    success(function (data, status, headers, config) {
                        var title = 'alert.update';
                        var description = 'alert.updateText';
                        var button = 'button.next';
                        var buttonFunction = function () {
                            redirectToCampaignView();
                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }).
                    error(function (data, status, headers, config) {
                        var title = 'alert.updateError';
                        var description = 'alert.updateErrorText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    });
        }
    };

    $scope.deleteCampaign = function () {
        $http.delete('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId).
                success(function (data, status, headers, config) {
                    var title = 'alert.deleteSuccess';
                    var description = 'alert.deleteSuccessText';
                    var button = 'button.next';
                    var buttonFunction = function () {
                        redirectToCampaignView();
                    };
                    showModal($modal, description, title, button, null, buttonFunction, null, angular);
                }).
                error(function (data, status, headers, config) {
                    if (status === 406) {
                        var title = 'alert.deleteError';
                        var description = 'alert.deleteErrorConstraintText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    } else {
                        var title = 'alert.deleteError';
                        var description = 'alert.deleteErrorText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }
                });
    };

    var createCampaign = function () {
        if ($scope.campaignForm.$valid) {
            $http.post('../api/advertisers/' + advertiserId + '/campaigns/', $scope.campaign).
                    success(function (data, status, headers, config) {
                        campaignId = data.id;
                        var title = 'alert.createCampaign';
                        var description = 'alert.createCampaignText';
                        var button = 'button.next';
                        var buttonFunction = function () {
                            redirectToCampaignView();
                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    }).
                    error(function (data, status) {
                        var title = 'alert.creatFailed';
                        var description = 'alert.creatFailedText';
                        var button = 'button.back';
                        var buttonFunction = function () {

                        };
                        showModal($modal, description, title, button, null, buttonFunction, null, angular);
                    });
        }
    };

    $scope.saveCampaign = function () {
        if (isNewCampaign) {
            createCampaign();
        } else {
            updateCampaign();
        }
    };

    $scope.exitCampaign = function () {
        redirectToCampaignView();
    };

    $scope.saveImages = function () {
        $timeout(function () {
            if ($scope.image !== undefined) {
                for (var i = 0; i < $scope.image.length; i++) {
                    var fd = new FormData();
                    fd.append('file', $scope.image[i]);
                    $http.post("../api/images/", fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    })
                            .success(function (data, status) {
                                var image = {id: data.id, isTitle: true, altText: ""};
                                if ($scope.campaign.images === undefined) {
                                    $scope.campaign.images = [];
                                }
                                $scope.campaign.images.push(image);
                            })
                            .error(function (data, status) {

                            });
                }
            }
        }, 400);

    };

    $scope.deleteImage = function (imageId) {
        $scope.campaign.images = $scope.campaign.images.filter(function (obj) {
            return obj.id !== imageId;
        });
    };

    $scope.datePicker = (function () {
        var method = {};
        method.instances = [];

        method.open = function ($event, instance) {
            $event.preventDefault();
            $event.stopPropagation();

            method.instances[instance] = true;
        };

        var formats = ['MM/dd/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        method.format = formats[3];

        return method;
    }());
});