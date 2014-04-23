package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Play;
import uk.me.krupa.wwa.entity.game.Player;
import uk.me.krupa.wwa.service.game.GameService;

import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class GamePlayController extends AbstractController {

    private static final String GAME_ID = GamePlayController.class.getName() + ".gameId";

    @Autowired
    private transient GameService gameService;

    private transient Game game;
    private transient DataModel<WhiteCard> hand;
    private transient DataModel<Play> plays;

    private List<WhiteCard> pending = new ArrayList<>(3);
    private ListDataModel<WhiteCard> pendingModel = new ListDataModel<>(pending);

    private long gameId;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
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

    private String replacePlaceholders(String text, Play play) {
        List<String> data = new ArrayList<>();
        data.add(play.getCard1().getText());
        if (play.getCard2() != null) {
            data.add(play.getCard2().getText());
        }
        if (play.getCard3() != null) {
            data.add(play.getCard3().getText());
        }

        for (String value: data) {
            text = text.replaceFirst("_", value);
        }
        return text;
    }

    public boolean isCanPlay() {
        return pending.size() < getGame().getCurrentRound().getBlackCard().getPlayCount();
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

    public void removeCard() {
        int index = pendingModel.getRowIndex();
        if (index >= 0 && index < pending.size()) {
            pending.remove(index);
        }
    }

    public void commitPlay() {
        if (pending.size() == getGame().getCurrentRound().getBlackCard().getPlayCount()) {
            gameService.playCards(getUser(), pending, game.getId());
        }
        pending.clear();
        game = null;
        hand = null;
        plays = null;
    }

    public List<WhiteCard> getPending() {
        return pending;
    }

    public void setPending(List<WhiteCard> pending) {
        this.pending = pending;
    }

    public ListDataModel<WhiteCard> getPendingModel() {
        return pendingModel;
    }

    public void setPendingModel(ListDataModel<WhiteCard> pendingModel) {
        this.pendingModel = pendingModel;
    }
}