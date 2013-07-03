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

	  var user = new User();
	  user.fName = "Kolja;)";
	  var passwdConfirm = undefined;
	  
	  
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
              
              $('#loginModal').modal('hide');
              
              $scope.$emit('updateUserList', 'please do it');
          }
          else
            console.log("form is not valid!");

	  };
  });