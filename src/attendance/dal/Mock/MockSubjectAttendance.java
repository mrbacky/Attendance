package attendance.dal.Mock;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MockSubjectAttendance {

    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty overall = new SimpleIntegerProperty();
    private final StringProperty details = new SimpleStringProperty();

    public MockSubjectAttendance(String name, int overall, String details) {
        setName(name);
        setOverall(overall);
        setDetails(details);
        
        
    }

    public String getDetails() {
        return details.get();
    }

    public void setDetails(String value) {
        details.set(value);
    }

    public StringProperty detailsProperty() {
        return details;
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

    public int getOverall() {
        return overall.get();
    }

    public void setOverall(int value) {
        overall.set(value);
    }

    public IntegerProperty overallProperty() {
        return overall;
    }

}