(function() {
    'use strict';

    angular
        .module('services.beerlists',['config','blocks.exception'])
        .factory('BeerListService', BeerListService);

    BeerListService.$inject = ['$http','ENV','logger','exception'];

    /* @ngInject */
    function BeerListService($http,ENV,logger,exception) {
        var service = {
            getBeerList: getBeerList,
            drinkBeer: drinkBeer,
            getDrinksToVerify: getDrinksToVerify,
            verifyBeer: verifyBeer,
            rejectBeer: rejectBeer
        };

        return service;

        function getBeerList() {
            return $http.get(ENV.apiEndpoint + 'beerlists/getMyBeerList/')
            .then(getBeerListCompleted)
            .catch(exception.catcher);

            function getBeerListCompleted(data) {
                return data;
            }
        }

        function drinkBeer(listId,beerOnListId) {
          var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/crossoff';
          return $http.post(drinkUrl)
            .then(drinkBeerComplete)
            .catch(exception.catcher);

          function drinkBeerComplete(data) {
            return data;
          }
        }

        function verifyBeer(listId,beerOnListId) {
          var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/complete';
          return $http.post(drinkUrl)
            .then(verifyBeerComplete)
            .catch(exception.catcher);

          function verifyBeerComplete(data) {
            return data;
          }
        }

        function rejectBeer(listId,beerOnListId) {
          var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/reject';
          return $http.post(drinkUrl)
            .then(rejectBeerComplete)
            .catch(exception.catcher);

          function rejectBeerComplete(data) {
            return data;
          }
        }

        function getDrinksToVerify() {
          return $http.get(ENV.apiEndpoint + 'beerlists/getListToComplete/')
          .then(getDrinksToVerifyComplete)
          .catch(exception.catcher);

          function getDrinksToVerifyComplete(data) {
            return data;
          }
        }

    }
})();