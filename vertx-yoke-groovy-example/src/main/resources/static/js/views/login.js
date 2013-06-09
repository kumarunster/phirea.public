
var eb = null;


$(document).ready(function() {

	$("#genderGrp button").click(function () {
	    $("#gender").val($(this).text());
	});
	
//	$("#signup").validate({
//		rules : {
//			fname : "required",
//			lname : "required",
//			email : {
//				required : true,
//				email : true
//			},
//			passwd : {
//				required : true,
//				minlength : 8
//			},
//			conpasswd : {
//				required : true,
//				equalTo : "#passwd"
//			},
//			gender : "required"
//		},
//
//		errorClass : "help-block"
//
//	});
	
	if(eb == null) {
		eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');
		
		eb.onopen = function() {
			console.log("opened bus");
		};
	}
	
});



var sendAjaxJson = function() {
	var user = new User();
	user.fName = "Max";
	
	var data = JSON.stringify(user);
	
//	console.log("sending ajax request with " + data);
//	
//	var request = $.ajax({
//		url : "ajaxRequest",
//		type : "POST",
//		data : data,
//		contentType: "application/json; charset=utf-8",
//		dataType : "json"
//		
//			
//	});
//	
//	request.done(function(msg, state, response) {
//		console.log("request done" + msg);
//		
//		var userResponse = User.createFromJSON(response.responseText);
//		console.log("generated user: " + userResponse);
//	});
	
	var msg = {
		 body : "ping!"     
	};
	
	
	console.log("sending " + msg.body);
	eb.send('test.handler', msg, function(reply) {
		console.log("received " + reply.answer);
	});
	  
}