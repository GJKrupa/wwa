package uk.me.krupa.wwa.dto.request;

import java.util.List;

/**
 * Created by krupagj on 21/06/2014.
 */
public class SelectWinnerRequest {

    private long gameId;
    private long playId;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }
}
