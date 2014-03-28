package uk.me.krupa.wwa.entity.cards;

import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.common.BaseEntity;

/**
 * Created by krupagj on 27/03/2014.
 */
@NodeEntity
public class BlackCard extends Card {

    private int playCount;

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}
