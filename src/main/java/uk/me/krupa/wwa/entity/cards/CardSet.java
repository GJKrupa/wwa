package uk.me.krupa.wwa.entity.cards;

import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.common.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by krupagj on 27/03/2014.
 */
@NodeEntity
public class CardSet extends BaseEntity {

    private String name;
    private Set<BlackCard> blackCards;
    private Set<WhiteCard> whiteCards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BlackCard> getBlackCards() {
        if (blackCards == null) {
            blackCards = new HashSet<>();
        }
        return blackCards;
    }

    public void setBlackCards(Set<BlackCard> blackCards) {
        this.blackCards = blackCards;
    }

    public Set<WhiteCard> getWhiteCards() {
        if (whiteCards == null) {
            whiteCards = new HashSet<>();
        }
        return whiteCards;
    }

    public void setWhiteCards(Set<WhiteCard> whiteCards) {
        this.whiteCards = whiteCards;
    }
}
