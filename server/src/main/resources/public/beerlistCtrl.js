$(document).ready(function() {

	$.jqx.theme = "bootstrap";
	
	var crossOffBtnDefaultText = "Select Next Beer From List";

   // Create the error message popup
   $("#messageNotification").jqxNotification({
       width: 250, position: "top-right", opacity: 0.9,
       autoOpen: false, animationOpenDelay: 400, autoClose: true, autoCloseDelay: 3000, template: "warning"
   });
   
   $("#crossOffBtn").jqxButton({disabled:true });
   
   $("#crossOffBtn").bind('click', function(event) {
	   var rowindex = $('#drinkGrid').jqxGrid('getselectedrowindex');
	   var datarow = $("#drinkGrid").jqxGrid('getrowdata', rowindex);
	   var beer = datarow.name;
	   var selectedBeerOnListId = datarow.id;
	   crossOffBeer(beerList.id,selectedBeerOnListId);
	   $("#drinkGrid").jqxGrid('unselectrow',rowindex);
	   $("#crossOffBtn").empty().append(crossOffBtnDefaultText);
	   
	   // Refresh drink & ordered lists
	   refreshGrids();
   });

   // Display Beerlist
	var drinkListAdapter = null; 
	var orderedListAdapter = null;
	var completedListAdapter = null;
	var beerList = new refreshGrids();


	// Grid for beers to drink on 
	$("#drinkGrid").jqxGrid({
			source: drinkListAdapter,
			width: "100%",
			autoheight : true,
			sortable: true,
			editable: true,
			selectionmode: 'singlerow',
			sorttogglestates: 2,
			columns: [
			   { text: 'Beer', datafield: 'name', editable: false },
			   { text: 'Brewery', datafield: 'brewery', editable: false },
			   { text: 'Country', datafield: 'country', editable: false },
			   { text: 'Region', datafield: 'region', editable: false },
			   { text: 'ABV', datafield: 'abv', editable: false },
			   { text: 'IBU', datafield: 'ibu', editable: false },
			   { text: 'oz', datafield: 'oz' , editable: false}
			]
	});

	// Show the beer button when a beer is selected
	$("#drinkGrid").bind('rowselect', function (event) {
		var index = event.args.rowindex;
		var datarow = $("#drinkGrid").jqxGrid('getrowdata', index);
		var beer = datarow.name;
		$("#crossOffBtn").empty().append("Click here to finish a " + beer);
		$("#crossOffBtn").jqxButton({disabled:false});
	});

	// Hide the beer button when a row is unselected
	$("#drinkGrid").bind('rowunselect', function (event) {
		var unselectedRowIndex = event.args.rowindex;
	});
	
	// Grid for beers that were ordered but not yet verified 
	$("#orderedGrid").jqxGrid({
		source: orderedListAdapter,
		width: "100%",
		autoheight: true,
		sortable: true,
		sorttogglestates: 2,
		columns: [
		   { text: 'Ordered', datafield: 'orderedDate'},
		   { text: 'Beer', datafield: 'name' },
		   { text: 'Brewery', datafield: 'brewery' },
		   { text: 'Country', datafield: 'country' },
		   { text: 'Region', datafield: 'region' },
		   { text: 'ABV', datafield: 'abv' },
		   { text: 'IBU', datafield: 'ibu' },
		   { text: 'oz', datafield: 'oz'}
		]
	});

	// Grid for beers that are completed 
	$("#completedGrid").jqxGrid({
		source: completedListAdapter,
		width: "100%",
		autoheight: true,
		sortable: true,
		sorttogglestates: 2,
		columns: [
		   { text: 'Completed', datafield: 'completedDate'},
		   { text: 'Beer', datafield: 'name' },
		   { text: 'Brewery', datafield: 'brewery' },
		   { text: 'Country', datafield: 'country' },
		   { text: 'Region', datafield: 'region' },
		   { text: 'ABV', datafield: 'abv' },
		   { text: 'IBU', datafield: 'ibu' },
		   { text: 'oz', datafield: 'oz'}
		]
	});
	
	// Returns beerlist for the current logged in user
	function fetchBeerListFromServer() {
		var beerList = null;
		$.ajax({
			type : 'GET',
			url : "/beerlists/getMyBeerList/",
			timeout: 1500,
			async: false,
			cache: false,
			contentType : "application/json",
			success: function(data) {
				beerList = data;
			},
			error: function(data) {
				message = data.responseJSON.message;
				if (message.length) {
		    		$("#errorMsg").replaceWith(message);
		    		$("#messageNotification").jqxNotification("open");
				}
			}
		});
		return beerList; 
	}
	
	// Returns beerlist for the current logged in user
	function crossOffBeer(listId,beerOnListId) {
		
		var url = "/beerlists/" + listId + "/beers/" + beerOnListId + "/crossoff"; 
		
		var beerList = null;
		$.ajax({
			type : 'POST',
			url : url, 
			async: false,
			contentType : "application/json",
			success: function(data) {
				beerList = data.beerLists[0];
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
	
	// Reloads beer list and sets up new datasources to refresh the grids
	function refreshGrids() {
		// List heading

		// Beer List
		var beerList = new fetchBeerListFromServer(); 
		drinkListAdapter = new getListDataAdapter(beerList.drinkList);
		$("#drinkGrid").jqxGrid({source:drinkListAdapter});
		orderedListAdapter = new getListDataAdapter(beerList.orderedList);
		$("#orderedGrid").jqxGrid({source:orderedListAdapter});
		completedListAdapter = new getListDataAdapter(beerList.completedList);
		$("#completedGrid").jqxGrid({source:completedListAdapter});

		var listName = beerList.user.name + " (" + beerList.user.email + ")";
		var listNumber = "List #" + beerList.listNumber + ", started on " + beerList.startDate;
		$("#listName").empty().append(listName);
		$("#listNumber").empty().append(listNumber);
		return beerList;
	}
	
	function getListDataAdapter(beersOnList) {
		// Beers On Drink List data source 
		var drinkListSource = 
		{
			localdata : beersOnList, 
			datatype: "array",
			id : "id",
			sortcolumn: 'name',
			sortdirection: 'asc',
			datafields: [
			   { name: 'id', map: 'id' },
			   { name : 'ordered', type: 'boolean'},
			   { name : 'orderedDate', type: 'string'},
			   { name : 'completed', type: 'boolean'},
			   { name : 'completedDate', type: 'string'},
			   { name : 'completedBy'},
			   { name: 'name', map: 'beer>name' },
			   { name: 'brewery', map: 'beer>brewery' },
			   { name: 'country', map: 'beer>country' },
			   { name: 'region', map: 'beer>region' },
			   { name: 'abv', map: 'beer>abv' },
			   { name: 'ibu', map: 'beer>ibu' },
			   { name: 'oz', map: 'beer>oz' },
			   { name: 'discontinued', map: 'beer>discontinue' },
			   { name: 'outOfStock', map: 'beer>outOfStock' }
			 ]
		};
		var drinkListAdapter = new $.jqx.dataAdapter(drinkListSource, {
			loadComplete: function (data) { },
			loadError: function (xhr, status, error) { }      
		});
		return drinkListAdapter;
	}


});