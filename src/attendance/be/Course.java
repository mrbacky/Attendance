/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Martin
 */
public class Course {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty startDuration = new SimpleStringProperty();
    private final StringProperty endDuration = new SimpleStringProperty();
    private final IntegerProperty id = new SimpleIntegerProperty();

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Course(int id, String name) {
        setId(id);
        setName(name);
    }
    
    public Course(String name, String startDuration, String endDuration) {
       
    }
    public String getEndDuration() {
        return endDuration.get();
    }

    public void setEndDuration(String value) {
        endDuration.set(value);
    }

    public StringProperty endDurationProperty() {
        return endDuration;
    }

    public String getStartDuration() {
        return startDuration.get();
    }

    public void setStartDuration(String value) {
        startDuration.set(value);
    }

    public StringProperty startDurationProperty() {
        return startDuration;
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

}
