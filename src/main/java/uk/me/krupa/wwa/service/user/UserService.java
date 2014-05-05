package uk.me.krupa.wwa.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.entity.user.UserAuthority;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;

import java.util.Collections;

public interface UserService extends UserDetailsService {

    User saveUser(User user);
    User loadByUsername(String username);

    UserAuthority getUserAuthority(String permission);
    UserAuthority saveUserAuthority(UserAuthority userAuthority);

}
