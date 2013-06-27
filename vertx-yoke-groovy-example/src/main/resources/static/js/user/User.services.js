'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', [])
	.value('version', '0.1')
	.service('loginService', function() {
		
		this.storeUser = function(user) {
			
			var data = JSON.stringify(user);
			
			var msg = {
				 user : data     
			};
			
			console.log("sockjs: sending request with " + msg.user);
			/* */
            eb.send('user.signup.handler', msg, function(reply) {
				
				console.log("sockjs: received " + reply.answer);
				if(reply.answer != null) {
					var userResponse = User.createFromJSON(JSON.stringify(reply.answer) );
					console.log("sockjs: responsed user: " + JSON.stringify(userResponse) );
				}
				
			});
			
		};
			  
	})
	.service('userService', function() {
		
		this.findAllUser = function() {
			
			var msg = {
				 action : 'findAllUser'     
			};
			
			console.log("sockjs: sending request with " + msg.action);
			var result = Array();
			/* */
            eb.send('user.service.handler', msg, function(reply) {
				console.log("sockjs: received " + reply.answer);
				if(reply.answer != null) {
					for(var i in reply.answer) {
						result.add(User.createFromJSON( JSON.stringify(reply.answer[i]) ));
					}
				}	
			});
            
            return result;
		};
			  
	});
