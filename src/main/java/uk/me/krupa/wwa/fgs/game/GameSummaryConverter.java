package uk.me.krupa.wwa.fgs.game;

import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.user.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by krupagj on 17/06/2014.
 */
public interface GameSummaryConverter {

    GameSummary convert(Game game, User user);
    List<GameSummary> convertAll(Collection<Game> games, User user);

}
