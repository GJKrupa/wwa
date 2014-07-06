package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.me.krupa.wwa.dto.summary.CardSetSummary;
import uk.me.krupa.wwa.service.cards.CardService;

import java.util.List;

@Controller("cardManagerController")
@Scope("request")
public class CardManagerController {

    @Autowired
    private CardService cardService;

    @RequestMapping("/cardSets.do")
    @ResponseBody
    public List<CardSetSummary> getMyGames() {
        return cardService.getAllCardSets();
    }

}
