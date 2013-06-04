
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
});