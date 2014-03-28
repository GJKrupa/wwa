package uk.me.krupa.wwa.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.neo4j.annotation.GraphTraversal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.entity.user.UserAuthority;
import uk.me.krupa.wwa.repository.user.UserAuthorityRepository;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;

import java.util.logging.Logger;

/**
 * Created by krupagj on 19/03/2014.
 */
@Service("springSocialSecurityUserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService, SocialUserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadByUsername(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        return loadByUsername(userId);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userDetailsRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User loadByUsername(String username) {
        LOGGER.info("Loading user " + username);
        return userDetailsRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAuthority getUserAuthority(String permission) {
        return userAuthorityRepository.findByPermission(permission);
    }

    @Override
    @Transactional
    public UserAuthority saveUserAuthority(UserAuthority userAuthority) {
        return userAuthorityRepository.save(userAuthority);
    }
}