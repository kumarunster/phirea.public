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
        
        var deferredListeners = new Array();
	    
	    this.getEventBus = function() {
	        var deferred = $q.defer();
	        
	        if(eb == null) {
	            state = 'initializing';
	            
    	        eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');
                            
                eb.onopen = function() {
                    console.log("opened bus");
                    state = 'opened';
                    
                    safeApply($rootScope, function() {
                        deferred.resolve(eb);
                        var defLength = deferredListeners.length;
                        for(var i=0; i<defLength; i++)
                            deferredListeners[i].resolve(eb);
                        
                        deferredListeners.splice(0, length);
                    });
                };
                
                eb.onclose = function() {
                    console.log("bus is closed!!! WHY???");
                }
            }
            
            if(state == 'initializing')
               deferredListeners.push(deferred);
            else if(state == 'opened')
               safeApply($rootScope, function() {
                        deferred.resolve(eb);
                });
            
	        
	        return deferred.promise;
	    };
	    
	})
	.service('loginService', function($rootScope, $q, eventBusService) {
		
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
		
		this.doLogin = function(email, password) {
            var deferred = $q.defer();
            
            //TODO use service to get sessionId
            var currentSessionId = $('body').attr('sessionId');
            
            var msg = {
                 "sessionId": currentSessionId,
                 "email" : email,
                 "password" : password     
            };
            
            console.log("sockjs: sending request with " + msg);
            /* */
            
            var ebPromise = eventBusService.getEventBus();
            
            ebPromise.then(function(eb) {
                eb.send('user.login.handler', msg, function(reply) {
                    var userResponse = null;
                    console.log("sockjs: received " + reply.answer);
                    if(reply.answer != null) {
                        userResponse = User.createFromJSON(JSON.stringify(reply.answer) );
                        console.log("sockjs: responsed user: " + JSON.stringify(userResponse) );
                    }
                    
                    $rootScope.$apply(function() {
                        deferred.resolve(userResponse);
                    });
                    
                });
            });
            
            return deferred.promise;
        };
        
        
        this.logoutUserFromCurrentSession = function() {
            var deferred = $q.defer();
            
            //TODO use service to get sessionId
            var currentSessionId = $('body').attr('sessionId');
            
            var msg = {
                 "sessionId": currentSessionId
            };
            
            console.log("sockjs: sending request with " + msg);
            /* */
            
            var ebPromise = eventBusService.getEventBus();
            
            ebPromise.then(function(eb) {
                eb.send('user.logout.handler', msg, function(reply) {
                    var response = null;
                    console.log("sockjs: received " + reply.answer);
                    if(reply.answer != null) {
                        response = reply.answer;
                    }
                    
                    $rootScope.$apply(function() {
                        deferred.resolve(response);
                    });
                    
                });
            });
            
            return deferred.promise;
        };
			  
	})
	.service('userService', function($rootScope, $q, eventBusService) {
		

        this.findUserForSession = function() {
            var deferred = $q.defer();
    
            //TODO use service to get sessionId
            var currentSessionId = $('body').attr('sessionId');
            
            var msg = {
                "action" : 'findUserForSession',
                "sessionId": currentSessionId
            };
            
            /* */
            var ebPromise = eventBusService.getEventBus();    
            ebPromise.then(function(eb) {
                eb.send('user.service.handler', msg, function(reply) {
                    var userResponse = null;
                    console.log("sockjs: received " + reply.answer);
                    if(reply.answer != null) {
                        userResponse = User.createFromJSON(JSON.stringify(reply.answer) );
                        console.log("sockjs: responsed user: " + JSON.stringify(userResponse) );
                    }
                    
                    $rootScope.$apply(function() {
                        deferred.resolve(userResponse);
                    });
                });
            });
    
            return deferred.promise;
        }; 
		
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
		
		
		this.findLoggedInUser = function() {
            
            var deferred = $q.defer();
            
            var msg = {
                 action : 'findLoggedInUser'     
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
			  
	})
	.service('chatService', function($rootScope, $q, eventBusService) {
        
        this._chatState = false;

        this.startChat = function() {
        
            if(this._chatState == true)
                return;
            
            //TODO use service to get sessionId
            var currentSessionId = $('body').attr('sessionId');
                        
            /* */
            var ebPromise = eventBusService.getEventBus();    
            ebPromise.then(function(eb) {
                eb.registerHandler('chat.handler', function(message) {
                    
                    console.log("sockjs: chat.handler received " + message);
                    
                    $rootScope.$broadcast('chat-message-arrived', message);
                    
                });
            });
            
            this._chatState = true;
        }; 
        
        this.sendMessage = function(user, text) {
            
            var msg = {
                "sender" : user,
                "text" : text
            };
            
            /* */
            var ebPromise = eventBusService.getEventBus();
            
            ebPromise.then(function(eb) {
                eb.publish('chat.handler', msg);
            });
            
        };
        
              
    });
