package uk.me.krupa.wwa.repository.user;

import org.springframework.data.neo4j.repository.GraphRepository;
import uk.me.krupa.wwa.entity.user.UserAuthority;

/**
 * Created by krupagj on 25/03/2014.
 */
public interface UserAuthorityRepository extends GraphRepository<UserAuthority> {

    UserAuthority findByPermission(String permission);

}
