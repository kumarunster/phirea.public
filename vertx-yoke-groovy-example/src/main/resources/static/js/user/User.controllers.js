'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('UserCtrl', function($scope, loginService, userService) {
	  
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
	  
	  $scope.findAllUser = function() {
		  console.log("try to find all users!")
          return userService.findAllUser();
	  };
  })
  .controller('MyCtrl2', [function() {

  }]);