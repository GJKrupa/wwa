package uk.me.krupa.wwa.ui.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.faces.context.FacesContext;

/**
 * Created by krupagj on 22/04/2014.
 */
@Controller("userController")
@Scope("request")
public class UserController extends AbstractController {

    public String logout() {
        SecurityContextHolder.clearContext();
        return "/login";
    }

}
