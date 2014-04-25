package uk.me.krupa.wwa.service.game;

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

    List<Game> getOpenGames(User user);

    List<Game> getGamesForUser(User user);

    void createGame(User user, String name);

    void joinGame(User user, Long id);

    Game getGameById(long id);

    void playCards(User user, List<WhiteCard> card, Long gameId);

    void startGame(Long id);

    void chooseWinner(Long id, Play winningPlay);
}
