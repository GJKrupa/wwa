package uk.me.krupa.wwa.repository.game;

import org.springframework.data.neo4j.repository.GraphRepository;
import uk.me.krupa.wwa.entity.game.Round;

/**
 * Created by krupagj on 02/05/2014.
 */
public interface RoundRepository extends GraphRepository<Round> {
}
