package uk.me.krupa.wwa.entity.cards;

import uk.me.krupa.wwa.entity.common.BaseEntity;

/**
 * Created by krupagj on 27/03/2014.
 */
public abstract class Card extends BaseEntity {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
