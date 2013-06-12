'use strict';

/* Directives */


angular.module('myApp.directives', [])
  .directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])
  .directive('phPasswordValidate', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function(viewValue) {
            	scope.pwdValidLength = (viewValue && viewValue.length >= 8 ? 'valid' : undefined);
                scope.pwdHasLetter = (viewValue && /[A-z]/.test(viewValue)) ? 'valid' : undefined;
                scope.pwdHasNumber = (viewValue && /\d/.test(viewValue)) ? 'valid' : undefined;

                if(scope.pwdValidLength && scope.pwdHasLetter && scope.pwdHasNumber) {
                    ctrl.$setValidity('pwd', true);
                    return viewValue;
                } else {
                    ctrl.$setValidity('pwd', false);                    
                    return undefined;
                }

            });
        }
    };
  })
  .directive('phPasswordConfirmValidate', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attr, ctrl) {
            ctrl.$parsers.unshift(function(viewValue) {
                scope.pwdConfirmValid = ( (viewValue != undefined &&
                                            scope.user.passwd === viewValue) ? 'valid' : undefined);

                if(scope.pwdConfirmValid) {
                    ctrl.$setValidity('pwdConfirm', true);
                    return viewValue;
                } else {
                    ctrl.$setValidity('pwdConfirm', false);                    
                    return undefined;
                }

            });
        }  
    };  
  });
