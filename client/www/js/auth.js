'use strict';

(function (angular) {

  function AuthService($http, $q, session, ENV, logger, exception){
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
        .then( loginSuccess )
        .catch( exception.catcher('Login failed') );

        function loginSuccess(response, status, headers, config) {
            isAuthenticated = true;
            session.setUser(response.data.userId);
            session.setAccessToken(response.data.token);
            session.setUserData(response.data.userTO);
            session.setRole(response.data.userTO.role);
        }

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
        .then(logOutComplete)
        .catch(exception.catcher('Logout failed'));

        function logOutComplete() {
            logger.log('User logged out');
        }
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
  AuthService.$inject = ['$http', '$q', 'session','ENV','logger','exception'];

  // Export
  angular
    .module('app.auth',['app.core'])
    .service('auth', AuthService);

})(angular);
