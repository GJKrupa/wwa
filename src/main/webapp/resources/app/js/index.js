angular.module('app', ['indexControllers', 'indexServices']);

angular.module('indexControllers', [])

    .controller('MyGamesController', ['$scope', 'GamesService', function($scope, GamesService) {
        this.myGames = GamesService.getMyGames();
        this.openGames = GamesService.getOpenGames();
        this.gameName = "";
        this.orderProp = 'name';

        this.refreshOpenGames = function() {
            this.openGames = GamesService.getOpenGames();
        };

        this.refreshGame = function(id) {
            for (n=0; n<this.myGames.length; ++n) {
                if (this.myGames[n].id == id) {
                    this.myGames[n] = GamesService.getGame(id);
                    break;
                }
            }

            for (n=0; n<this.openGames.length; ++n) {
                if (this.openGames[n].id == id) {
                    this.openGames[n] = GamesService.getGame(id);
                    break;
                }
            }
        }

        this.startGame = function(id) {
            for (n=0; n<this.myGames.length; ++n) {
                if (this.myGames[n].id == id) {
                    this.myGames[n] = GamesService.start(id);
                    break;
                }
            }
        };

        this.joinGame = function(id) {
            this.myGames.push(GamesService.joinGame(id));
            for (n=0; n<this.openGames.length; ++n) {
                if (this.openGames[n].id == id) {
                    this.openGames.splice(n, 1);
                    break;
                }
            }
        };

        this.createGame = function(name) {
            this.myGames.push(GamesService.createGame(this.gameName));
            this.gameName = "";
        }
    }]);


angular.module('indexServices', ['ngResource'])

    .factory('GamesService', ['$resource', function($resource) {
        return $resource('/wwa-1.0/:dest', {}, {
            getMyGames: {method: 'GET', params: {dest: 'myGames.do'}, isArray: true},
            getOpenGames: {method: 'GET', params: {dest: 'openGames.do'}, isArray: true},
            getGame: {method: 'POST', params: {dest: 'getGame.do'}},
            joinGame: {method: 'POST', params: {dest: 'joinGame.do'}},
            createGame: {method: 'POST', params: {dest: 'createGame.do'}},
            start: {method: 'POST', params: {dest: 'startGame.do'}}
        })
    }]);

