package uk.me.krupa.wwa.entity.game;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.common.BaseEntity;
import uk.me.krupa.wwa.entity.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by krupagj on 21/04/2014.
 */
@NodeEntity
public class Player extends BaseEntity {

    @Fetch
    private User user;

    @Fetch
    private Set<WhiteCard> hand;

    private int order;

    private int score = 0;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<WhiteCard> getHand() {
        if (hand == null) {
            hand = new HashSet<>();
        }
        return hand;
    }

    public void setHand(Set<WhiteCard> hand) {
        this.hand = hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
