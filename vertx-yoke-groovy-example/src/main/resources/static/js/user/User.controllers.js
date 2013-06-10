'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('LoginCtrl', function($scope) {
	  
	  $scope.createNewUser = function() {
		  var newUser = new User();
		  newUser.fName = "Kolja;)";
		  $scope.user = newUser; 
		    
	  };
	  
	  $scope.storeUser = function() {
		  var jsonUser = JSON.stringify($scope.user);
		  console.log("storing user: " + jsonUser);
	  };
  })
  .controller('MyCtrl2', [function() {

  }]);