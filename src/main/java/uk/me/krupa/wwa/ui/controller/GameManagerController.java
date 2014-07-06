package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.me.krupa.wwa.dto.request.CreateGameRequest;
import uk.me.krupa.wwa.dto.request.JoinGameRequest;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.service.game.GameService;
import uk.me.krupa.wwa.service.notification.NotificationService;

import java.util.List;

/**
 * Created by krupagj on 21/04/2014.
 */
@Controller("gameManagerController")
@Scope("request")
public class GameManagerController extends AbstractController {

    @Autowired
    private GameService gameService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/createGame.do")
    @ResponseBody
    public GameSummary createGame(@RequestBody CreateGameRequest request) {
        GameSummary game = gameService.createGame(getUser(), request.getName(), request.getPassword(), request.getCardSets());
        notificationService.notifyGameCreated(getUser(), game.getId(), request.getName());
        return game;
    }

    @RequestMapping("/openGames.do")
    @ResponseBody
    public List<GameSummary> getOpenGames() {
        return gameService.getOpenGames(getUser());
    }

    @RequestMapping("/getGame.do")
    @ResponseBody
    public GameSummary getGame(@RequestBody long id) {
        return gameService.getGameSummaryById(id, getUser());
    }


    @RequestMapping("/myGames.do")
    @ResponseBody
    public List<GameSummary> getMyGames() {
        return gameService.getGamesForUser(getUser());
    }

    @RequestMapping("/startGame.do")
    @ResponseBody
    public GameSummary startGameJson(@RequestBody long id) {
        gameService.startGame(id, getUser());
        notificationService.notifyGameStarted(getUser(), id);
        return gameService.getGameSummaryById(id, getUser());
    }

    @RequestMapping("/joinGame.do")
    @ResponseBody
    public GameSummary joinGame(@RequestBody JoinGameRequest request) {
        gameService.joinGame(getUser(), request.getId(), request.getPassword());
        notificationService.notifyGameJoined(getUser(), request.getId());
        return gameService.getGameSummaryById(request.getId(), getUser());
    }
}
