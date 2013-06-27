
var eb = null;


$(document).ready(function() {

    /* */
	if(eb == null) {
		eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');
		
		eb.onopen = function() {
			console.log("opened bus");
		};
	}
	
});

