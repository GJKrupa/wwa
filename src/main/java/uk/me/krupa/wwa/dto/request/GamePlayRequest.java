package uk.me.krupa.wwa.dto.request;

import java.util.List;

/**
 * Created by krupagj on 21/06/2014.
 */
public class GamePlayRequest {

    private long id;
    private List<Long> cards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getCards() {
        return cards;
    }

    public void setCards(List<Long> cards) {
        this.cards = cards;
    }
}
