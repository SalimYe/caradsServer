var compareTo = function() {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {
             
            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };
 
            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
};
 
startapp.directive("compareTo", compareTo);

startapp.directive("fileread", [function () {
        return {
            scope: {
                fileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    scope.$apply(function () {
                        // scope.fileread = changeEvent.target.files[0];
                        // or all selected files:
                        scope.fileread = changeEvent.target.files;
                    });
                });
            }
        }
    }]);

startapp.directive('zip', function() {
  var INTEGER_REGEXP = /^\-?\d+$/;
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.zip = function(modelValue, viewValue) {
        if (INTEGER_REGEXP.test(viewValue)) {
          return true;
        }
        return false;
      };
    }
  };  
});

startapp.directive('phone', function() {
  var INTEGER_REGEXP = /\+?\d{1,4}?[-.\s]?\(?\d{1,3}?\)?[-.\s]?\d{1,4}[-.\s]?\d{1,4}[-.\s]?\d{1,9}/g;
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.phone = function(modelValue, viewValue) {
        if (INTEGER_REGEXP.test(viewValue)) {
          return true;
        }
        return false;
      };
    }
  };  
});

startapp.directive('titleOpt', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.titleOpt = function(modelValue, viewValue) {
        if (viewValue === 'Frau' || viewValue === 'Herr') {
          return true;
        }
        return false;
      };
    }
  };  
});

startapp.directive('color', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.color = function(modelValue, viewValue) {
        if (modelValue !== undefined || modelValue !== '') {
          return true;
        }
        return false;
      };
    }
  };  
});