package uk.me.krupa.wwa.repository.game;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import uk.me.krupa.wwa.entity.game.Player;

/**
 * Created by krupagj on 27/03/2014.
 */
@Repository
public interface PlayerRepository extends GraphRepository<Player> {

}
