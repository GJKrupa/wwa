package uk.me.krupa.wwa.dto.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Request to create a new game.
 */
public class CreateGameRequest {

    private String name;
    private String password;
    private List<Long> cardSets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getCardSets() {
        if (cardSets == null) {
            cardSets = new ArrayList<>();
        }
        return cardSets;
    }

    public void setCardSets(List<Long> cardSets) {
        this.cardSets = cardSets;
    }
}
