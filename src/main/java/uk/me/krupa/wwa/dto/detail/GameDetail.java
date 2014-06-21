package uk.me.krupa.wwa.dto.detail;

import uk.me.krupa.wwa.dto.summary.PlayerSummary;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by krupagj on 21/06/2014.
 */
public class GameDetail {

    private Long id;
    private String name;

    private String owner;
    private boolean myGame;

    private boolean canPlay;
    private boolean czar;

    private BlackCardDetail blackCard;
    private List<WhiteCardDetail> hand;
    private List<PlayDetail> previousPlays;
    private List<PlayDetail> plays;
    private List<PlayerSummary> players;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isMyGame() {
        return myGame;
    }

    public void setMyGame(boolean myGame) {
        this.myGame = myGame;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCzar() {
        return czar;
    }

    public void setCzar(boolean czar) {
        this.czar = czar;
    }

    public List<WhiteCardDetail> getHand() {
        if (hand == null) {
            hand = new LinkedList<>();
        }
        return hand;
    }

    public List<PlayDetail> getPlays() {
        if (plays == null) {
            plays = new LinkedList<>();
        }
        return plays;
    }

    public void setPlays(List<PlayDetail> plays) {
        this.plays = plays;
    }

    public List<PlayerSummary> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerSummary> players) {
        this.players = players;
    }

    public BlackCardDetail getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(BlackCardDetail blackCard) {
        this.blackCard = blackCard;
    }

    public List<PlayDetail> getPreviousPlays() {
        if (previousPlays == null) {
            previousPlays = new LinkedList<>();
        }
        return previousPlays;
    }
}
