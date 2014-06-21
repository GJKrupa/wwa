package uk.me.krupa.wwa.dto.nfy;

/**
 * Created by krupagj on 21/06/2014.
 */
public enum NotificationType {
    CREATED("{0} has created game {1}"),
    JOINED("{0} has joined game {1}"),
    STARTED("{0} has started"),
    ALL_PLAYED("{0} is now ready for judging"),
    WINNER_CHOSEN("{0} has won the last round of {1}");

    public final String message;

    private NotificationType(final String message) {
        this.message = message;
    }
}
