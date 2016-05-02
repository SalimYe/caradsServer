/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

app.controller("ModalController", function ($scope, $modalInstance, $modal, item) {

    $scope.item = item;

    $scope.actionOne = function () {
        $modalInstance.close();
    };

    $scope.actionTwo = function () {
        $modalInstance.dismiss();
    };
});
