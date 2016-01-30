'use strict';

angular.module('starter.controllers', ['resources','ionic.utils'])

.controller('AppCtrl', function($scope, $ionicModal, $timeout, $http, auth, session) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  // Form data for the login modal
  $scope.loginData = {
      username : session.getEmail(),
      password : session.getPassword()
  };
  $scope.myBeerList = {};
  $scope.drinksToVerify = {};
  $scope.errorMessage = '';

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/login.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.modal = modal;
  });

  // Triggered in the login modal to close it
  $scope.closeLogin = function() {
    $scope.modal.hide();
  };

  // Open the login modal
  $scope.login = function() {
    $scope.modal.show();
  };

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);
    auth.logIn($scope.loginData.username,$scope.loginData.password);
    $scope.closeLogin();

  };
})

.controller('BeerListCtrl', function($scope,MyBeerList) {

  $scope.$on('$stateChangeSuccess', function(){
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
          $scope.refreshVerifyList();
      });
  };

  $scope.verifyBeer = function(listId,beerOnListId) {
    MyBeerList.verifyBeer(listId,beerOnListId)
      .then(function() {
          $scope.refresh();
          $scope.refreshVerifyList();
      });
  };

  $scope.refreshVerifyList = function() {
      MyBeerList.getDrinksToVerify()
        .then(function(response){
          $scope.drinksToVerify = response.data.beers;
        });
  };


})

.controller('VerifyListCtrl', function($scope,MyBeerList) {

  $scope.$on('$stateChangeSuccess', function(){
    $scope.refresh();
  });

  $scope.verifyBeer = function(listId,beerOnListId) {
    MyBeerList.verifyBeer(listId,beerOnListId)
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

.controller('AccountCtrl', function($scope,session) {
  var userData = session.getUserData();
  $scope.acctData = {
      name : userData.name,
      nickName : userData.nickName,
      email : session.getEmail(),
      password: session.getPassword(),
      password2 : session.getPassword(),
  };

  $scope.update = function() {
    console.log('Updating account!');
  };

});
