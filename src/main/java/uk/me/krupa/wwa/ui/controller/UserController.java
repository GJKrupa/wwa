package uk.me.krupa.wwa.ui.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Created by krupagj on 22/04/2014.
 */
@Controller("userController")
@Scope("request")
public class UserController extends AbstractController {

    public String logout() {
        SecurityContextHolder.clearContext();
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        return "/login";
    }

    public void forceMobileMode() {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).setAttribute("mobile", Boolean.TRUE);
    }

}
