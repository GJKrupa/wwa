package uk.me.krupa.wwa.repository.user;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.me.krupa.wwa.entity.user.User;

/**
 * Created by krupagj on 21/03/2014.
 */
public interface UserDetailsRepository extends GraphRepository<User> {

    User findByUsername(String username);

}
