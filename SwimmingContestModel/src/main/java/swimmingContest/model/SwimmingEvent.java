package swimmingContest.model;

import java.io.Serializable;
import java.util.Objects;

/***
 * An SwimmingEvent is a race in which there are few Participants
 * Is defined by the distance of the race and the stroke (the style in which is swum)
 */
public class SwimmingEvent extends Entity<Long> implements Serializable {
    private SwimmingStroke swimmingStroke;
    private Integer distance;

    public SwimmingEvent() {
    }

    /***
     * A SwimmingEvent it is defined by its stroke and the distance that is going to be swum by its competitors
     * @param swimmingStroke
     * @param distance
     */
    public SwimmingEvent(SwimmingStroke swimmingStroke, Integer distance) {
        this.swimmingStroke = swimmingStroke;
        this.distance = distance;
    }

    public SwimmingStroke getSwimmingStroke() {
        return swimmingStroke;
    }

    public void setSwimmingStroke(SwimmingStroke swimmingStroke) {
        this.swimmingStroke = swimmingStroke;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwimmingEvent that = (SwimmingEvent) o;
        return swimmingStroke == that.swimmingStroke &&
                Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(swimmingStroke, distance);
    }

    @Override
    public String toString() {
        return this.swimmingStroke+" "+this.distance;
    }
}
