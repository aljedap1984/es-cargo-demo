angular.module('springPortfolio.controllers', ['ui.bootstrap'])
    .constant("buy", "Buy")
    .constant("sell", "Sell")
    .constant("save", "Save")
    .controller('PortfolioController',
    ['$scope', '$http', '$uibModal', 'TradeService',
    function ($scope, $http, $uibModal, tradeService) {
        $scope.notifications = [];
        $scope.positions = [];

        $scope.selectedCustomer = null;
        $scope.customers = [];
        $scope.cargos = [];

        $http({
            method: 'GET',
            url: '/api/cargo',
            data: { }
        }).success(function (result) {
            console.log(result);
            $scope.positions = result;
        });

        var pushNotification = function(message) {
            $scope.notifications.unshift(message);
        };

        var validateCargo = function(cargo) {
            if (isNaN(cargo.customerId) || (cargo.customerId < 1)) {
                $scope.notifications.push("New Cargo Error: Select a customer");
                return false;
            }
            return true;
        }

        $scope.openTradeModal = function (action) {
            var modalInstance = $uibModal.open({
                templateUrl: 'tradeModal.html',
                controller: 'TradeModalController',
                size: "sm",
                resolve: {
                    action: action
                }
            });
            modalInstance.result.then(function (result) {
                var cargo = {
                    "customerId" : result.customerId,
                    "amount" : result.amount
                };
                if(validateCargo(cargo)) {
                    $http({
                        method: 'POST',
                        url: '/api/cargo',
                        data: cargo
                    }).success(function (result) {
                        console.log(result);
                    });
                }
            });
        };

        $scope.logout = function() {
            tradeService.disconnect();
        };

        tradeService.connect("/events")
            .then(function () {
                    return tradeService.listenEvents().then(null, null, function (event) {
                        console.log("TODO EVENT: ");
                        console.log(event);
                        if(event.eventType == "CargoInvoiceGeneratedEvent") {
                            //muestro en notificacion
                            pushNotification("Generated invoice Id: "+event.entityId);
                            //mostrar factura
                            var j = eval('(' + event.eventData + ')');
                            var invoce = "Invoce \n CustomerId:"+ j.customerId;
                            invoce += "\n Amount: "+ j.amount;
                            invoce += "\n Date: "+ j.date;
                            alert(invoce);
                            //refrescar la tabla de cargo
                            $http({
                                method: 'GET',
                                url: '/api/cargo',
                                data: { }
                            }).success(function (result) {
                                console.log(result);
                                $scope.positions = result;
                            });
                        } else {
                            alert("Error Insufficient founds");
                        }
                    });
                },
                function (error) {
                    pushNotification(error);
                });

    }])
    .controller('TradeModalController',
            ["$scope", "$uibModalInstance", "TradeService", "action","$http",
            function ($scope, $uibModalInstance, tradeService, action,$http) {

        $http({
            method: 'GET',
            url: '/api/customer',
            data: { }
        }).success(function (result) {
            $scope.customers = result;
        });
        $scope.action = action;
        $scope.amount = 0;
        $scope.selectedCustomer = 0;
        $scope.trade = function () {
            $uibModalInstance.close({action: action, customerId:$scope.selectedCustomer,amount: $scope.amount});
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);