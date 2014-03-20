package uk.me.krupa.wwa.service.user;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by krupagj on 19/03/2014.
 */
@Service("springSocialSecurityUserDetailsService")
public class UserService implements UserDetailsService, SocialUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SocialUser(username, "FAKE", Collections.<GrantedAuthority>emptyList());
    }


    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        return new SocialUser(userId, "FAKE", Collections.<GrantedAuthority>emptyList());
    }
}
