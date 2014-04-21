package uk.me.krupa.wwa.entity.game;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.common.BaseEntity;
import uk.me.krupa.wwa.entity.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by krupagj on 21/04/2014.
 */
@NodeEntity
public class Game extends BaseEntity {

    private String name;

    @Fetch
    private Player owner;

    @Fetch
    private Set<Player> players;

    @Fetch
    private Round currentRound;

    @Fetch
    private Set<BlackCard> blackDeck;

    @Fetch
    private Set<WhiteCard> whiteDeck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Set<Player> getPlayers() {
        if (players == null) {
            players = new HashSet<>();
        }
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public Set<BlackCard> getBlackDeck() {
        if (blackDeck == null) {
            blackDeck = new HashSet<>();
        }
        return blackDeck;
    }

    public void setBlackDeck(Set<BlackCard> blackDeck) {
        this.blackDeck = blackDeck;
    }

    public Set<WhiteCard> getWhiteDeck() {
        if (whiteDeck == null) {
            whiteDeck = new HashSet<>();
        }
        return whiteDeck;
    }

    public void setWhiteDeck(Set<WhiteCard> whiteDeck) {
        this.whiteDeck = whiteDeck;
    }

    public Player addPlayer(User user) {
        Player player = new Player();
        player.setUser(user);
        player.setScore(0);
        if (players != null) {
            player.setOrder(players.stream().mapToInt(p -> p.getOrder()).max().getAsInt() + 1);
        } else {
            player.setOrder(0);
        }
        getPlayers().add(player);
        return player;
    }

    public int getPlayerCount() {
        return players.size();
    }
}
