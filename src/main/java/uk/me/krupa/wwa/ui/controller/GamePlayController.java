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
    private SimpMessagingTemplate simpMessagingTemplate;

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
        return gameService.getGameDetailById(request.getId(), getUser());
    }

    @RequestMapping("/selectWinner.do")
    @ResponseBody
    public GameDetail selectWinner(@RequestBody SelectWinnerRequest request) {
        gameService.chooseWinner(request.getGameId(), request.getPlayId());
        return gameService.getGameDetailById(request.getGameId(), getUser());
    }


//    public DataModel<WhiteCard> getMyHand() {
//        if (hand == null) {
//            List<WhiteCard> cards = new ArrayList<>(getGame().getPlayers().stream()
//                    .filter(p -> p.getUser().equals(getUser()))
//                    .findFirst().orElse(new Player()).getHand());
//            hand = new ListDataModel<>(cards);
//        }
//        return hand;
//    }
//
//    public boolean isCzar() {
//        return getGame().getCurrentRound().getCzar().getUser().equals(getUser());
//    }
//
//    public boolean isAllPlayed() {
//        HashSet<Player> players = new HashSet<Player>();
//
//        players.addAll(
//                getGame().getPlayers().stream()
//                        .filter(p -> !p.equals(getGame().getCurrentRound().getCzar()))
//                        .collect(Collectors.toList())
//        );
//
//        game.getCurrentRound().getPlays().forEach(
//                p -> players.remove(p.getPlayer())
//        );
//
//        return players.isEmpty();
//    }
//
//    public DataModel<Play> getPlays() {
//        if (plays == null) {
//            plays = new ListDataModel<Play>(new LinkedList(getGame().getCurrentRound().getPlays()));
//        }
//        return plays;
//    }
//
//    public String getPlay() {
//        Play play = getPlays().getRowData();
//        return replacePlaceholders(getGame().getCurrentRound().getBlackCard().getText(), play);
//    }
//
//    public boolean isPreviousRoundAvailable() {
//        return getGame().getCurrentRound() != null && getGame().getCurrentRound().getPrevious() != null;
//    }
//
//    public User getPreviousRoundWinner() {
//            return getGame().getCurrentRound().getPrevious().getWinningPlay().getPlayer().getUser();
//    }
//

//

//
//    public boolean isCanClear() {
//        return !pending.isEmpty();
//    }
//
//    public boolean isAlreadyPlayed() {
//        return pending.contains(hand.getRowData());
//    }
//
//    public boolean isCanSubmit() {
//        return pending.size() == getGame().getCurrentRound().getBlackCard().getPlayCount();
//    }
//
//    public void playCard() {
//        WhiteCard card = getMyHand().getRowData();
//        if (pending.size() < getGame().getCurrentRound().getBlackCard().getPlayCount()) {
//            pending.add(card);
//        }
//    }
//
//    public void clearPending() {
//        pending.clear();
//    }
//
//    public void commitPlay() {
//        if (pending.size() == getGame().getCurrentRound().getBlackCard().getPlayCount()) {
//            gameService.playCards(getUser(), pending, game.getId());
//        }
//        System.err.println("/topic/game/" + gameId);
//        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, "PLAYED");
//        clearRoundData();
//    }
//
//    public void clearRoundData() {
//        pending.clear();
//        game = null;
//        hand = null;
//        plays = null;
//        players = null;
//    }
//
//    public boolean isPlayedMove() {
//        return getGame().getCurrentRound().hasPlayed(getUser());
//    }
//
//    public Play getMyPlay() {
//        return getGame().getCurrentRound().playFor(getUser());
//    }
//
//    public void selectWinner() {
//        Play winningPlay = plays.getRowData();
//        gameService.chooseWinner(game.getId(), winningPlay);
//        System.err.println("/topic/game/" + gameId);
//        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, "JUDGED");
//        clearRoundData();
//    }
//
//    public List<WhiteCard> getPending() {
//        return pending;
//    }
//
//    public void setPending(List<WhiteCard> pending) {
//        this.pending = pending;
//    }
//
//    public DataModel<Player> getPlayers() {
//        if (players == null) {
//            players = new ListDataModel<>(getGame().getPlayerList());
//        }
//        return players;
//    }
//
//    public String getPreviousRoundPlayText() {
//        if (players.isRowAvailable()) {
//            String pattern = getGame().getCurrentRound().getPrevious().getBlackCard().getText();
//
//            Play play = getGame().getCurrentRound().getPrevious().playFor(players.getRowData().getUser());
//            if (play == null) {
//                return null;
//            } else {
//                return replacePlaceholders(pattern, play);
//            }
//        } else {
//            return null;
//        }
//    }
//
//    public String getPlayStyle() {
//        Round previous = getGame().getCurrentRound().getPrevious();
//        Play play = previous.playFor(players.getRowData().getUser());
//        if (play == null) {
//            return "";
//        }
//        if (previous.getWinningPlay().equals(play)) {
//            return "";
//        } else {
//            return "";
//        }
//    }
}
