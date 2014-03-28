package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;
import uk.me.krupa.wwa.service.cards.CardService;
import uk.me.krupa.wwa.service.user.UserService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by krupagj on 19/03/2014.
 */
@Controller("loginController")
@Scope("request")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    private List<BlackCard> blackCardList;

    private User user;

    @PostConstruct
    public void init() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userService.loadByUsername(username);
    }

    public List<BlackCard> getBlackCardList() {
        if (blackCardList == null) {
            Collection<CardSet> sets = cardService.getAllCardSets();
            blackCardList = cardService.getBlackCardsFor(sets.iterator().next());
            sets.iterator().next();
        }
        return blackCardList;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getImageUrl() {
        return user.getImageUrl();
    }

    public String getName() {
        return user.getFullName();
    }

}
