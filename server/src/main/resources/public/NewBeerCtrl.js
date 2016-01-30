$(document).ready(function() {
	
    $.jqx.theme = "bootstrap";

    $.ajaxSetup({
    	  contentType: "application/json; charset=utf-8"
    	});
	
	// Create 'Add' Button.
    $("#addBtn").jqxButton();
    
	// Create 'Cancel' Button.
    $("#cancelBtn").jqxButton();
    
    // Handle click for 'Cancel' Button
    $("#cancelBtn").bind('click', function () {
    	window.location.href = 'ListBeers.html'; 
    });
    

	    $("form").submit(function(event) {

		name = $('input[name="name"]').val();
		brewery = $('input[name="brewery"]').val();
		description = $('input[name="description"]').val();
		country = $('input[name="country"]').val();

		var formData = {
			"name" : name,
			"brewery" : brewery,
			"description" : description,
			"country" : country
		};

		$.ajax({
			type : "POST",
			url : "beers",
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