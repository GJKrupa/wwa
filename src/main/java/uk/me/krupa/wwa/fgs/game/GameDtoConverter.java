package uk.me.krupa.wwa.fgs.game;

import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.user.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by krupagj on 17/06/2014.
 */
public interface GameDtoConverter {

    GameSummary toSummary(Game game, User user);
    List<GameSummary> toSummaries(Collection<Game> games, User user);
    GameDetail toDetail(Game game, User user);
}
