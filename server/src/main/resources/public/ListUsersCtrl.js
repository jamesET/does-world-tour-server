$(document).ready(function() {
	
    $.jqx.theme = "bootstrap";
	
    // Users Grid 
	var source =
	{
	    url: "users/",
	    datatype: "json",
	    timeout: 1000,
	    sortable: true,
	    datafields: [
	        { name: 'name' },
	        { name: 'email' },
	        { name: 'nickName' },
	    ],
	};
	
    var dataAdapter = new $.jqx.dataAdapter(source, {
        loadComplete: function (data) { },
        loadError: function (xhr, status, error) { }      
    });
    
    $("#users-jqxgrid").jqxGrid(
    {
        source: dataAdapter,
	    width: "100%",
	    height: "100%",
        columns: [
          { text: 'Name', datafield: 'name' },
          { text: 'Email', datafield: 'email' },
          { text: 'Nickname', datafield: 'nickName' },
        ]
    });
    
});