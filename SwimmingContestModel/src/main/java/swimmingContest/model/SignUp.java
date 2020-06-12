package swimmingContest.model;


import javafx.util.Pair;

import java.io.Serializable;

/***
 * The SignUp represents the Participant and the SwimmingEvents that he/she is going to attend
 * A SignUp is defined by the Id of the Participant and of the SwimmingEvent
 */
public class SignUp extends Entity<Pair<Long,Long>> implements Serializable {
    public SignUp() {
    }

    @Override
    public String toString() {
        return this.getId().getKey()+" "+this.getId().getValue();
    }
}
