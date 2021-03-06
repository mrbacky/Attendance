package attendance.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author annem
 */
public class User {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private UserType type;
    private int currentSelectedCourse;

    public User(int id, String name, UserType type) {
        this.id.set(id);
        this.name.set(name);
        this.type = type;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public enum UserType {
        TEACHER, STUDENT
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public int getCurrentSelectedCourse() {
        return currentSelectedCourse;
    }

    public void setCurrentSelectedCourse(int currentSelectedCourse) {
        this.currentSelectedCourse = currentSelectedCourse;
    }

}
