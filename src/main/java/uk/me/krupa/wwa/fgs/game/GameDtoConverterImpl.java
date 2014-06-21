package uk.me.krupa.wwa.fgs.game;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import uk.me.krupa.wwa.dto.detail.CardDetail;
import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.detail.PlayDetail;
import uk.me.krupa.wwa.dto.detail.WhiteCardDetail;
import uk.me.krupa.wwa.dto.summary.GameSummary;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.GameState;
import uk.me.krupa.wwa.entity.game.Play;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by krupagj on 17/06/2014.
 */
@Service("gameDtoConverter")
@Scope("singleton")
public class GameDtoConverterImpl implements GameDtoConverter {

    @Autowired
    private Mapper mapper;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Override
    public List<GameSummary> toSummaries(Collection<Game> games, User user) {
        LinkedList<GameSummary> summaries = new LinkedList<>();
        games.stream().map(g -> toSummary(g, user)).forEach(summaries::add);
        return Collections.unmodifiableList(summaries);
    }

    @Override
    public GameSummary toSummary(Game game, User user) {
        GameSummary gameSummary = mapper.map(game, GameSummary.class);
        gameSummary.setYourTurn(isYourTurn(game, user));
        gameSummary.setCanStart(canStart(game, user));
        gameSummary.setCanPlay(canPlay(game));
        if (game.getOwner().getUser().equals(user)) {
            gameSummary.setMyGame(true);
        }
        return gameSummary;
    }

    @Override
    public GameDetail toDetail(Game game, User user) {
        GameDetail detail = mapper.map(game, GameDetail.class);
        Round round = game.getCurrentRound();
        Round lastRound = neo4jTemplate.fetch(round.getPrevious());

        detail.setCzar(round.getCzar().getUser().equals(user));
        detail.setCanPlay(isYourTurn(game, user));

        if (!detail.isCzar()) {
            game.getPlayerForUser(user).getHand().stream()
                    .map(c -> mapper.map(c, WhiteCardDetail.class))
                    .forEach(detail.getHand()::add);
        }

        if (allPlayed(game)) {
            round.getPlays().stream()
                    .map(c -> populatePlayDetail(c, round, user, false))
                    .forEach(detail.getPlays()::add);
        } else {
            round.getPlays().stream()
                    .filter(p -> p.getPlayer().getUser().equals(user))
                    .map(c -> populatePlayDetail(c, round, user, false))
                    .forEach(detail.getPlays()::add);
        }

        if (lastRound != null) {
            lastRound.getPlays().stream()
                    .map(c -> populatePlayDetail(c, lastRound, user, true))
                    .forEach(detail.getPreviousPlays()::add);

            detail.getPreviousPlays().add(createCzarPlay(user, lastRound));
        }

        detail.setMyGame(game.getOwner().getUser().equals(user));
        return detail;
    }

    private PlayDetail createCzarPlay(User user, Round round) {
        PlayDetail fakePlay = new PlayDetail();
        fakePlay.setPlayerScore(round.getCzar().getScore());
        fakePlay.setPlayerUrl(round.getCzar().getUser().getImageUrl());
        fakePlay.setPlayerName(round.getCzar().getUser().getFullName());
        fakePlay.setYourPlay(round.getCzar().getUser().equals(user));
        return fakePlay;
    }

    private PlayDetail populatePlayDetail(Play play, Round round, User user, boolean populateUserDetails) {
        PlayDetail detail = mapper.map(play, PlayDetail.class);
        detail.setYourPlay(play.getPlayer().getUser().equals(user));
        detail.setPlayedText(replacePlaceholders(round.getBlackCard().getText(), play));
        detail.setWinner(play.equals(round.getWinningPlay()));
        if (populateUserDetails) {
            detail.setPlayerName(play.getPlayer().getUser().getFullName());
            detail.setPlayerUrl(play.getPlayer().getUser().getImageUrl());
            detail.setPlayerScore(play.getPlayer().getScore());
        }
        return detail;
    }

    private String replacePlaceholders(String text, Play play) {
        List<String> data = new ArrayList<>();
        data.add(play.getCard1().getText());
        if (play.getCard2() != null) {
            data.add(play.getCard2().getText());
        }
        if (play.getCard3() != null) {
            data.add(play.getCard3().getText());
        }
        return replacePlaceholders(text, data);
    }

    private String replacePlaceholders(String text, List<String> data) {
        for (String value: data) {
            if (text.contains("_")) {
                int index = text.indexOf('_');
                text = text.substring(0, index) + value + text.substring(index+1);
            }
        }
        return text;
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
            return allPlayed(game);
        } else {
            return game.getCurrentRound().playFor(user) == null;
        }
    }

    private boolean allPlayed(Game game) {
        return game.getCurrentRound().getPlays().size() == game.getPlayers().size() - 1;
    }
}
