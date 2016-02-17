(function(){
  'use strict';

  angular
      .module('beers.service',['config','blocks.logger'])
      .factory('BeerService', BeerService );

      BeerService.$inject = ['$http','$q','ENV','logger'];

      function BeerService($http,$q,ENV,logger) {
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
              .catch(updateBeerFailed);

            function updateBeerComplete(data, status, headers, config) {
              return data;
            }

            function updateBeerFailed(e) {
              var newMessage = 'XHR Failed for getCustomer ';
              if (e.data && e.data.status) {
                  newMessage = newMessage + e.data.status;
              }
              if (e.data && e.data.message) {
                newMessage = newMessage + '\n' + e.data.message;
              }
              e.data.description = newMessage;
              logger.error(newMessage);
              return $q.reject(e);
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
