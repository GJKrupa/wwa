package uk.me.krupa.wwa.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.*;
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
    @Transactional(readOnly = true)
    public List<Game> getOpenGames(User user) {
        return Collections.unmodifiableList(new LinkedList(gameRepository.listOpenGames(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getGamesForUser(User user) {
        return Collections.unmodifiableList(new LinkedList(gameRepository.listGamesForUser(user)));
    }

    @Override
    @Transactional
    public void createGame(User user, String name) {
        Game game = new Game();
        game.setName(name);
        game.setState(GameState.PENDING);

        Player owner = game.addPlayer(user);
        game.setOwner(owner);

        CardSet cardSet = cardRepository.findAll().iterator().next();

        game.getBlackDeck().addAll(cardRepository.getBlackCardsInSet(cardSet));
        game.getWhiteDeck().addAll(cardRepository.getWhiteCardsInSet(cardSet));

        game = gameRepository.save(game);
    }

    @Override
    @Transactional
    public void joinGame(User user, Long id) {
        Game game = gameRepository.findOne(id);
        game.addPlayer(user);
        gameRepository.save(game);
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGameById(long id) {
        return gameRepository.findOne(id);
    }

    @Override
    @Transactional
    public void playCards(User user, List<WhiteCard> cards, Long gameId) {
        Game game = gameRepository.findOne(gameId);
        Player player = game.getPlayerForUser(user);

        Round currentRound = game.getCurrentRound();
        Play play = new Play();
        play.setCard1(cards.get(0));
        if (cards.size() > 1) {
            play.setCard2(cards.get(2));
        }
        if (cards.size() > 3) {
            play.setCard3(cards.get(3));
        }
        play.setPlayer(player);
        currentRound.getPlays().add(play);

        gameRepository.save(game);
    }

    private void pickAdditionalCards(Game game, Round round) {

        neo4jTemplate.fetch(game.getWhiteDeck());
        neo4jTemplate.fetch(game.getBlackDeck());

        List<WhiteCard> whiteCards = new ArrayList<>(game.getWhiteDeck());
        Collections.shuffle(whiteCards, random);
        Iterator<WhiteCard> it = whiteCards.iterator();

        game.getPlayers().stream().forEach(player -> {
            int handSize;
            // Play 3, Pick 2
            if (round.getBlackCard().getPlayCount() == 3 && !round.getCzar().equals(player)) {
                handSize = 12;
            } else {
                handSize = 10;
            }

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
        neo4jTemplate.fetch(game.getBlackDeck());
        List<BlackCard> blackCards = new ArrayList<>(game.getBlackDeck());
        BlackCard selected = blackCards.get(random.nextInt(blackCards.size()));

        game.getBlackDeck().remove(selected);
        round.setBlackCard(selected);
        return selected;
    }
}