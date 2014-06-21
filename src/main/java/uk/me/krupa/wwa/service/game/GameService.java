package uk.me.krupa.wwa.service.game;

import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Play;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;

import java.util.List;

/**
 * Created by krupagj on 21/04/2014.
 */
public interface GameService {

    public Round createNewRound(long gameId);

    List<GameSummary> getOpenGames(User user);

    GameSummary createGame(User user, String name);

    void joinGame(User user, Long id);

    Game getGameById(long id);

    void playCards(User user, List<Long> card, Long gameId);

    void startGame(Long id);

    GameSummary startGame(Long id, User user);

    void chooseWinner(Long game, Long winningPlay);

    List<GameSummary> getGamesForUser(User user);

    GameSummary getGameSummaryById(long id, User user);

    GameDetail getGameDetailById(long id, User user);
}
