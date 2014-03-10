'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
                         'ngRoute', 
                         'ngAnimate',
                         'angularFileUpload',
                         'myApp.filters', 
                         'myApp.services', 
                         'myApp.directives', 
                         'myApp.controllers'
                         ]).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login-partial.html', {templateUrl: 'login-partial.html', controller: 'LoginCtrl'});
    
    $routeProvider.otherwise({redirectTo: '/index'});
  }]);
