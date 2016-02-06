"use strict";

 angular.module('config', [])

.constant('ENV', {name:'development',apiEndpoint:'http://dev.yoursite.com:10000/'})

.constant('AUTH_EVENTS', {
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized'
})


;
