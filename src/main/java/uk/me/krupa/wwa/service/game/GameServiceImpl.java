package uk.me.krupa.wwa.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Player;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.repository.cards.CardRepository;
import uk.me.krupa.wwa.repository.game.GameRepository;

import java.util.*;

/**
 * Created by krupagj on 21/04/2014.
 */
@Service
public class GameServiceImpl implements GameService{

    private Random random = new Random();

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Override
    @Transactional(readOnly = false)
    public Round createNewRound(long gameId) {
        Game game = gameRepository.findOne(gameId);

        Round round = createRound(game);
        selectBlackCard(game, round);
        pickAdditionalCards(game, round);
        game.setCurrentRound(round);

        game = gameRepository.save(game);
        return game.getCurrentRound();
    }

    @Override
    public List<Game> getOpenGames(User user) {
        return gameRepository.findAll().as(List.class);
    }

    @Override
    @Transactional
    public void createGame(User user, String name) {
        Game game = new Game();
        game.setName(name);

        Player owner = game.addPlayer(user);
        game.setOwner(owner);

        CardSet cardSet = cardRepository.findAll().iterator().next();

        game.getBlackDeck().addAll(cardRepository.getBlackCardsInSet(cardSet));
        game.getWhiteDeck().addAll(cardRepository.getWhiteCardsInSet(cardSet));

        game = gameRepository.save(game);
    }

    @Override
    public void joinGame(User user, Long id) {
        Game game = gameRepository.findOne(id);
        game.addPlayer(user);
        gameRepository.save(game);
    }

    private void pickAdditionalCards(Game game, Round round) {
        int handSize;
        // Play 3, Pick 2
        if (round.getBlackCard().getPlayCount() == 3) {
            handSize = 12;
        } else {
            handSize = 10;
        }

        List<WhiteCard> whiteCards = new ArrayList<>(game.getWhiteDeck());
        Collections.shuffle(whiteCards, random);
        Iterator<WhiteCard> it = whiteCards.iterator();

        game.getPlayers().stream().filter(player -> !player.equals(round.getCzar())).forEach(player -> {
            while (player.getHand().size() < handSize && it.hasNext()) {
                WhiteCard card = it.next();
                game.getWhiteDeck().remove(card);
                player.getHand().add(card);
            }
        });
    }

    private Round createRound(Game game) {
        Round round = new Round();

        List<Player> playerList = new ArrayList<>(game.getPlayers());
        playerList.sort((a,b) -> a.getOrder() - b.getOrder());
        if (game.getCurrentRound() == null) {
            round.setCzar(playerList.get(0));
        } else {
            int index = (game.getCurrentRound().getCzar().getOrder() + 1) % playerList.size();
            round.setCzar(playerList.get(index));
            round.setPrevious(game.getCurrentRound());
        }
        return round;
    }

    private BlackCard selectBlackCard(Game game, Round round) {
        List<BlackCard> blackCards = new ArrayList<>(game.getBlackDeck());
        BlackCard selected = blackCards.get(random.nextInt(blackCards.size()));

        game.getBlackDeck().remove(selected);
        round.setBlackCard(selected);
        return selected;
    }
}
