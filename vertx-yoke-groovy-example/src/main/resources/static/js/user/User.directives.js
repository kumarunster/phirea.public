'use strict';

/* Directives */


angular.module('myApp.directives', [])
  .directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])
  .directive('genderToggleValue', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function($scope, element, attr, ctrl) {
            element.bind('click', function() {
                eval('$scope.'+attr.ngModel + '=' + attr.genderToggleValue);
            });
        }
    };
  })
  .directive('scrollGlue', function(){
        /**
         * Found on https://github.com/Luegg/angularjs-scroll-glue/blob/master/src/scrollglue.js
         */
        return {
            priority: 1,
            require: ['?ngModel'],
            restrict: 'A',
            link: function(scope, $el, attrs, ctrls){
                var el = $el[0],
                    ngModel = ctrls[0] || fakeNgModel(true);
    
                function scrollToBottom(){
                    el.scrollTop = el.scrollHeight;
                }
    
                function shouldActivateAutoScroll(){
                    return el.scrollTop + el.clientHeight == el.scrollHeight;
                }
    
                scope.$watch(function(){
                    if(ngModel.$viewValue){
                        scrollToBottom();
                    }
                });
    
                $el.bind('scroll', function(){
                    scope.$apply(ngModel.$setViewValue.bind(ngModel, shouldActivateAutoScroll()));
                });
            }
        };
    });
  /*
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
  })
  */
  ;
  
angular.module('myApp.filters', ['ngSanitize'])  
    .filter('newlines', function () {
        return function(text) {
            return text.replace(/\n/g, '<br/>');
        }
    })
    .filter('noHTML', function () {
        return function(text) {
            return text
                    .replace(/&/g, '&amp;')
                    .replace(/>/g, '&gt;')
                    .replace(/</g, '&lt;');
        }
    });
