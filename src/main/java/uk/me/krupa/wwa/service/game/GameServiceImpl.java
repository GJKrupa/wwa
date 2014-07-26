package uk.me.krupa.wwa.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.game.*;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.fgs.game.GameDtoConverter;
import uk.me.krupa.wwa.repository.cards.CardRepository;
import uk.me.krupa.wwa.repository.cards.WhiteCardRepository;
import uk.me.krupa.wwa.repository.game.GameRepository;
import uk.me.krupa.wwa.repository.game.PlayerRepository;
import uk.me.krupa.wwa.repository.game.RoundRepository;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by krupagj on 21/04/2014.
 */
@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOG = Logger.getLogger(GameServiceImpl.class.getName());

    private Random random = new Random();

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private WhiteCardRepository whiteCardRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private GameDtoConverter gameDtoConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = false)
    public Round createNewRound(long gameId) {
        Game game = gameRepository.findOne(gameId);

        createNewRound(game);

        game = gameRepository.save(game);
        return game.getCurrentRound();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSummary> getGamesForUser(User user) {
        return gameDtoConverter.toSummaries(gameRepository.listGamesForUser(user), user);
    }

    @Override
    @Transactional(readOnly = true)
    public GameSummary getGameSummaryById(long id, User user) {
        return gameDtoConverter.toSummary(gameRepository.findOne(id), user);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDetail getGameDetailById(long id, User user) {
        try {
            return gameDtoConverter.toDetail(gameRepository.findOne(id), user);
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, "Failed", t);
            throw t;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSummary> getOpenGames(User user) {
        return gameDtoConverter.toSummaries(gameRepository.listOpenGames(user), user);
    }

    @Override
    @Transactional
    public GameSummary createGame(User user, String name, String password, List<Long> cardSets) {
        Game game = new Game();
        game.setName(name);
        if (StringUtils.hasText(password)) {
            game.setPassword(passwordEncoder.encode(password));
        }
        game.setState(GameState.PENDING);

        Player owner = game.addPlayer(user);
        game.setOwner(owner);

        cardSets.stream().map(s -> cardRepository.findOne(s))
                .filter(s -> s != null)
                .forEach(s -> {
                    game.getBlackDeck().addAll(cardRepository.getBlackCardsInSet(s));
                    game.getWhiteDeck().addAll(cardRepository.getWhiteCardsInSet(s));
                });

        return gameDtoConverter.toSummary(gameRepository.save(game), user);
    }

    @Override
    @Transactional
    public void joinGame(User user, Long id, String password) {
        Game game = gameRepository.findOne(id);

        if (game.getPassword() != null) {
            if (!passwordEncoder.matches(password, game.getPassword())) {
                throw new AccessDeniedException("Incorrect password");
            }
        }

        game.addPlayer(user);
        gameRepository.save(game);
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGameById(long id) {
        Game game = gameRepository.findOne(id);
        neo4jTemplate.fetch(game.getCurrentRound().getPrevious());
        return game;
    }

    @Override
    @Transactional
    public void playCards(User user, List<Long> cardIds, Long gameId) {
        Game game = gameRepository.findOne(gameId);
        Player player = game.getPlayerForUser(user);

        List<WhiteCard> cards = new LinkedList<>();
        cardIds.stream().map(c -> whiteCardRepository.findOne(c)).forEach(cards::add);

        player.getHand().removeAll(cards);

        Round currentRound = game.getCurrentRound();
        Play play = new Play();
        play.setCard1(cards.get(0));
        if (cards.size() > 1) {
            play.setCard2(cards.get(1));
        }
        if (cards.size() > 2) {
            play.setCard3(cards.get(2));
        }
        play.setPlayer(player);
        currentRound.getPlays().add(play);

        playerRepository.save(player);
        gameRepository.save(game);
    }

    @Override
    @Transactional
    public void startGame(Long id) {
        Game game = gameRepository.findOne(id);
        createNewRound(game);
        game.setState(GameState.IN_PROGRESS);
        gameRepository.save(game);
    }

    @Override
    @Transactional
    public GameSummary startGame(Long id, User user) {
        Game game = gameRepository.findOne(id);
        createNewRound(game);
        game.setState(GameState.IN_PROGRESS);
        return gameDtoConverter.toSummary(gameRepository.save(game), user);
    }

    @Override
    @Transactional
    public void chooseWinner(Long gameId, Long winningPlay) {
        Game game = gameRepository.findOne(gameId);
        putCardsInPack(game);

        game.getCurrentRound().getPlays().stream()
                .filter(p -> winningPlay.equals(p.getId()))
                .findFirst()
                .ifPresent(p -> registerWinner(game, p));

        createNewRound(game);
        gameRepository.save(game);
    }


    private void registerWinner(Game game, Play winningPlay) {
        game.getCurrentRound().setWinningPlay(winningPlay);
        roundRepository.save(game.getCurrentRound());
        game.getPlayers().stream()
                .filter(p -> winningPlay.getPlayer().equals(p))
                .findFirst()
                .ifPresent(p -> {
                    p.setScore(p.getScore() + 1);
                    playerRepository.save(p);
                });
    }

    private void putCardsInPack(Game game) {
        game.getBlackDeck().add(game.getCurrentRound().getBlackCard());
        game.getCurrentRound().getPlays().forEach(
                p -> game.getWhiteDeck().addAll(p.getCardsAsList())
        );
    }

    private void createNewRound(Game game) {
        Round round = createRound(game);
        selectBlackCard(game, round);
        pickAdditionalCards(game, round);
        game.setCurrentRound(round);
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
