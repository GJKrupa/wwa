package uk.me.krupa.wwa.repository.cards;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;

import java.util.List;

/**
 * Created by krupagj on 27/03/2014.
 */
@Repository
public interface WhiteCardRepository extends GraphRepository<WhiteCard> {

}
