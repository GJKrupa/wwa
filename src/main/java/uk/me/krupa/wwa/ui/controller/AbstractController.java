package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.service.user.UserService;

import javax.annotation.PostConstruct;

/**
 * Created by krupagj on 21/04/2014.
 */
public class AbstractController {

    @Autowired
    private UserService userService;

    private User user;

    @PostConstruct
    public void init() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userService.loadByUsername(username);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getImageUrl() {
        return user.getImageUrl();
    }

    public String getFullName() {
        return user.getFullName();
    }

    protected User getUser() {
        return user;
    }
}
