package uk.me.krupa.wwa.dto.summary;

/**
 * Representation of a player's summary state within a single game.
 */
public class PlayerSummary {

    private String name;
    private String avatarUrl;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
