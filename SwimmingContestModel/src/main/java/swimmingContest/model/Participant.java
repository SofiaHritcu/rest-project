package swimmingContest.model;

import java.io.Serializable;
import java.util.Objects;

/***
 * The Participant class extends the Person class having a one more attribute(age)
 */
public class Participant extends Person implements Serializable {
    private Integer age;

    public Participant() {
    }

    /***
     * A object of class Participant is a Person with age
     * @param firstName - the firstName of the Participant
     * @param lastName - the lastName of the Participant
     * @param age - the age of the Participant ( the attribute that is added to the class Person)
     */
    public Participant(String firstName, String lastName, Integer age) {
        super(firstName, lastName);
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Participant that = (Participant) o;
        return Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), age);
    }

    @Override
    public String toString() {
        return "Participant: "+this.getFirstName()+" "+this.getLastName()+" "+this.getAge();
    }
}
