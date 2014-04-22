package uk.me.krupa.wwa.repository.game;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by krupagj on 27/03/2014.
 */
@Repository
public interface GameRepository extends GraphRepository<Game> {

    @Query("START u=node({0}) MATCH (g:Game {state: 'PENDING'}) WHERE NOT (g)-[:players]->()-[:user]->(u) RETURN DISTINCT g")
    Set<Game> listOpenGames(User user);

    @Query("START u=node({0}) MATCH (g:Game) WHERE (g)-[:players]->()-[:user]->(u) RETURN DISTINCT g")
    Set<Game> listGamesForUser(User user);

}
