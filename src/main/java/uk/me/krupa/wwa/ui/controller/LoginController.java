package uk.me.krupa.wwa.ui.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 * Created by krupagj on 19/03/2014.
 */
@Controller("loginController")
public class LoginController {
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
