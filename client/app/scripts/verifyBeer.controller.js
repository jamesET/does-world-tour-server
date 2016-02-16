(function() {
    'use strict';

    angular
        .module('verifybeer')
        .controller('VerifyListController', VerifyListController);

    VerifyListController.$inject = ['$scope','$interval','MyBeerList','logger'];

    /* @ngInject */
    function VerifyListController($scope,$interval,MyBeerList,logger) {
        $scope.drinksToVerify = {};
        $scope.refresh = refresh;
        $scope.verifyBeer = verifyBeer;
        $scope.rejectBeer = rejectBeer;

        $scope.$on('$ionicView.enter', function(e) {
            $scope.refresh();
            $interval(refresh,30000);
        } );

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
