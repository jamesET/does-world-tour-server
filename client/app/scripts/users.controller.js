(function() {
    'use strict';

    angular
        .module('app.users')
        .controller('UserMaintController', UserMaintController);

    UserMaintController.$inject = ['$scope','$ionicModal',
      'exception','logger', 'UserSvc'];

    /* @ngInject */
    function UserMaintController($scope, $ionicModal, exception, logger, UserSvc) {
        var vm = this;
        $scope.refresh = refresh;
        $scope.openModal = openModal;
        $scope.closeModal = closeModal;
        $scope.updateUser = updateUser;
        $scope.users = {};
        $scope.selectedUser = {
          name : '',
          role : ''
        };

        activate();

        function activate() {
          refresh();

          $ionicModal.fromTemplateUrl('templates/userForm.html', {
              scope: $scope,
              animation: 'slide-in-up'
            })
            .then(function(modal) {
              $scope.modal = modal;
          });

          $scope.$on('$destroy', function() {
            $scope.modal.remove();
          });

          $scope.$on('$ionicView.enter', function(e) {
            $scope.refresh();
          });
        }

        function refresh() {
            UserSvc.getUsers()
              .then(function(response){
                $scope.users = response.data.users;
                $scope.errorMessage = response.data.errorMsg;
              });
        }

        function openModal(user) {
          $scope.selectedUser = {
            id : user.id,
            name : user.name,
            role : user.role
          };
          $scope.modal.show();
        }

        function closeModal() {
          $scope.modal.hide();
        }

        function updateUser() {
            var user = $scope.selectedUser;
            logger.info('updateUser', user.name, 'TODO');
            closeModal();
        }

    }
})();
