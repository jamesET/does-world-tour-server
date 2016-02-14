(function(){
  'use strict';

  angular
      .module('beers.service',['config'])
      .factory('BeerService', BeerService );

      BeerService.$inject = ['$http','$q','ENV'];

      function BeerService($http,$q,ENV) {
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
            var def = $q.defer();
            $http.put(beersUrl,beer)
              .then(function(response) {
                def.resolve(response);
              });
            return def.promise;
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
