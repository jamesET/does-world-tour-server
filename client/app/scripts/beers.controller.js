(function(){
  'use strict';

  angular
      .module('beers',[
        'beers.service'
      ])
      .controller('BeersController', BeersController );

  function BeersController ($scope,$ionicModal,BeerService) {

    $scope.beers = {};
    $scope.beer = {};

    $scope.$on('$ionicView.enter', function(e) {
      $scope.refresh();
    });

    $scope.refresh = function() {
        BeerService.getBeers()
          .then(function(response){
            $scope.beers = response.data.beers;
            $scope.errorMessage = response.errorMsg;
          });
    };

  $ionicModal.fromTemplateUrl('templates/beerForm.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
  });

  $scope.add = function() {
    $scope.beer = {};
    $scope.action = 'add';
    $scope.modal.show();
  };

  $scope.update = function(beer) {
    $scope.beer = beer;
    $scope.action = 'update';
    $scope.modal.show();
  };

  $scope.closeModal = function(action) {
    if (action === 'ok') {
        if ($scope.action === 'update') {
          BeerService.update($scope.beer)
            .then(function(response){
                $scope.refresh();
                $scope.errorMessage = response.errorMsg;
            });
        } else {
          BeerService.add($scope.beer)
            .then(function(response){
                $scope.refresh();
                $scope.errorMessage = response.errorMsg;
            });
        }
    }
    $scope.modal.hide();
    $scope.action = null;
  };

  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
}

})();
