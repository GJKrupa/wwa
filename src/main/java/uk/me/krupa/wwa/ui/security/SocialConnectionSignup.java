package uk.me.krupa.wwa.ui.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 * Created by krupagj on 20/03/2014.
 */
public class SocialConnectionSignup implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {
        return connection.getProfileUrl();
    }
}
