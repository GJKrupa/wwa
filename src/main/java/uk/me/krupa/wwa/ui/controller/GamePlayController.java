package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.request.GamePlayRequest;
import uk.me.krupa.wwa.dto.request.SelectWinnerRequest;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Play;
import uk.me.krupa.wwa.entity.game.Player;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.service.game.GameService;
import uk.me.krupa.wwa.service.notification.NotificationService;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by krupagj on 22/04/2014.
 */
@Controller("gamePlayController")
@Scope("session")
public class GamePlayController extends AbstractController {

    @Autowired
    private transient GameService gameService;

    @Autowired
    private NotificationService notificationService;

    private long gameId;

    public long getGameId() {
        return Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
    }

    @RequestMapping("/getGameDetail.do")
    @ResponseBody
    public GameDetail getGameDetail(@RequestParam("id") long id) {
        return gameService.getGameDetailById(id, getUser());
    }

    @RequestMapping("/playCards.do")
    @ResponseBody
    public GameDetail playCards(@RequestBody GamePlayRequest request) {
        gameService.playCards(getUser(), request.getCards(), request.getId());
        GameDetail game = gameService.getGameDetailById(request.getId(), getUser());
        if (game.getPlays().size() == game.getPlayers().size() - 1) {
            notificationService.notifyJudgingReady(getUser(), game.getId());
        }
        return game;
    }

    @RequestMapping("/selectWinner.do")
    @ResponseBody
    public GameDetail selectWinner(@RequestBody SelectWinnerRequest request) {
        gameService.chooseWinner(request.getGameId(), request.getPlayId());
        notificationService.notifyJudged(getUser(), request.getGameId());
        return gameService.getGameDetailById(request.getGameId(), getUser());
    }

}
