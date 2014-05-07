package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Play;
import uk.me.krupa.wwa.entity.game.Player;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.service.game.GameService;

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

    private transient Game game;
    private transient DataModel<WhiteCard> hand;
    private transient DataModel<Play> plays;
    private transient DataModel<Player> players;

    private List<WhiteCard> pending = new ArrayList<>(3);

    private long gameId;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
        clearRoundData();
    }

    public Game getGame() {
        if (game == null) {
            game = gameService.getGameById(getGameId());
        }
        return game;
    }


    public DataModel<WhiteCard> getMyHand() {
        if (hand == null) {
            List<WhiteCard> cards = new ArrayList<>(getGame().getPlayers().stream()
                    .filter(p -> p.getUser().equals(getUser()))
                    .findFirst().orElse(new Player()).getHand());
            hand = new ListDataModel<>(cards);
        }
        return hand;
    }

    public boolean isCzar() {
        return getGame().getCurrentRound().getCzar().getUser().equals(getUser());
    }

    public boolean isAllPlayed() {
        HashSet<Player> players = new HashSet<Player>();

        players.addAll(
                getGame().getPlayers().stream()
                        .filter(p -> !p.equals(getGame().getCurrentRound().getCzar()))
                        .collect(Collectors.toList())
        );

        game.getCurrentRound().getPlays().forEach(
                p -> players.remove(p.getPlayer())
        );

        return players.isEmpty();
    }

    public DataModel<Play> getPlays() {
        if (plays == null) {
            plays = new ListDataModel<Play>(new LinkedList(getGame().getCurrentRound().getPlays()));
        }
        return plays;
    }

    public String getPlay() {
        Play play = getPlays().getRowData();
        return replacePlaceholders(getGame().getCurrentRound().getBlackCard().getText(), play);
    }

    public boolean isPreviousRoundAvailable() {
        return getGame().getCurrentRound() != null && getGame().getCurrentRound().getPrevious() != null;
    }

    public User getPreviousRoundWinner() {
            return getGame().getCurrentRound().getPrevious().getWinningPlay().getPlayer().getUser();
    }

    public String getPendingText() {
        Play play = getGame().getCurrentRound().playFor(getUser());
        if (play == null) {
            return replacePlaceholders(getGame().getCurrentRound().getBlackCard().getText(),
                    pending.stream().map(card -> card.getText()).collect(Collectors.toList()));
        } else {
            return replacePlaceholders(getGame().getCurrentRound().getBlackCard().getText(), play);
        }
    }

    private String replacePlaceholders(String text, Play play) {
        List<String> data = new ArrayList<>();
        data.add(play.getCard1().getText());
        if (play.getCard2() != null) {
            data.add(play.getCard2().getText());
        }
        if (play.getCard3() != null) {
            data.add(play.getCard3().getText());
        }
        return replacePlaceholders(text, data);
    }

    private String replacePlaceholders(String text, List<String> data) {
        for (String value: data) {
            if (text.contains("_")) {
                int index = text.indexOf('_');
                text = text.substring(0, index) + value + text.substring(index+1);
            }
        }
        return text;
    }

    public boolean isCanPlay() {
        return pending.size() < getGame().getCurrentRound().getBlackCard().getPlayCount();
    }

    public boolean isCanClear() {
        return !pending.isEmpty();
    }

    public boolean isAlreadyPlayed() {
        return pending.contains(hand.getRowData());
    }

    public boolean isCanSubmit() {
        return pending.size() == getGame().getCurrentRound().getBlackCard().getPlayCount();
    }

    public void playCard() {
        WhiteCard card = getMyHand().getRowData();
        if (pending.size() < getGame().getCurrentRound().getBlackCard().getPlayCount()) {
            pending.add(card);
        }
    }

    public void clearPending() {
        pending.clear();
    }

    public void commitPlay() {
        if (pending.size() == getGame().getCurrentRound().getBlackCard().getPlayCount()) {
            gameService.playCards(getUser(), pending, game.getId());
        }
        System.err.println("/topic/game/" + gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, "PLAYED");
        clearRoundData();
    }

    public void clearRoundData() {
        pending.clear();
        game = null;
        hand = null;
        plays = null;
        players = null;
    }

    public boolean isPlayedMove() {
        return getGame().getCurrentRound().hasPlayed(getUser());
    }

    public Play getMyPlay() {
        return getGame().getCurrentRound().playFor(getUser());
    }

    public void selectWinner() {
        Play winningPlay = plays.getRowData();
        gameService.chooseWinner(game.getId(), winningPlay);
        System.err.println("/topic/game/" + gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, "JUDGED");
        clearRoundData();
    }

    public List<WhiteCard> getPending() {
        return pending;
    }

    public void setPending(List<WhiteCard> pending) {
        this.pending = pending;
    }

    public DataModel<Player> getPlayers() {
        if (players == null) {
            players = new ListDataModel<>(getGame().getPlayerList());
        }
        return players;
    }

    public String getPreviousRoundPlayText() {
        if (players.isRowAvailable()) {
            String pattern = getGame().getCurrentRound().getPrevious().getBlackCard().getText();

            Play play = getGame().getCurrentRound().getPrevious().playFor(players.getRowData().getUser());
            if (play == null) {
                return null;
            } else {
                return replacePlaceholders(pattern, play);
            }
        } else {
            return null;
        }
    }

    public String getPlayStyle() {
        Round previous = getGame().getCurrentRound().getPrevious();
        Play play = previous.playFor(players.getRowData().getUser());
        if (play == null) {
            return "list-group-item-disabled";
        }
        if (previous.getWinningPlay().equals(play)) {
            return "list-group-item-success";
        } else {
            return "";
        }
    }
}
