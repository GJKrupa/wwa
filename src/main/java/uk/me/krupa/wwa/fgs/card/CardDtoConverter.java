package uk.me.krupa.wwa.fgs.card;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import uk.me.krupa.wwa.dto.summary.CardSetSummary;
import uk.me.krupa.wwa.entity.cards.CardSet;

import java.util.Collection;
import java.util.List;

@Service("cardDtoConverter")
@Scope("singleton")
public interface CardDtoConverter {

    CardSetSummary toSummary(CardSet cardSet);
    List<CardSetSummary> toSummaries(Collection<CardSet> cardSets);

}
