'use strict';

angular.module('myApp.view1', ['ngRoute','beers.service'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', [ 'BeerService', function(BeerService) {

  var vm = this;
  vm.beers = { };

  BeerService.getBeers()
      .then(getBeersSuccess,getBeersFailed);

  function getBeersSuccess(beers) {
      vm.beers = beers;
  }

  function getBeersFailed(reason) {
    console.log(reason);
  }

}]);
