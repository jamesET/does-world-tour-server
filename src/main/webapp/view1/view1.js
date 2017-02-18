'use strict';

angular.module('myApp.view1', ['ngRoute','beers.service'])

.config(['$routeProvider', function($routeProvider,$scope) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', [ '$scope', 'BeerService', function($scope,BeerService) {

  var vm = this;
  vm.groupedBeers = [];
  vm.numBeers = 0;
  vm.isPrinting = false;
  vm.activated = false;

  activate();

  function activate() {
    vm.groupedBeers = [];
    vm.activated = false;
    BeerService.getBeers()
      .then(getBeersSuccess,getBeersFailed);
  }

  function getBeersSuccess(beers) {
      vm.groupedBeers = getGroupedBeerList(beers);
      vm.activated = true;
  }

  function getBeersFailed(reason) {
    vm.activated = true;
    console.log(reason);
  }

  // Take beerlist and group them by country for view
  function getGroupedBeerList(drinkList) {
    var countries = {};
    var countryGroups = [];

    vm.numBeers = 0;

    for (var id in drinkList) {
        var country = drinkList[id].country;
        var beer = drinkList[id];

        // Discontinued beers are filtered out
        if (beer.discontinued) {
            continue;
        }

        vm.numBeers++;

        // Find country
        var countryIndex = countries[country];

        // Add new country if needed
        if (!countryIndex && countryIndex != 0 ) {
            countryGroup = {
                country : country,
                block : 0,
                beers : [],
                show : false
            };
            countryIndex = countryGroups.push(countryGroup) - 1;
            countries[country] = countryIndex;
        }

        // Add beer to country
        var countryGroup = countryGroups[countryIndex];
        countryGroup.beers.push(drinkList[id]);
        countryGroups[countryIndex] = countryGroup;
    }

    countryGroups.sort(
      function(a,b){
        var c1 = a.country;
        var c2 = b.country;
        return c1.localeCompare(c2);
      }
    );
    return countryGroups;
  }


}]);
