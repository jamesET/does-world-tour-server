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
  vm.groupedBeers = [];

  BeerService.getBeers()
      .then(getBeersSuccess,getBeersFailed);

  function getBeersSuccess(beers) {
      vm.beers = beers;
      vm.groupedBeers = getGroupedBeerList(beers);
  }

  function getBeersFailed(reason) {
    console.log(reason);
  }

  // Take beerlist and group them by country for view
  function getGroupedBeerList(drinkList) {
    var countries = {};
    var countryGroups = [];

    for (var id in drinkList) {
        var country = drinkList[id].country;
        var beer = drinkList[id];

        // Find country
        var countryIndex = countries[country];

        // If country has more than 30 beers make a new group
        if (countryIndex != null) {
          var blockFactor = 52;
          var existingGroup = countryGroups[countryIndex];
          if (existingGroup.beers.length >= blockFactor) {
            var extendedCountryGroup = (existingGroup.beers.length / blockFactor)+1;
            country = drinkList[id].country + "-" + extendedCountryGroup;
            countryIndex = countries[country];
          }
        }

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
