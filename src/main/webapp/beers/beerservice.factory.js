(function(){
  'use strict';

  angular
      .module('beers.service',[])
      .factory('BeerService', BeerService );

      BeerService.$inject = ['$http','$q'];

      function BeerService($http,$q) {
        var service = {
          getBeers : getBeers,
        };
        return service;

        // get all beers from the server
        function getBeers() {
          var beersUrl = 'https://www.mondoes.club/beers/browse';
          return $http.get(beersUrl)
            .then(getBeersComplete,getBeersFailed)

            function getBeersComplete(response) {
                return response.data.beers;
            }

            function getBeersFailed(reason) {
              console.log("Error getting beers");
            }
        }

    }

})();
