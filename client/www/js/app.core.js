(function() {
    'use strict';

    angular
        .module('app.core', [
            'ionic',
            'ionic.utils',
            'blocks.exception',
            'blocks.logger',
            'config'
        ])
        .constant('AUTH_EVENTS', {
            notAuthenticated: 'auth-not-authenticated',
            notAuthorized: 'auth-not-authorized'
        });
})();
