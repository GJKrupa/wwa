package uk.me.krupa.wwa.ui.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;

/**
 * Created by krupagj on 20/03/2014.
 */
public class SocialConnectionSignup implements ConnectionSignUp {

    @Autowired
    private UserDetailsRepository userDetailsStore;

    @Override
    public String execute(Connection<?> connection) {
        userDetailsStore.addImageForUser(connection.getProfileUrl(), connection.getImageUrl());
        userDetailsStore.addNameForUser(connection.getProfileUrl(), connection.fetchUserProfile().getName());
        return connection.getProfileUrl();
    }
}
