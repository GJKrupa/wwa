package uk.me.krupa.wwa.service.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.repository.cards.CardRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
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

    @Override
    @Transactional
    public CardSet createCardSet(String name) {
        CardSet cardSet = new CardSet();
        cardSet.setName(name);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/black.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    BlackCard card = new BlackCard();
                    card.setText(line);
                    card.setPlayCount(StringUtils.countOccurrencesOf(line, "_"));
                    cardSet.getBlackCards().add(card);
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("No black.txt", ex);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/white.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    WhiteCard card = new WhiteCard();
                    card.setText(line);
                    cardSet.getWhiteCards().add(card);
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("No black.txt", ex);
        }

        return cardRepository.save(cardSet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardSet> getAllCardSets() {
        return cardRepository.findAll().as(LinkedList.class);
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
