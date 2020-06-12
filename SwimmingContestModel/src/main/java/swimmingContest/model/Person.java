package swimmingContest.model;

import java.io.Serializable;
import java.util.Objects;

/***
 * The class Person defines a person object extended from Entity class
 */
public class Person extends Entity<Long> implements Serializable {

    private String firstName,lastName;

    public Person() {
    }

    /***
     * An object of class Person has 2 attributes:
     * @param firstName - the firstName of the Person object
     * @param lastName - the lastName of the Person
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Person: "+this.firstName+" "+this.lastName;
    }
}
