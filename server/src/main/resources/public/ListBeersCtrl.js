$(document).ready(function() {
	
    $.jqx.theme = "bootstrap";
	
    // Beers Grid 
	var source =
	{
	    url: "beers/",
	    datatype: "json",
	    timeout: 1000,
	    sortable: true,
	    datafields: [
	        { name: 'name' },
	        { name: 'brewery' },
	        { name: 'description' },
	        { name: 'country' }
	    ],
	};
	
    var dataAdapter = new $.jqx.dataAdapter(source, {
        loadComplete: function (data) { },
        loadError: function (xhr, status, error) { }      
    });
    
    $("#jqxgrid").jqxGrid(
    {
        source: dataAdapter,
	    width: "100%",
	    height: "100%",
        columns: [
          { text: 'Beer', datafield: 'name' },
          { text: 'Brewery', datafield: 'brewery' },
          { text: 'Description', datafield: 'description' },
          { text: 'Country', datafield: 'country' }
        ]
    });
    
});