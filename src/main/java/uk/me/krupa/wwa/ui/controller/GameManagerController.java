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

    private DataModel<Game> games;

    private String name;

    public DataModel<Game> getActiveGames() {
        if (games == null) {
            games = new ListDataModel<>(gameService.getOpenGames(getUser()));
        }
        return games;
    }

    public void createGame() {
        gameService.createGame(getUser(), name);
        games = null;
    }

    public void joinGame() {
        gameService.joinGame(getUser(), games.getRowData().getId());
        games = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
