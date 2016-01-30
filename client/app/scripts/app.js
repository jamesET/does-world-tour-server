// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
'use strict';
angular.module('starter', ['ionic','ionic.utils', 'starter.controllers'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider

    .state('app', {
    url: '/app',
    abstract: true,
    templateUrl: 'templates/menu.html',
    controller: 'AppCtrl'
  })

  .state('app.mybeerlist', {
      url: '/mybeerlist',
      views: {
        'menuContent': {
          templateUrl: 'templates/mybeerlist.html',
          controller: 'BeerListCtrl'
        }
      }
  })

  .state('app.account', {
      url: '/account',
      views: {
        'menuContent': {
          templateUrl: 'templates/account.html',
          controller: 'AccountCtrl'
        }
      }
  })

  .state('app.completeBeers', {
      url: '/completeBeers',
      views: {
        'menuContent': {
          templateUrl: 'templates/completeBeers.html',
          controller: 'VerifyListCtrl'
        }
      }
  });

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/app/mybeerlist');
});
