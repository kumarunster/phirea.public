'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('LoginCtrl', function($scope, loginService) {
	  
	  $scope.createNewUser = function() {
		  var newUser = new User();
		  newUser.fName = "Kolja;)";
		  $scope.user = newUser; 
		  $scope.passwdConfirm = undefined;
		    
	  };
	  
	  $scope.storeUser = function() {
          if($scope.signupForm.$valid) {
              var jsonUser = JSON.stringify($scope.user);
              console.log("storing user: " + jsonUser);
              loginService.storeUser($scope.user);
          }
          else
            console.log("form is not valid!")

	  };
  })
  .controller('MyCtrl2', [function() {

  }]);