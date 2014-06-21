package uk.me.krupa.wwa.service.notification;

import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.entity.user.User;

/**
 * Created by krupagj on 21/06/2014.
 */
public interface NotificationService {
    void notifyGameCreated(User user, Long id, String name);
    void notifyGameJoined(User user, long id);
    void notifyGameStarted(User user, long id);
    void notifyJudgingReady(User user, long id);
    void notifyJudged(User user, long id);
}
