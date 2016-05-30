'use strict';

var startapp = angular.module('startapp', ['ngRoute', 'ngTable', 'pascalprecht.translate', 'ui.bootstrap', 'ngAnimate']);

startapp.config(function ($routeProvider) {

    $routeProvider
            .when('/registration/', {
                controller: 'registration',
                templateUrl: 'views/registration.html'
            })
            .when('/registration/driver/', {
                controller: 'driver',
                templateUrl: 'views/driver.html'
            })
            .when('/registration/advertiser/', {
                controller: 'advertiser',
                templateUrl: 'views/advertiser.html'
            })
            .when('/home', {
                controller: 'home',
                templateUrl: 'views/home.html'
            })
            .when('/faq', {
                controller: 'faq',
                templateUrl: 'views/faq.html'
            })
            .when('/', {
                redirectTo: '/home'
            })
            .otherwise({redirectTo: '/'});

});

startapp.config(function ($translateProvider) {

    var de = (function () {
        var json = null;
        $.ajax({
            'async': false,
            'global': false,
            'url': "lang/de.json",
            'dataType': "json",
            'success': function (data) {
                json = data;
            }
        });
        return json;
    })();

    var en = (function () {
        var json = null;
        $.ajax({
            'async': false,
            'global': false,
            'url': "lang/en.json",
            'dataType': "json",
            'success': function (data) {
                json = data;
            }
        });
        return json;
    })();

    $translateProvider
            .translations('de', de)
            .translations('en', en)
            .registerAvailableLanguageKeys(['en', 'de'], {
                'en_US': 'en',
                'en_UK': 'en',
                'de_DE': 'de',
                'de_CH': 'de',
                'de_AT': 'de'
            })
            .determinePreferredLanguage()
            .fallbackLanguage('en');

});

// datepickerPopup fix
startapp.directive('datepickerPopup', function (){
  return {
    restrict: 'EAC',
    require: 'ngModel',
    link: function(scope, element, attr, controller) {
      //remove the default formatter from the input directive to prevent conflict
      controller.$formatters.shift();
    }
  };
});
 
startapp.run(function(datepickerConfig, datepickerPopupConfig, timepickerConfig) {  
    datepickerConfig.startingDay = 1;
    datepickerConfig.showWeeks = false;
 
    datepickerPopupConfig.currentText = 'Heute';
    datepickerPopupConfig.clearText = 'Löschen';
    datepickerPopupConfig.closeText = 'Schließen';
   
    timepickerConfig.hourStep = 1;
    timepickerConfig.minuteStep = 15;
 
    timepickerConfig.showMeridian = true;
   
});