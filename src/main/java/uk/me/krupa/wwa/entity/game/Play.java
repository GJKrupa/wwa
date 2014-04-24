package uk.me.krupa.wwa.entity.game;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.common.BaseEntity;
import uk.me.krupa.wwa.entity.user.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by krupagj on 21/04/2014.
 */
@NodeEntity
public class Play extends BaseEntity {

    @Fetch
    private Player player;

    @Fetch
    private WhiteCard card1;

    @Fetch
    private WhiteCard card2;

    @Fetch
    private WhiteCard card3;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public WhiteCard getCard1() {
        return card1;
    }

    public void setCard1(WhiteCard card1) {
        this.card1 = card1;
    }

    public WhiteCard getCard2() {
        return card2;
    }

    public void setCard2(WhiteCard card2) {
        this.card2 = card2;
    }

    public WhiteCard getCard3() {
        return card3;
    }

    public void setCard3(WhiteCard card3) {
        this.card3 = card3;
    }

    public List<WhiteCard> getCardsAsList() {
        List<WhiteCard> cards = new LinkedList<>();
        if (card1 != null) {
            cards.add(card1);
        }
        if (card2 != null) {
            cards.add(card2);
        }
        if (card3 != null) {
            cards.add(card3);
        }
        return Collections.unmodifiableList(cards);
    }
}
