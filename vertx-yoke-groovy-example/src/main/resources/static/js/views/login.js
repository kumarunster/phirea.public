
//var eb = null;


$(function() {

//	$("#genderGrp button").click(function () {
//		console.log('gender click');
//		var $checkBtn = $(this);
//		console.log('datavalue: ' + $checkBtn.attr('datavalue'));
//	    $("#gender").val($checkBtn.attr('datavalue'));
//	});
	
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
	
//	if(eb == null) {
//		eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');
//		
//		eb.onopen = function() {
//			console.log("opened bus");
//		};
//	}
	
});



//var sendAjaxJson = function() {
//	var user = new User();
//	user.fName = "Max";
//	
//	var data = JSON.stringify(user);
//	
//	console.log("ajax: sending request with " + data);
//	
//	var request = $.ajax({
//		url : "ajaxRequest",
//		type : "POST",
//		data : data,
//		contentType: "application/json; charset=utf-8",
//		dataType : "json"	
//	});
//	
//	request.done(function(msg, state, response) {
//		console.log("ajax: request done" + msg);
//		var userResponse = User.createFromJSON(response.responseText);
//		console.log("ajax: responsed user: " + JSON.stringify(userResponse));
//	});
//	  
//}
//
//
//
//var sendSockJsJson = function() {
//	var user = new User();
//	user.fName = "Max";
//	
//	var data = JSON.stringify(user);
//	
//	var msg = {
//		 user : data     
//	};
//	
//	console.log("sockjs: sending request with " + msg.user);
//	eb.send('user.signup.handler', msg, function(reply) {
//		
//		console.log("sockjs: received " + reply.answer);
//		if(reply.answer != null) {
//			var userResponse = User.createFromJSON(JSON.stringify(reply.answer) );
//			console.log("sockjs: responsed user: " + JSON.stringify(userResponse) );
//		}
//		
//	});
//	  
//}