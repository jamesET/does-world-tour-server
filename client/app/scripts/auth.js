'use strict';

(function (angular) {

  function AuthService($http, session, ENV){
    var baseUrl = ENV.apiEndpoint;
    var loginUrl = baseUrl + '/login/';
    var logoutUrl =  baseUrl + '/logout/';
    var isAuthenticated = false;

    /**
    * Check whether the user is logged in
    * @returns boolean
    */
    this.isLoggedIn = function isLoggedIn(){
      return session.getUser() !== null;
    };

    /**
    * Log in
    *
    * @param credentials
    * @returns {*|Promise}
    */
    this.logIn = function(username,password){
      session.setEmail(username);
      session.setPassword(password);
      var credentials = {
        username : username,
        password : password
      };
      return $http
        .post(loginUrl, credentials)
        .then(function(response){
          var data = response.data;
          isAuthenticated = true;
          session.setUser(data.userId);
          session.setAccessToken(data.token);
          session.setUserData(data.userTO);
          session.setRole(data.userTO.role);
        });
    };

    /**
    * Log out
    *
    * @returns {*|Promise}
    */
    this.logOut = function(){
      isAuthenticated = false;
      session.destroy();
      return $http
        .post(logoutUrl)
        .then(function(response){
        });
    };

    this.isAuthenticated = function() {
      return isAuthenticated;
    };

    this.isAdmin = function() {
      if (!isAuthenticated) {
        return false;
      }
        if (session.getRole() === 'ADMIN') {
          return true;
        } else {
          return false;
        }
    };

  }

  // Inject dependencies
  AuthService.$inject = ['$http', 'session','ENV'];

  // Export
  angular
    .module('app.auth',['config'])
    .service('auth', AuthService);

})(angular);
