(function() {
    'use strict';

    angular
        .module('Toastr',['ionic'])
        .factory('toastr', toastr);

    function toastr($ionicLoading) {
        var vm = this;
        var service = {
            show : show
        };
        return service;

        function show(title,message) {
          $ionicLoading.show({
            template: '<h2>' + title + '</h2><br>' + message,
            noBackdrop: true,
            duration: 2000
          });
        }
    }
})();
