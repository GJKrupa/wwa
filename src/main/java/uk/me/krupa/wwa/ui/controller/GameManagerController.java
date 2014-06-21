package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.service.game.GameService;

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
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/createGame.do")
    @ResponseBody
    public GameSummary createGame(@RequestBody String name) {
        GameSummary game = gameService.createGame(getUser(), name);
        simpMessagingTemplate.convertAndSend("/topic/newGames", game);
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
        simpMessagingTemplate.convertAndSend("/topic/updatedGames", id);
        return gameService.getGameSummaryById(id, getUser());
    }

    @RequestMapping("/joinGame.do")
    @ResponseBody
    public GameSummary joinGame(@RequestBody long id) {
        gameService.joinGame(getUser(), id);
        simpMessagingTemplate.convertAndSend("/topic/updatedGames", id);
        return gameService.getGameSummaryById(id, getUser());
    }
}
