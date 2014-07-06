package uk.me.krupa.wwa.fgs.card;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import uk.me.krupa.wwa.dto.summary.CardSetSummary;
import uk.me.krupa.wwa.entity.cards.CardSet;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by krupagj on 03/07/2014.
 */
@Service("cardDtoConverter")
@Scope("singleton")
public class CardDtoConverterImpl implements CardDtoConverter {

    @Autowired
    private Mapper mapper;

    @Override
    public CardSetSummary toSummary(CardSet cardSet) {
        CardSetSummary summary = mapper.map(cardSet, CardSetSummary.class);
        return summary;
    }

    @Override
    public List<CardSetSummary> toSummaries(Collection<CardSet> cardSets) {
        LinkedList<CardSetSummary> summaries = new LinkedList<>();
        cardSets.stream().map(g -> toSummary(g)).forEach(summaries::add);
        return Collections.unmodifiableList(summaries);
    }
}
