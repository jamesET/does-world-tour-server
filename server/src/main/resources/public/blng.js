(function(){
  var app = angular.module('beerList',['ngResource']);
  var beerlist = this;
  
  //-------------------------------------------------------------//
  app.controller('BeerListController', function($scope,$resource){
	  var BeerLists = $resource('/beerlists/getMyBeerList');
	  this.beerList = {};
	  
	  
	  BeerLists.get({}, function(data) {
		 $scope.beerList = data; 
	  });
	  
  });
  
  
  
})();