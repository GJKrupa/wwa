package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.service.cards.CardService;

/**
 * Created by krupagj on 27/03/2014.
 */
@Controller("cardSetController")
@Scope("request")
public class CardSetController {

    @Autowired
    private CardService cardService;

    private String cardSetName;

    public String getCardSetName() {
        return cardSetName;
    }

    public void setCardSetName(String cardSetName) {
        this.cardSetName = cardSetName;
    }

    public String createCardSet() {
        cardService.createCardSet(cardSetName);
        return "/index";
    }
}
