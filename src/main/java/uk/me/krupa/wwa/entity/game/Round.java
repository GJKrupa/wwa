package uk.me.krupa.wwa.entity.game;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.common.BaseEntity;
import uk.me.krupa.wwa.entity.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by krupagj on 21/04/2014.
 */
@NodeEntity
public class Round extends BaseEntity {

    private Round previous;

    @Fetch
    private BlackCard blackCard;

    @Fetch
    private Set<Play> plays;

    @Fetch
    private Play winningPlay;

    @Fetch
    private Player czar;

    public Round getPrevious() {
        return previous;
    }

    public void setPrevious(Round previous) {
        this.previous = previous;
    }

    public BlackCard getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(BlackCard blackCard) {
        this.blackCard = blackCard;
    }

    public Set<Play> getPlays() {
        if (plays == null) {
            plays = new HashSet<>();
        }
        return plays;
    }

    public void setPlays(Set<Play> plays) {
        this.plays = plays;
    }

    public Player getCzar() {
        return czar;
    }

    public void setCzar(Player czar) {
        this.czar = czar;
    }

    public boolean hasPlayed(User user) {
        return getPlays().stream()
                .filter(p -> p.getPlayer().getUser().equals(user))
                .findFirst()
                .isPresent();
    }

    public Play playFor(User user) {
        return getPlays().stream()
                .filter(p -> p.getPlayer().getUser().equals(user))
                .findFirst()
                .orElse(null);
    }

    public Play getWinningPlay() {
        return winningPlay;
    }

    public void setWinningPlay(Play winningPlay) {
        this.winningPlay = winningPlay;
    }
}
