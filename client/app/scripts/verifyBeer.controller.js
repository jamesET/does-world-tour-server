(function() {
    'use strict';

    angular
        .module('verifybeer')
        .controller('VerifyListController', VerifyListController);

    VerifyListController.$inject = ['$scope','$interval','MyBeerList','logger'];

    /* @ngInject */
    function VerifyListController($scope,$interval,MyBeerList,logger) {
        var vm = this;
        $scope.drinksToVerify = {};
        $scope.refresh = refresh;
        $scope.verifyBeer = verifyBeer;
        $scope.rejectBeer = rejectBeer;

        $scope.refresh();

        $scope.$on('$ionicView.enter', function(e) {
            $scope.refresh();
            vm.autoRefresh = $interval(refresh,10000);
        } );

        $scope.$on('$ionicView.leave', function(e) {
            $interval.cancel(vm.autoRefresh);
            vm.autoRefresh = null;
        });

        function refresh() {
            MyBeerList.getDrinksToVerify()
              .then(function(response){
                $scope.drinksToVerify = response.data.beers;
                logger.log('getDrinksToVerify refreshed. ');
              });
        }

        function verifyBeer(listId,beerOnListId) {
          MyBeerList.verifyBeer(listId,beerOnListId)
            .then(function() {
                $scope.refresh();
            });
        }

        function rejectBeer(listId,beerOnListId) {
          MyBeerList.rejectBeer(listId,beerOnListId)
            .then(function() {
                $scope.refresh();
            });
        }

    }
})();
