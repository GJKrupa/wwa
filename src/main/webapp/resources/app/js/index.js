angular.module('app', ['indexControllers', 'indexServices', 'ui.bootstrap']);

angular.module('indexControllers', [])

    .controller('GameController', ['PlayService', function(PlayService) {
        this.updatePendingText = function() {
            text = this.game.blackCard.text;
            for (n=0; n<this.playedCards.length; ++n) {
                text = text.replace("_", this.playedCards[n].text);
            }
            this.pendingText = text;

            if (this.playedCards.length == 0) {
                $('.whenCardsSelected').addClass('disabled');
            } else {
                $('.whenCardsSelected').removeClass('disabled');
            }

            if (this.playedCards.length == this.game.blackCard.playCount) {
                $('.whenAllCardsSelected').removeClass('disabled');
            } else {
                $('.whenAllCardsSelected').addClass('disabled');
            }
        };

        this.refresh = function() {
            this.playedCards = [];
            this.game = PlayService.getGame({id: this.gameId});
            this.game.$promise.then(angular.bind(this, this.updatePendingText));
        };

        this.playCard = function(id) {
            if (this.playedCards.length < this.game.blackCard.playCount) {
                for (n=0; n<this.game.hand.length; ++n) {
                    card = this.game.hand[n];
                    if (card.id == id) {
                        this.playedCards.push(card);
                        this.game.hand.splice(n, 1);
                        break;
                    }
                }
                this.updatePendingText();
            }
        };

        this.clearSelectedCards = function() {
            for (n=0; n<this.playedCards.length; ++n) {
                this.game.hand.splice(0, 0, this.playedCards[n]);
            }
            this.playedCards = [];
            this.updatePendingText();
        };

        this.playSelectedCards = function() {
            if (this.playedCards.length == this.game.blackCard.playCount) {
                cardIds = [];
                for (n=0; n<this.playedCards.length; ++n) {
                    cardIds.push(this.playedCards[n].id);
                }
                $('.whenCardsSelected').addClass('disabled');
                $('.whenAllCardsSelected').addClass('disabled');
                this.game = PlayService.playCards({id: gameId, cards: cardIds});
                this.game.$promise.then(angular.bind(this, this.updatePendingText));
            }
        };

        this.selectWinner = function(id) {
            this.game = PlayService.selectWinner({gameId: gameId, playId: id});
            this.game.$promise.then(angular.bind(this, this.updatePendingText));
        };

        this.handleNotification = function(notification) {
            if (notification.game == this.gameId) {
                this.refresh();
                toastr.info(notification.displayMessage);
            }
        };

        this.pendingText = '';
        this.playedCards = [];
        this.gameId = gameId;
        this.userId = userId;
        this.refresh();
    }])

    .controller('CreateGameController', ['GamesService', '$filter', '$window', function(GamesService, $filter, $window) {
        this.cardSets = GamesService.getCardSets();
        this.name = "";
        this.password = "";

        this.createGame = function() {
            var game = GamesService.createGame({
                name: this.name,
                password: this.password,
                cardSets: $.map(
                    $filter('filter')(this.cardSets, {selected:true}, true),
                    function(cardSet) {
                        return cardSet.id;
                    })
            });
            this.name = "";
            this.password = "";
            $.each(this.cardSets, function(index, cardSet) {
                cardSet.selected = false;
            })
            game.$promise.then(
                angular.bind(this, function(game) {
                    $window.location.href='index.xhtml';
                })
            );
        };

        this.handleNotification = function(notification) {
            if (notification.user != this.userId) {
                toastr.info(notification.displayMessage);
            }
        };
    }])

    .controller('MyGamesController', ['GamesService', function(GamesService) {
        this.myGames = GamesService.getMyGames();
        this.openGames = GamesService.getOpenGames();
        this.gameName = "";
        this.orderProp = 'name';
        this.userId = userId;
        this.gameToJoin = null;
        this.password = null;
        this.errorMessage = null;

        this.tabs = [
            {active: true},
            {active: false}
        ];

        this.refreshOpenGames = function() {
            this.openGames = GamesService.getOpenGames();
        };

        this.gameUpdated = function(id) {
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
            var theGame = GamesService.joinGame({id: id, password: this.password});
            this.password = null;
            theGame.$promise.then(angular.bind(this, function(game) {
                $('#passwordModal').modal('hide');
                if (!theGame.error) {
                    this.myGames.push(game);
                    this.tabs[0].active = true;
                    for (n=0; n<this.openGames.length; ++n) {
                        if (this.openGames[n].id == id) {
                            this.openGames.splice(n, 1);
                            break;
                        }
                    }
                } else {
                    this.errorMessage = theGame.errorMessage;
                    $('#errorModal').modal('show');
                }
            }),
            angular.bind(this, function(reason) {
                console.log(reason);
                alert(reason);
            }));

        };

        this.passwordPrompt = function(id) {
            this.gameToJoin = id;
            $('#passwordModal').modal('show');
        };

        this.gameUpdated = function(id) {
            for (n=0; n<this.myGames.length; ++n) {
                if (this.myGames[n].id == id) {
                    this.myGames[n] = GamesService.getGame(id);
                }
            }
            for (n=0; n<this.openGames.length; ++n) {
                if (this.openGames[n].id == id) {
                    this.openGames[n] = GamesService.getGame(id);
                }
            }
        };

        this.gameStarted = function(id) {
            for (n=0; n<this.myGames.length; ++n) {
                if (this.myGames[n].id == id) {
                    this.myGames[n] = GamesService.getGame(id);
                }
            }
            for (n=0; n<this.openGames.length; ++n) {
                if (this.openGames[n].id == id) {
                    this.openGames.splice(n,1);
                }
            }
        };

        this.handleNotification = function(notification) {
            if (notification.user != this.userId) {
                if (notification.notificationType == 'CREATED') {
                    this.openGames.push(GamesService.getGame(notification.game));
                } else if (notification.notificationType == 'JOINED') {
                    this.gameUpdated(notification.game);
                } else if (notification.notificationType == 'STARTED') {
                    this.gameStarted(notification.game);
                } else {
                    this.gameUpdated(notification.game);
                }
                toastr.info(notification.displayMessage);
            }
        };
    }]);


angular.module('indexServices', ['ngResource'])

    .factory('GamesService', ['$resource', function($resource) {
        return $resource('/wwa-1.0/:dest', {}, {
            getMyGames: {method: 'GET', params: {dest: 'myGames.do'}, isArray: true},
            getOpenGames: {method: 'GET', params: {dest: 'openGames.do'}, isArray: true},
            getGame: {method: 'POST', params: {dest: 'getGame.do'}},
            joinGame: {method: 'POST', params: {dest: 'joinGame.do'}},
            createGame: {method: 'POST', params: {dest: 'createGame.do'}},
            getCardSets: {method: 'GET', params: {dest: 'cardSets.do'}, isArray: true},
            start: {method: 'POST', params: {dest: 'startGame.do'}}
        })
    }])

    .factory('PlayService', ['$resource', function($resource) {
        return $resource('/wwa-1.0/:dest', {}, {
            getGame: {method: 'GET', params: {dest: 'getGameDetail.do'}},
            playCards: {method: 'POST', params: {dest: 'playCards.do'}},
            selectWinner: {method: 'POST', params: {dest: 'selectWinner.do'}}
        })
    }]);


