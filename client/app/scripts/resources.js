'use strict';

angular.module('resources',['ngResource','config','blocks.exception'])

.factory('UserSvc', ['$http','$q','ENV','exception',
  function($http,$q,ENV,exception) {
    var service = {};

    service.signup = function(newUser) {
      var signupUrl = ENV.apiEndpoint + 'users/signup/';
      var def = $q.defer();
      $http.post(signupUrl,newUser)
        .then(function(response) {
          def.resolve(response);
        });
      return def.promise;
    };

    service.getUsers = function() {
      var usersUrl = ENV.apiEndpoint + 'users/';
      var def = $q.defer();
      $http.get(usersUrl)
        .then(function(response) {
          def.resolve(response);
        });
      return def.promise;
    };

    service.update = function(user) {
        var updateUrl = ENV.apiEndpoint + 'users/';
        var def = $q.defer();
        $http.put(updateUrl,user)
          .then(function(response) {
            def.resolve(response);
          });
        return def.promise;
    };

    service.adminEdit = function(user) {
      var updateUrl = ENV.apiEndpoint + 'users/' + user.id + '/adminEdit';
      var editUser = {
          role: user.role
      };

      return $http.put(updateUrl,editUser)
        .then(adminEditComplete)
        .catch(exception.catcher)

      function adminEditComplete(data, status, headers, config) {
          return data;
      }

    }

    return service;
  }])

.factory('MyBeerList', ['$http','$q','ENV',
  function($http,$q,ENV) {
    var service = {};

    service.getBeerlist = function() {
        var def = $q.defer();
        $http.get(ENV.apiEndpoint + 'beerlists/getMyBeerList/')
        .then(function(data) {
          def.resolve(data);
        },
      function(data) {
          console.log('getMyBeerList failed');
      });
      return def.promise;
    };

    service.drinkBeer = function(listId,beerOnListId) {
      ///beerlists/" + listId + "/beers/" + beerOnListId + "/crossoff";
      var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/crossoff';
      var def = $q.defer();
      $http.post(drinkUrl)
        .then(function(data) {
          console.log('crossOff' + listId + ',' + beerOnListId);
          def.resolve(data);
        },
        function(data) {
            console.log('crossoff failed');
        });
      return def.promise;
    };

    service.verifyBeer = function(listId,beerOnListId) {
      ///beerlists/" + listId + "/beers/" + beerOnListId + "/crossoff";
      var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/complete';
      var def = $q.defer();
      $http.post(drinkUrl)
        .then(function(data) {
          console.log('verify' + listId + ',' + beerOnListId);
          def.resolve(data);
        },
        function(data) {
            console.log('verify failed');
        });
      return def.promise;
    };

    service.rejectBeer = function(listId,beerOnListId) {
      var drinkUrl = ENV.apiEndpoint + '/beerlists/' + listId + '/beers/' + beerOnListId + '/reject';
      var def = $q.defer();
      $http.post(drinkUrl)
        .then(function(data) {
          console.log('reject' + listId + ',' + beerOnListId);
          def.resolve(data);
        },
        function(data) {
            console.log('reject failed');
        });
      return def.promise;
    };

    service.getDrinksToVerify = function() {
      var def = $q.defer();
      $http.get(ENV.apiEndpoint + 'beerlists/getListToComplete/')
      .then(function(data) {
        def.resolve(data);
      },
      function(data) {
          console.log('getMyBeerList failed');
      });
      return def.promise;
    };

    return service;
}])

.factory('sessionInjector',
  ['session','$rootScope','$q','AUTH_EVENTS',
  function(session,$rootScope,$q,AUTH_EVENTS) {
    var sessionInjector = {
        request: function(config) {
            config.headers['X-SECURITY-TOKEN'] = session.getAccessToken();
            return config;
        },
        responseError: function(response) {
            $rootScope.$broadcast({
                401: AUTH_EVENTS.notAuthenticated,
                403: AUTH_EVENTS.notAuthorized
            }[response.status],response);
            return $q.reject(response);
        }
    };
    return sessionInjector;
}])

.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('sessionInjector');
    $httpProvider.defaults.timeout = 5000;
}]);
