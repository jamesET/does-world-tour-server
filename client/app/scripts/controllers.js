'use strict';

angular.module('starter.controllers', ['resources','ionic.utils'])

.controller('AppCtrl', function($scope, $timeout, $http, $state, auth, session) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  $scope.errorMessage = '';

  $scope.logOut = function() {
      auth.logOut();
      $state.go('start');
  };

})

.controller('BeersCtrl', function($scope,BeerSvc){
  console.log('Enter BeersCtrl');
  $scope.beers = {};

  $scope.$on('$ionicView.enter', function(e) {
    $scope.refresh();
  });

  $scope.refresh = function() {
      BeerSvc.getBeers()
        .then(function(response){
          $scope.beers = response.data.beers;
          $scope.errorMessage = response.errorMsg;
        });
  };

})

.controller('BeerListCtrl', function($scope,MyBeerList) {

  $scope.$on('$ionicView.enter', function(e) {
    $scope.refresh();
  });

  $scope.refresh = function() {
      MyBeerList.getBeerlist()
        .then(function(beerlist){
          $scope.myBeerList = beerlist.data;
        });
  };

  $scope.drinkBeer = function(listId,beerOnListId) {
    MyBeerList.drinkBeer(listId,beerOnListId)
      .then(function() {
          $scope.refresh();
    });
  };


})

.controller('VerifyListCtrl', function($scope,MyBeerList) {

  $scope.$on('$ionicView.enter', function(e) {
    $scope.refresh();
  });

  $scope.verifyBeer = function(listId,beerOnListId) {
    MyBeerList.verifyBeer(listId,beerOnListId)
      .then(function() {
          $scope.refresh();
      });
  };

  $scope.rejectBeer = function(listId,beerOnListId) {
    MyBeerList.rejectBeer(listId,beerOnListId)
      .then(function() {
          $scope.refresh();
      });
  };

  $scope.refresh = function() {
      MyBeerList.getDrinksToVerify()
        .then(function(response){
          $scope.drinksToVerify = response.data.beers;
        });
  };

})

.controller('StartCtrl', function($scope,$state,session) {

  // Navigate to the login form
  $scope.login = function() {
      $state.go('login');
  };

  // Navigate to the signup form
  $scope.join = function() {
      $state.go('join');
  };

  $scope.browse = function() {
    $state.go('app.beers');
  };
})

.controller('LoginCtrl', function($scope,$state,session,auth) {

  $scope.errorMessage = '';
  $scope.loginData = {};

  // Let the user go back to the start page
  $scope.back = function() {
      $state.go('start');
  };

  // Perform the login action when the user submits the login form
  $scope.login = function() {
    $scope.errorMessage = '';
    auth.logIn($scope.loginData.username,$scope.loginData.password)
      .then(function(){
        // if login succeeds forward on to the beerlist
        $scope.loginData = { };
        var token = session.getAccessToken();
        if (token !== null) {
          $state.go('app.mybeerlist');
        }
      }, function(response) {
          $scope.errorMessage = 'Failure';
          if (response.data !== null) {
              $scope.errorMessage = $scope.errorMessage + ': ' + response.data.message;
          }
      });
  };

})

.controller('JoinCtrl', function($scope,$state,session,UserSvc) {

  $scope.acctData = { };
  $scope.errorMessage = '';

  $scope.back = function() {
      $state.go('start');
  };

  $scope.join = function() {
    $scope.errorMessage = '';

    var newUser = {
        email : $scope.acctData.email,
        name : $scope.acctData.name,
        password : $scope.acctData.password,
        nickName : $scope.acctData.nickName
    };

    var password2 = $scope.acctData.password2;
    if (newUser.password !== password2) {
        $scope.errorMessage = 'passwords do not match, please re-enter password';
        return;
    }

    UserSvc.signup(newUser)
      .then(function(response){
          // success, go login for the first time
          session.setEmail(newUser.email);
          $state.go('login');
      },
      function(response){
          $scope.errorMessage = response.data.message;
      }
    );

  };

})

.controller('AccountCtrl', function($scope,session) {
  $scope.acctData = {};

  $scope.$on('$ionicView.enter', function(e) {
    var ud = session.getUserData();
    var pwd = session.getPassword();
    $scope.acctData = {
        email : ud.email,
        password: pwd,
        password2 : pwd,
        name : ud.name,
        nickName : ud.nickName,
        role : ud.role
    };
  });


  $scope.update = function() {
    console.log('Updating account!');
  };

});
