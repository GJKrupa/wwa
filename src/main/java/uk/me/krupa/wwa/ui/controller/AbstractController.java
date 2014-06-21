package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.service.user.UserService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * Created by krupagj on 21/04/2014.
 */
public class AbstractController {

    @Autowired
    private UserService userService;

    private String name;
    private User user;

    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (user == null || name == null || !name.equals(username)) {
            user = userService.loadByUsername(username);
            name = username;
        }

        return user;
    }

    public String getUsername() {
        return getUser().getUsername();
    }

    public String getImageUrl() {
        return getUser().getImageUrl();
    }

    public String getFullName() {
        return getUser().getFullName();
    }

    protected <T> void addToFlash(String key, T value) {
        getFlashScope().put(key, value);
    }

    protected <T> T getFromFlash(String key, Class<? extends T> clazz) {
        return (T)getFlashScope().get(key);
    }

    protected boolean isInFlash(String key) {
        return getFlashScope().containsKey(key);
    }

    private Flash getFlashScope() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }
}
