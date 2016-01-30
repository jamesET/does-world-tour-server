$(document).ready(function() {

	$.jqx.theme = "bootstrap";
	
   // Create the error message popup
   $("#messageNotification").jqxNotification({
       width: 250, position: "top-right", opacity: 0.9,
       autoOpen: false, animationOpenDelay: 400, autoClose: true, autoCloseDelay: 3000, template: "warning"
   });
   
   $("#completeBtn").jqxButton();
   
   $("#completeBtn").bind('click', function(event) {
	   // call complete for each beer selected
	   var rowindexes = $('#completeGrid').jqxGrid('getselectedrowindexes'); 
	   for (rowindex in rowindexes) {
		   var datarow = $("#completeGrid").jqxGrid('getrowdata', rowindex);
		   var beerListId = datarow.beerListId;
		   var beerOnListId = datarow.beerOnListId;
		   completeBeer(beerListId,beerOnListId);
		   $("#completeGrid").jqxGrid('unselectrow',rowindex);
	   }
	   
	   fetchCompleteListFromServer();
   });

   
   // Display Beerlist
	var completeListAdapter = null;
	fetchCompleteListFromServer();

	// Grid for beers to drink on 
	$("#completeGrid").jqxGrid({
			source: completeListAdapter,
			width: "100%",
			autoheight : true,
			editable: true,
			selectionmode: 'multiplerows',
			columns: [
			   { text: 'Name', datafield: 'userName', editable: false },
			   { text: 'Email', datafield: 'userEmail', editable: false },
			   { text: 'Ordered', datafield: 'orderedDate', editable: false },
			   { text: 'Beer', datafield: 'beerName', editable: false }
			]
	});
	
	// Returns list of beers to complete 
	function fetchCompleteListFromServer() {
		var completeList = null;
		$.ajax({
			type : 'GET',
			url : "/beerlists/getListToComplete",
			timeout: 1500,
			cache: false,
			contentType : "application/json",
			success: function(data,status,jqXHR) {
				refreshGrid(data);
			},
			error: function(data) {
				message = data.responseJSON.message;
				if (message.length) {
		    		$("#errorMsg").replaceWith(message);
		    		$("#messageNotification").jqxNotification("open");
				}
			}
		});
		return completeList; 
	}
	
	// 
	function completeBeer(listId,beerOnListId) {
		
		var url = "/beerlists/" + listId + "/beers/" + beerOnListId + "/complete"; 
		
		$.ajax({
			type : 'POST',
			url : url, 
			contentType : "application/json",
			success: function(data) {
				fetchCompleteListFromServer();
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
	function refreshGrid(completeList) {
		completeListAdapter = new getListDataAdapter(completeList);
		$("#completeGrid").jqxGrid({source:completeListAdapter});
	}
	
	function getListDataAdapter(completeList) {
		// Beers to complete
		var completeListSource = 
		{
			localdata : completeList.beers, 
			datatype: "array",
			id : "beerListId",
			sortcolumn: 'userName',
			sortdirection: 'asc',
			datafields: [
			   { name: 'userName', map: 'user>name' },
			   { name: 'userEmail', map: 'user>email' },
			   { name: 'beerListId', map: 'beerListId' },
			   { name : 'orderedDate', map: 'beer>orderedDate'},
			   { name : 'beerOnListId', map: 'beer>id'},
			   { name : 'beerName', map: 'beer>beer>name'},
			 ]
		};
		var completeListAdapter = new $.jqx.dataAdapter(completeListSource, {
			loadComplete: function (data) { },
			loadError: function (xhr, status, error) { }      
		});
		return completeListAdapter;
	}


});