package uk.me.krupa.wwa.fgs.game;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.GameState;
import uk.me.krupa.wwa.entity.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by krupagj on 17/06/2014.
 */
@Service("gameSummaryConverter")
public class GameSummaryConverterImpl implements GameSummaryConverter {

    @Autowired
    private Mapper mapper;

    @Override
    public List<GameSummary> convertAll(Collection<Game> games, User user) {
        LinkedList<GameSummary> summaries = new LinkedList<>();
        games.stream().map(g -> convert(g, user)).forEach(summaries::add);
        return Collections.unmodifiableList(summaries);
    }

    @Override
    public GameSummary convert(Game game, User user) {
        GameSummary gameSummary = mapper.map(game, GameSummary.class);
        gameSummary.setYourTurn(isYourTurn(game, user));
        gameSummary.setCanStart(canStart(game, user));
        gameSummary.setCanPlay(canPlay(game));
        if (game.getOwner().getUser().equals(user)) {
            gameSummary.setMyGame(true);
        }
        return gameSummary;
    }

    private boolean canPlay(Game game) {
        return game.getState() == GameState.IN_PROGRESS;
    }

    private boolean canStart(Game game, User user) {
        return game.getOwner().getUser().equals(user) && game.getPlayerCount() > 2 && game.getCurrentRound() == null;
    }

    private boolean isYourTurn(Game game, User user) {
        if (game.getCurrentRound() == null) {
            return false;
        } else if (user.equals(game.getCurrentRound().getCzar().getUser())) {
            return game.getCurrentRound().getPlays().size() == game.getPlayers().size() - 1;
        } else {
            return game.getCurrentRound().playFor(user) == null;
        }
    }
}
