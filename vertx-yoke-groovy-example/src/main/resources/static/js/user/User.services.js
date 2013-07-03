'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', [])
	.value('version', '0.1')
	.service('eventBusService', function($rootScope, $q) {
	    
	    var eb = null;
	    
	    var state = null;
	    
	    var safeApply = function(scopeToApply, fn) {
          var phase = scopeToApply.$root.$$phase;
          if(phase == '$apply' || phase == '$digest') {
            if(fn && (typeof(fn) === 'function')) {
              fn();
            }
          } else {
            scopeToApply.$apply(fn);
          }
        };
	    
	    this.getEventBus = function() {
	        var deferred = $q.defer();
	        
	        if(eb == null) {
    	        eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');
                            
                eb.onopen = function() {
                    console.log("opened bus");
                    state = 'opened';
                    
                    safeApply($rootScope, function() {
                        deferred.resolve(eb);
                    });
                };
            }
            
            if(state == 'opened')
               safeApply($rootScope, function() {
                        deferred.resolve(eb);
                });
	        
	        return deferred.promise;
	    };
	    
	})
	.service('loginService', function( eventBusService) {
		
		this.storeUser = function(user) {
			
			var data = JSON.stringify(user);
			
			var msg = {
				 user : data     
			};
			
			console.log("sockjs: sending request with " + msg.user);
			/* */
			
			var ebPromise = eventBusService.getEventBus();
            
            ebPromise.then(function(eb) {
                eb.send('user.signup.handler', msg, function(reply) {
    				
    				console.log("sockjs: received " + reply.answer);
    				if(reply.answer != null) {
    					var userResponse = User.createFromJSON(JSON.stringify(reply.answer) );
    					console.log("sockjs: responsed user: " + JSON.stringify(userResponse) );
    				}
    				
    			});
    		});
			
		};
			  
	})
	.service('userService', function($rootScope, $q, eventBusService) {
		
		this.findAllUser = function() {
			
			var deferred = $q.defer();
			
			var msg = {
				 action : 'findAllUser'     
			};
			
			console.log("sockjs: sending request with " + msg.action);
			/* */
			var ebPromise = eventBusService.getEventBus();
			
			ebPromise.then(function(eb) {
                eb.send('user.service.handler', msg, function(reply) {
    				console.log("sockjs: received " + reply.answer);
    				if(reply.answer != null) {
    			        var result = Array();
    			        
    			        var answerObj = JSON.parse(reply.answer);
    			        
    					for(var i in answerObj) {
    					    console.log('' + answerObj[i]);
    						result.push(User.createFromJSON( JSON.stringify(answerObj[i]) ));
    					}
    					
    					$rootScope.$apply(function() {
        					deferred.resolve(result);
                        });
    				}
    				else
    				    deferred.reject('no data found');
    			});
			});
			
            
            return deferred.promise;
		};
			  
	});
