package uk.me.krupa.wwa.service.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.krupa.wwa.dto.summary.CardSetSummary;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.fgs.card.CardDtoConverter;
import uk.me.krupa.wwa.repository.cards.CardRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by krupagj on 27/03/2014.
 */
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardDtoConverter cardDtoConverter;

    @Override
    @Transactional(readOnly = true)
    public List<CardSetSummary> getAllCardSets() {
        return cardDtoConverter.toSummaries(cardRepository.findAll().as(LinkedList.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlackCard> getBlackCardsFor(CardSet cardSet) {
        return Collections.<BlackCard>unmodifiableList(cardRepository.getBlackCardsInSet(cardSet));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WhiteCard> getWhiteCardsFor(CardSet cardSet) {
        return Collections.unmodifiableList(cardRepository.getWhiteCardsInSet(cardSet));
    }
}
