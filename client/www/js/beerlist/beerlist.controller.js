(function() {
    'use strict';

    angular
        .module('app.beerlists')
        .controller('BeerListController', BeerListController);

    BeerListController.$inject = ['$scope','BeerListService','logger'];

    /* @ngInject */
    function BeerListController($scope,BeerListService,logger) {
        var vm = this;
        $scope.myBeerList = {};
        $scope.refresh = refresh;
        $scope.drinkBeer = drinkBeer;

        activate();

        function activate() {
          $scope.$on('$ionicView.enter', function(e) {
            $scope.refresh();
          });
        }

        function refresh() {
            BeerListService.getBeerList()
              .then(function(beerlist){
                $scope.myBeerList = beerlist.data;
              })
              .finally(function(){
                    $scope.$broadcast('scroll.refreshComplete');
              });
        }

        function drinkBeer(listId,beerOnListId) {
          BeerListService.drinkBeer(listId,beerOnListId)
            .then(function() {
                $scope.refresh();
          });
        }

    }
})();
