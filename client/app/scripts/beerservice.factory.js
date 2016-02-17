(function(){
  'use strict';

  angular
      .module('beers.service',['config','blocks.exception'])
      .factory('BeerService', BeerService );

      BeerService.$inject = ['$http','$q','ENV','logger','exception'];

      function BeerService($http,$q,ENV,logger,exception) {
        var service = {
          getBeers : getBeers,
          update : update,
          add : add
        };
        return service;

        function getBeers() {
          var beersUrl = ENV.apiEndpoint + 'beers/browse';
          var def = $q.defer();
          $http.get(beersUrl)
            .then(function(response) {
              def.resolve(response);
            });
          return def.promise;
        }

        function update(beer) {
            var beersUrl = ENV.apiEndpoint + 'beers/';
            return $http.put(beersUrl,beer)
              .then(updateBeerComplete)
              .catch(exception.catcher('Update Beer Failed'));

            function updateBeerComplete(data, status, headers, config) {
              return data;
            }
        }


        function add(beer) {
            var beersUrl = ENV.apiEndpoint + 'beers/';
            var def = $q.defer();
            $http.post(beersUrl,beer)
              .then(function(response) {
                def.resolve(response);
              });
            return def.promise;
        }

    }

})();
