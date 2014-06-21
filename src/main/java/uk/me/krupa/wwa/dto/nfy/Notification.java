package uk.me.krupa.wwa.dto.nfy;

import java.text.MessageFormat;

/**
 * Created by krupagj on 21/06/2014.
 */
public class Notification {
    private final long game;
    private final long user;
    private final NotificationType notificationType;
    private final String displayMessage;

    public Notification(long game, long user, NotificationType type, Object... parameters) {
        this.game = game;
        this.user = user;
        this.notificationType = type;
        this.displayMessage = MessageFormat.format(notificationType.message, parameters);
    }

    public long getGame() {
        return game;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public long getUser() {
        return user;
    }
}
