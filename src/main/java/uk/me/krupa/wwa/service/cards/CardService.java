package uk.me.krupa.wwa.service.cards;

import uk.me.krupa.wwa.dto.summary.CardSetSummary;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;

import java.util.List;

/**
 * Created by krupagj on 27/03/2014.
 */
public interface CardService {

    List<CardSetSummary> getAllCardSets();

    List<BlackCard> getBlackCardsFor(CardSet cardSet);

    List<WhiteCard> getWhiteCardsFor(CardSet cardSet);

}
