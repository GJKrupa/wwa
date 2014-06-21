package uk.me.krupa.wwa.dto.detail;

/**
 * Created by krupagj on 21/06/2014.
 */
public class PlayDetail {

    private long id;
    private String playedText;
    private boolean yourPlay;
    private boolean winner;
    private String playerName;
    private String playerUrl;
    private int playerScore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayedText() {
        return playedText;
    }

    public void setPlayedText(String playedText) {
        this.playedText = playedText;
    }

    public boolean isYourPlay() {
        return yourPlay;
    }

    public void setYourPlay(boolean yourPlay) {
        this.yourPlay = yourPlay;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
}
