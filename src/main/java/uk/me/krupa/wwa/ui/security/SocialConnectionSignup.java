package uk.me.krupa.wwa.ui.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.entity.user.UserAuthority;
import uk.me.krupa.wwa.service.user.UserService;

import java.util.logging.Logger;

/**
 * Created by krupagj on 20/03/2014.
 */
public class SocialConnectionSignup implements ConnectionSignUp {

    private static final Logger LOGGER = Logger.getLogger(SocialConnectionSignup.class.getName());

    @Autowired
    private UserService userService;

    @Override
    public String execute(Connection<?> connection) {

        String username = connection.getProfileUrl();
        User user = userService.loadByUsername(username);

        if (user == null) {
            LOGGER.info("Creating new user for " + connection.getProfileUrl());

            user = new User();
            UserProfile userProfile = connection.fetchUserProfile();
            user.setFirstName(userProfile.getFirstName());
            user.setLastName(userProfile.getLastName());
            user.setFullName(userProfile.getName());
            user.setImageUrl(connection.getImageUrl());
            user.setUsername(username);

            user.getGrantedAuthorities().add(getDefaultAuthority());

            user = userService.saveUser(user);
        }

        return user.getUsername();
    }

    public UserAuthority getDefaultAuthority() {
        UserAuthority authority = userService.getUserAuthority(UserAuthority.ROLE_USER);
        if (authority == null) {
            authority = new UserAuthority();
            authority.setPermission(UserAuthority.ROLE_USER);
            authority = userService.saveUserAuthority(authority);
        }
        return authority;
    }
}
