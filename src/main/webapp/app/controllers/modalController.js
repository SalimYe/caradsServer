startapp.controller("ModalController", function ($scope, $modalInstance, $modal, item) {

    $scope.item = item;

    $scope.actionOne = function () {
        $modalInstance.close();
    };

    $scope.actionTwo = function () {
        $modalInstance.dismiss();
    };
});