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
		  var jsonUser = JSON.stringify($scope.user);
		  console.log("storing user: " + jsonUser);
		  loginService.storeUser($scope.user);
	  };
  })
  .controller('MyCtrl2', [function() {

  }]);