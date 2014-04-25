package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.service.game.GameService;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * Created by krupagj on 21/04/2014.
 */
@Controller("gameManagerController")
@Scope("request")
public class GameManagerController extends AbstractController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePlayController gamePlayController;

    private DataModel<Game> openGames;
    private DataModel<Game> myGames;

    private String name;

    public DataModel<Game> getOpenGames() {
        if (openGames == null) {
            openGames = new ListDataModel<>(gameService.getOpenGames(getUser()));
        }
        return openGames;
    }

    public DataModel<Game> getMyGames() {
        if (myGames == null) {
            myGames = new ListDataModel<>(gameService.getGamesForUser(getUser()));
        }
        return myGames;
    }

    public void createGame() {
        gameService.createGame(getUser(), name);
        myGames = null;
    }

    public void joinGame() {
        gameService.joinGame(getUser(), openGames.getRowData().getId());
        openGames = null;
        myGames = null;
    }

    public String startGame() {
        gameService.startGame(myGames.getRowData().getId());
        gamePlayController.setGameId(myGames.getRowData().getId());
        return "/play";
    }

    public String openGame() {
        Game game = myGames.getRowData();

        if (game.getCurrentRound() == null) {
            return "/viewPending";
        } else {
            gamePlayController.setGameId(myGames.getRowData().getId());
            return "/play";
        }
    }

    public boolean isMyGame() {
        return myGames.getRowData().getOwner().getUser().equals(getUser());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
