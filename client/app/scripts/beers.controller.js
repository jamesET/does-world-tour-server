(function(){
  'use strict';

  angular
      .module('app.beers',[
        'beers.service'
      ])
      .controller('BeersController', BeersController );

  function BeersController ($scope,$ionicModal,BeerService) {

    $scope.allBeers = {};
    $scope.noBeers = true;
    $scope.beer = {};

    $scope.closeModal = closeModal;
    $scope.openModal = openModal;
    $scope.refresh = refresh;
    $scope.save = save;

    activate();

    function activate() {
      $scope.$on('$ionicView.enter', function(e) {
        $scope.refresh();
      });

      $ionicModal.fromTemplateUrl('templates/beerForm.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function(modal) {
        $scope.modal = modal;
      });

      $scope.$on('$destroy', function() {
        $scope.modal.remove();
      });
    }

    function refresh() {
        BeerService.getBeers()
          .then(function(response){
            if (response && response.data) {
              $scope.noBeers = false;
              $scope.allBeers = response.data.beers;
            } else {
              $scope.noBeers = true;
            }
          });
    }

    function openModal(targetBeer) {
      if (targetBeer) {
        $scope.beer = targetBeer;
      } else {
        $scope.beer = {};
      }
      $scope.modal.show();
    }

    function save() {
      if ($scope.beer.id) {
        // this is an update
        BeerService
          .update($scope.beer)
          .then(changeDone);
      } else {
          BeerService
            .add($scope.beer)
            .then(changeDone);
      }

      function changeDone() {
          $scope.modal.hide();
          $scope.refresh();
      }
    }

    function closeModal() {
        $scope.modal.hide();
    }

  }

})();
