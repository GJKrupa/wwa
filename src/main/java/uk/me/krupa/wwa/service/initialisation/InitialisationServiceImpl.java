package uk.me.krupa.wwa.service.initialisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.me.krupa.wwa.entity.cards.BlackCard;
import uk.me.krupa.wwa.entity.cards.CardSet;
import uk.me.krupa.wwa.entity.cards.WhiteCard;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.entity.user.UserAuthority;
import uk.me.krupa.wwa.repository.cards.CardRepository;
import uk.me.krupa.wwa.repository.user.UserAuthorityRepository;
import uk.me.krupa.wwa.repository.user.UserDetailsRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by krupagj on 02/05/2014.
 */
@Service("initialisationService")
public class InitialisationServiceImpl implements InitialisationService {

    private static final Logger LOG = Logger.getLogger(InitialisationServiceImpl.class.getName());

    private static final int TEST_USER_COUNT = 10;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void initialise() {
        if (!initialised()) {
            LOG.warning("Data not loaded.  Initialising reference data.");
            UserAuthority defaultAuthority = createUserAuthorities();
            // TODO: Remove for production
            createTestUsers(defaultAuthority);
            createCardSet("Default", "The standard Cards Against Humanity set", "standard");
            createCardSet("EYC 8/51", "Expand your Cards Black 8/White 51", "eyc_8_51");
        }
    }

    private void createTestUsers(UserAuthority defaultAuthority) {
        User user;
        final String password = passwordEncoder.encode("password");
        for (int n=1; n<TEST_USER_COUNT+1; ++n) {
            user = new User();
            user.setFirstName("Player");
            user.setLastName("" + n);
            user.setFullName("Player " + n);
            user.setUsername("player" + n);
            user.setPassword(password);
            user.setImageUrl("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male-128.png");
            user.setGrantedAuthorities(Collections.singleton(defaultAuthority));
            userDetailsRepository.save(user);
        }
    }

    private UserAuthority createUserAuthorities() {
        UserAuthority authority = new UserAuthority();
        authority.setPermission(UserAuthority.ROLE_USER);
        return userAuthorityRepository.save(authority);
    }

    private boolean initialised() {
        return userAuthorityRepository.findByPermission(UserAuthority.ROLE_USER) != null;
    }

    private void createCardSet(String name, String description, String directory) {
        CardSet cardSet = new CardSet();
        cardSet.setName(name);
        cardSet.setDescription(description);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/cardsets/" + directory + "/black.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("BLACK: " + line);
                if (!line.isEmpty()) {

                    BlackCard card = new BlackCard();
                    card.setText(line);
                    card.setPlayCount(StringUtils.countOccurrencesOf(line, "_"));
                    cardSet.getBlackCards().add(card);
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("No black.txt", ex);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/cardsets/" + directory + "/white.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("WHITE: " + line);
                if (!line.isEmpty()) {
                    WhiteCard card = new WhiteCard();
                    card.setText(line);
                    cardSet.getWhiteCards().add(card);
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("No white.txt", ex);
        }

        cardRepository.save(cardSet);
    }
}
