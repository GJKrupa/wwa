package uk.me.krupa.wwa.repository.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krupagj on 21/03/2014.
 */
@Repository
public class UserDetailsDao implements UserDetailsRepository {

    private Map<String,String> imageMap = new HashMap<>();
    private Map<String,String> nameMap = new HashMap<>();

    @Override
    public void addImageForUser(String userId, String url) {
        imageMap.put(userId, url);
    }

    @Override
    public String getUrlForUser(String userId) {
        return imageMap.get(userId);
    }

    @Override
    public void addNameForUser(String userId, String name) {
        nameMap.put(userId, name);
    }

    @Override
    public String getNameForUser(String userId) {
        return nameMap.get(userId);
    }

}
