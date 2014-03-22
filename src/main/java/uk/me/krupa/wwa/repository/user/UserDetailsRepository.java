package uk.me.krupa.wwa.repository.user;

/**
 * Created by krupagj on 21/03/2014.
 */
public interface UserDetailsRepository {
    void addImageForUser(String userId, String url);

    String getUrlForUser(String userId);
}
