package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.service.user.UserService;

import javax.faces.context.FacesContext;

/**
 * Created by krupagj on 23/04/2014.
 */
@Controller("signUpController")
@Scope("request")
public class SignUpController extends AbstractController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private User pendingUser;

    public User getPendingUser() {
        if (pendingUser == null) {
            pendingUser = new User();
        }
        return pendingUser;
    }

    public void setPendingUser(User pendingUser) {
        this.pendingUser = pendingUser;
    }

    public String createUser() {
        pendingUser.getGrantedAuthorities().add(userService.getUserAuthority("ROLE_USER"));
        pendingUser.setFullName(pendingUser.getFirstName() + " " + pendingUser.getLastName());
        pendingUser.setImageUrl(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/user_photo_generic.png");
        pendingUser.setPassword(passwordEncoder.encode(pendingUser.getPassword()));
        userService.saveUser(pendingUser);
        return "/login";
    }
}
