package uk.me.krupa.wwa.dto.summary;

import java.util.List;

/**
 * Class representing a summary of a game.
 */
public class GameSummary {

    private Long id;
    private String name;
    private List<PlayerSummary> players;
    private boolean yourTurn;
    private boolean canPlay;
    private boolean canStart;
    private boolean myGame;
    private String owner;

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

    public List<PlayerSummary> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerSummary> players) {
        this.players = players;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCanStart() {
        return canStart;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
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
}
