package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;

/**
 * Created by krupagj on 19/03/2014.
 */
@Controller("loginController")
public class LoginController {

    @Autowired
    private UserDetailsRepository userDetailsStore;

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getImageUrl() {
        return userDetailsStore.getUrlForUser(getUsername());
    }

    public String getName() {
        return userDetailsStore.getNameForUser(getUsername());
    }

}
