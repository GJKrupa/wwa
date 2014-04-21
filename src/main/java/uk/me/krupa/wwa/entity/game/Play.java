package uk.me.krupa.wwa.entity.game;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.common.BaseEntity;
import uk.me.krupa.wwa.entity.user.User;

import java.util.List;

/**
 * Created by krupagj on 21/04/2014.
 */
@NodeEntity
public class Play extends BaseEntity {

    @Fetch
    private User user;

    @Fetch
    private WhiteCard card1;

    @Fetch
    private WhiteCard card2;

    @Fetch
    private WhiteCard card3;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
