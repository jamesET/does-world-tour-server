  'use strict';
(function (angular) {

  function sessionService($log, $localStorage){

    // Instantiate data when service is loaded
    this._user = $localStorage.get('session.user',0);
    this._email = $localStorage.get('session.email','');
    this._password = $localStorage.get('session.password','');
    this._accessToken = $localStorage.get('session.accessToken','');
    this._role = $localStorage.get('session.role','');
    this._userData = $localStorage.getObject('session.userData');

    this.getUser = function(){
      return this._user;
    };

    this.setUser = function(user){
      this._user = user;
      $localStorage.set('session.user', user);
      return this;
    };

    this.getRole = function(){
      return this._role;
    };

    this.setRole = function(role){
      this._role = role;
      $localStorage.set('session.role', role);
      return this;
    };

    this.getEmail = function(){
        return this._email;
    };

    this.setEmail = function(email){
      this._email = email;
      $localStorage.set('session.email', email);
      return this;
    };

    this.getPassword = function(){
        return this._password;
    };

    this.setPassword = function(password){
      this._password = password;
      $localStorage.set('session.password', password);
      return this;
    };

    this.getAccessToken = function(){
      return this._accessToken;
    };

    this.setAccessToken = function(token){
      this._accessToken = token;
      $localStorage.set('session.accessToken', token);
      return this;
    };

    this.getUserData = function(){
      return this._userData;
    };

    this.setUserData = function(userData){
      this._userData = userData;
      $localStorage.setObject('session.userData', userData);
      return this;
    };


    /**
     * Destroy session
     */
    this.destroy = function destroy(){
      this.setUser(null);
      this.setPassword(null);
      this.setAccessToken(null);
      this.setUserData(null);
    };

  }

  // Inject dependencies
  sessionService.$inject = ['$log', '$localStorage'];

  // Export
  angular
    .module('starter')
    .service('session', sessionService);

})(angular);
