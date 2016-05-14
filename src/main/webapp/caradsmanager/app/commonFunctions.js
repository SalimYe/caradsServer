/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




function showModal($modal, modalDescription, modalTitle, modalButtonOneText, modalButtonTwoText, actionOne, actionTwo, angular) {

    var opts = {
        backdrop: 'static',
        keyboard: false,
        templateUrl: 'views/modalContent.html',
        controller: 'ModalController',
        resolve: {} // empty storage
    };

    opts.resolve.item = function () {
        return angular.copy({description: modalDescription, title: modalTitle, buttonOne: modalButtonOneText, buttonTwo: modalButtonTwoText}); // pass data to Dialog
    };

    var modalInstance = $modal.open(opts);

    modalInstance.result.then(function () {
        //on close()
        actionOne();
    }, function () {
        //on dismiss()
        actionTwo();
    });
};

function getStateLabel(state) {
    switch (state) {
        case 'ASKED':
            return 'ausstehend';
        case 'REJECTED':
            return 'abgelehnt';
        case 'ACCEPTED':
            return 'angenommen';
    }
};

function getStateLabelBoostrapState(state) {
    switch (state) {
        case 'ASKED':
            return 'default';
        case 'REJECTED':
            return 'danger';
        case 'ACCEPTED':
            return 'success';
    }
};