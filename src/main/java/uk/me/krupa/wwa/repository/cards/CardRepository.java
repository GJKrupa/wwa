package uk.me.krupa.wwa.repository.cards;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;

import java.util.List;
import java.util.Set;

/**
 * Created by krupagj on 27/03/2014.
 */
@Repository
public interface CardRepository extends GraphRepository<CardSet> {

    @Query("START s=node({0}) MATCH s-[r:blackCards]->c RETURN c")
    List<BlackCard> getBlackCardsInSet(CardSet cardSet);

}
