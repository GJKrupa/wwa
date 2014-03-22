package uk.me.krupa.wwa.repository.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krupagj on 21/03/2014.
 */
@Repository
public class UserDetailsDao {

    private Map<String,String> imageMap = new HashMap<>();

    public void addImageForUser(String userId, String url) {
        imageMap.put(userId, url);
    }

    public String getUrlForUser(String userId) {
        return imageMap.get(userId);
    }

}
