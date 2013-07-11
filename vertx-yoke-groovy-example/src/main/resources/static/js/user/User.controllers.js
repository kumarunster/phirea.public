'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('UserCtrl', function($scope, $rootScope, loginService, userService) {
	  
	  var promiseAllUsers = userService.findAllUser();
	  promiseAllUsers.then(function(promise){
		  $scope.allUser = promise;
	  });
	  
	    
	  
	  $scope.findAllUser = function() {
		  console.log("try to find all users!");
          var result = userService.findAllUser();
		  console.log("returning: " + JSON.stringify(result));
		  result.then(function(promise) {
			  console.log("returning promise " + promise);
			  $scope.allUser = promise;
			  
			}, 
			function (promise) {
			  console.log("failure: " + promise);
			} );
	  };
	  
	  $scope.$on('updateUserList', function() {
		  var promiseAllUsers = userService.findAllUser();
		  promiseAllUsers.then(function(promise){
			  $scope.allUser = promise;
		  });  
	  });
	  
  })
  .controller('SignUpCtrl', function($scope, $rootScope, loginService, userService) {

	  $scope.user = new User();
	  $scope.passwdConfirm = undefined;
	  
	  
	  $scope.createNewUser = function() {
		  var newUser = new User();
		  $scope.user = newUser; 
		  $scope.passwdConfirm = undefined;
		    
	  };
	  
	  $scope.storeUser = function() {
          if($scope.signupForm.$valid) {
              var jsonUser = JSON.stringify($scope.user);
              console.log("storing user: " + jsonUser);
              loginService.storeUser($scope.user);
              
              $('#signupModal').modal('hide');
              
              $scope.$emit('updateUserList', 'please do it');
              
              $scope.user = new User();
        	  $scope.passwdConfirm = undefined;
          }
          else
            console.log("form is not valid!");

	  };
  });