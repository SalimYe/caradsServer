app.controller('campaignEdit', function ($scope, $routeParams, $http, $location, $timeout, $translate) {

    var advertiserId = $routeParams.advertiserId;
    var campaignId = $routeParams.campaignId;
    $scope.advertiserId = advertiserId;

    var alert = function (title, content, level) {
        $scope.alert = [];
        $scope.alert.title = title;
        $scope.alert.content = content;
        $scope.alert.level = level;
    };

    var redirectToCampaignView = function () {
        $location.path('advertiser/' + advertiserId + "/campaign/" + campaignId);
    };

    $scope.deleteAlert = function () {
        delete $scope.alert;
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
                    redirectToCampaignView();
                });
    }

    var updateCampaign = function () {
        if ($scope.campaignForm.$valid) {
        $http.put('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId, $scope.campaign).
                success(function (data, status, headers, config) {
                    redirectToCampaignView();
                }).
                error(function (data, status, headers, config) {
                    alert("Update fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " +
                            "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                            "den Administrator.", "danger");
                });
            }
    };

    $scope.deleteCampaign = function () {
        $http.delete('../api/advertisers/' + advertiserId + '/campaigns/' + campaignId).
                success(function (data, status, headers, config) {
                    redirectToCampaignView();
                }).
                error(function (data, status, headers, config) {
                    alert("Löschen fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " +
                            "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                            "den Administrator.", "danger");
                });
    };

    var createCampaign = function () {
        $http.post('../api/advertisers/' + advertiserId + '/campaigns/', $scope.campaign).
                success(function (data, status, headers, config) {
                    campaignId = data.id;
                    redirectToCampaignView();
                }).
                error(function (data, status) {
                    alert("Speichern fehlgeschlagen", "Das Speichern ist leider fehlgeschlagen. " +
                            "Sollte dieser Fehler nochmals erscheinen, wenden Sie sich bitte an " +
                            "den Administrator.", "danger");
                });
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