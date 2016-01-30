$(document).ready(function() {

	$.jqx.theme = "bootstrap";

	$("#addForm").hide();

	$.ajaxSetup({
		contentType : "application/json; charset=utf-8"
	});

	// Remove existing email value if it exists
	sessionStorage.clear();

	// Create 'Login' Button.
	$("#loginBtn").jqxButton();

	// Create 'Signup' Button.
	$("#signUpBtn").jqxButton();

	// Create 'Add User' Button.
	$("#addBtn").jqxButton();

	// Create 'Cancel' Button for Add User form.
	$("#cancelBtn").jqxButton();

	// Handle click for add user button 
	$("#signUpBtn").bind('click', function() {
		event.preventDefault();
		$("#loginForm").hide();
		$("#addForm").show();
	});
	
	// Handle click for Cancel button on the Add User form
	$("#cancelBtn").bind('click',function() {
		event.preventDefault();
		$("#addForm").hide();
		$("#loginForm").show();
	});
	
   // Create the error message popup
   $("#messageNotification").jqxNotification({
       width: 250, position: "top-right", opacity: 0.9,
       autoOpen: false, animationOpenDelay: 400, autoClose: true, autoCloseDelay: 3000, template: "warning"
   });

   // Handle click to Login
	$("#loginForm").on("submit",function(event) {
		event.preventDefault();
		name = $(this).find('input[name="name"]').val();
		password = $(this).find('input[name="password"]').val();
		login(name,password);
		return false;
	});
	
	// Handle click to Add User
	$("#addForm").on("submit",function(event) {
		event.preventDefault();
		
		name = $(this).find('input[name="name"]').val();
		password = $(this).find('input[name="password"]').val();
		email = $(this).find('input[name="email"]').val();
		nickName = $(this).find('input[name="nickName"]').val();
		addUser(name,password,email,nickName);
		return false;
	});
	
	// Call server to login an existing user
	function login(email,password) {
		
		var formData = {
				"email" : email,
				"password" : password,
			};

		$.ajax({
			type : "POST",
			url : "users/login",
			async : false,
			data : JSON.stringify(formData),
			contentType : "application/json",
			success: function(data) {
				sessionStorage.setItem('email',data.email);
				window.location.href = 'beerlist.html';
			},
			error: function(data) {
				message = data.responseJSON.message;
				if (message.length) {
	   		    	$("#errorMsg").replaceWith(message);
	   		    	$("#messageNotification").jqxNotification("open");
				}
			}
		});
	}
	
	// Call server to add a new user
	function addUser(name,password,email,nickName) {
		
		var doLogin = false;
		
		var formData = {
				"name" : name,
				"password" : password,
				"email" : email,
				"nickName" : nickName
			};

		$.ajax({
			type : "POST",
			url : "users/signup/",
			async : false,
			data : JSON.stringify(formData),
			contentType : "application/json",
			success : function(data) {
				window.location.href = 'beerlist.html';
			},
			error: function(data) {
				message = data.responseJSON.message;
				if (message.length) {
    		    	$("#errorMsg").replaceWith(message);
    		    	$("#messageNotification").jqxNotification("open");
				}
			}
		});
		
	}
	

});