$(document).ready(function() {

	$.jqx.theme = "bootstrap";

	$.ajaxSetup({
		contentType : "application/json; charset=utf-8"
	});

	// Create 'Add' Button.
	$("#addBtn").jqxButton();

	// Create 'Cancel' Button.
	$("#cancelBtn").jqxButton();

	// Handle click for 'Cancel' Button
	$("#cancelBtn").bind('click', function() {
		alert('Cancel Clicked');
	});

	$("form").submit(function(event) {

		name = $('input[name="name"]').val();
		password = $('input[name="password"]').val();
		email = $('input[name="email"]').val();
		nickName = $('input[name="nickName"]').val();

		var formData = {
			"name" : name,
			"password" : password,
			"email" : email,
			"nickName" : nickName
		};

		$.ajax({
			type : "POST",
			url : "users",
			async : false,
			data : JSON.stringify(formData),
			contentType : "application/json",
			complete : function(data) {
				console.log(data);
				wait = false;
			}
		});

	});

});