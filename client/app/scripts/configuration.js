"use strict";

 angular.module('config', [])

.constant('ENV', {
  name:'development',apiEndpoint:'http://Jamess-MacBook-Air.local:8080/'
})

.constant('AUTH_EVENTS', {
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized'
})

;
