'use strict';

/* Controllers */

angular.module('myApp.controllers', ['ngCookies', 'ngSanitize']).
  controller('UserCtrl', function($scope, $rootScope, loginService, userService) {
	  
	  $scope.currentUser = null;	  
	  
	  var promiseAllUsers = userService.findAllUser();
	  promiseAllUsers.then(function(promise){
		  $scope.allUser = promise;
	  });
	  
	  
	  var promiseLoggedInUser = userService.findUserForSession();
	  promiseLoggedInUser.then(function(promise){
		  $scope.$broadcast('loginUser', promise);
	  });
	  
	    
	  
	  $scope.findAllUser = function() {
		  console.log("try to find all users!");
          var result = userService.findAllUser();
		  console.log("returning a promise for allUsers: " + JSON.stringify(result));
		  result.then(function(promise) {
			  console.log("returning promise " + promise);
			  $scope.allUser = promise;
			  
			}, 
			function (promise) {
			  console.log("failure: " + promise);
			} );
	  };
	  
	  $scope.logout = function() {
		  var result = loginService.logoutUserFromCurrentSession();
		  result.then(function(promise) {
			  if(promise==true) { 
				  $rootScope.$broadcast('loginUser', null);
			  }
		  });
	  };
	  
	  //register event listener on "updateUserList"-Event
	  $scope.$on('updateUserList', function() {
		  var promiseAllUsers = userService.findAllUser();
		  promiseAllUsers.then(function(promise){
			  $scope.allUser = promise;
		  });  
	  });
	  
	  
	  //register event listener on "updateUserList"-Event
	  $scope.$on('loginUser', function(event, user) {
		  $scope.currentUser = user;
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
              
              //fires asynchronously an "updateUserList"-event
              $scope.$emit('updateUserList');
              
              $scope.user = new User();
        	  $scope.passwdConfirm = undefined;
          }
          else
            console.log("form is not valid!");

	  };
  })
  .controller('LoginCtrl', function($scope, $rootScope, $cookieStore, loginService, userService) {

	  $scope.email = undefined;
	  $scope.password = undefined;
	  
	  
	  $scope.login = function() {
          console.log("login user, session cookie " + $cookieStore.get('yoke.sess'));
          
          var userPromise = loginService.doLogin($scope.email, $scope.password);
          
          userPromise.then(function(user){
        	  if(user) {
        		  $('#loginModal').modal('hide');
        		  //fires asynchronously events
        		  $rootScope.$broadcast('loginUser', user);
        		  $rootScope.$broadcast('buddy-logged-in');
        		  
        		  $scope.email = undefined;
        		  $scope.password = undefined;
        	  }
        	  else {
        		  $scope.loginMsg = 'user cannot login';
        	  }
        	  
          });
	  };
  })
  .controller('ChatCtrl', function($scope, $rootScope, $cookieStore, chatService, userService) {

	  $scope.messages = new Array();
	  
	  $scope.$on('loginUser', function(event, user) {
		  if(user) {
			  $scope.sender = user;
			  chatService.startChat();
			  chatService.notificateBuddiesLoggedIn();
		  }
		  else {
			  chatService.notificateBuddiesLoggedIn();
			  chatService.stopChat();
			  $scope.loggedInUsers = new Array();
			  $scope.messages = new Array();
		  }
		  
	  });
	  
	  $scope.$on('buddy-logged-in', function(event) {
		  var promiseAllUsers = userService.findLoggedInUser();
		  promiseAllUsers.then(function(promise){
			  $scope.loggedInUsers = promise;
		  });
	  });
	  
	  $scope.$on('chat-message-arrived', function(event, message) {
		  $scope.$apply(function(){
			  $scope.messages.push(message);			  
		  });
	  });
	  
	  $scope.sendMessage = function(event) {
		  var send = false;
		  
		  if(event){
			  if(event.ctrlKey == true && event.keyCode == 13) {
				  send = true;
			  }
		  }
		  else {
			  send = true;
		  }
		  
		  if(send) {
			  chatService.sendMessage($scope.sender, $scope.message);
			  $scope.message = null;
		  }
	  };
	  
	  $scope.logEvent = function(event) {
		  console.log(event);
	  };
	  
  });