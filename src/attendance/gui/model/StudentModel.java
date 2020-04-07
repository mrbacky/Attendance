package attendance.gui.model;

import attendance.be.Course;
import attendance.be.Student;
import attendance.bll.ConcreteObservable;
import attendance.bll.DataObserver;
import attendance.bll.LogicFacade;
import attendance.bll.LogicManager;
import attendance.dal.Mock.MockStudentDAO;
import java.time.LocalDateTime;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class StudentModel {

    private static StudentModel studentModel;
    private final MockStudentDAO mockStudentDAO;
    private final LogicFacade logicManager;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();  
    private final ObservableList<Integer> absencePerWeekday = FXCollections.observableArrayList();

    private final IntegerProperty enrolledStudentsLabel = new SimpleIntegerProperty();
    private final IntegerProperty attendanceCountProperty;
    private ConcreteObservable bllComponent;

    public static StudentModel getInstance() {
        if (studentModel == null) {
            studentModel = new StudentModel();
        }
        return studentModel;
    }

    private StudentModel() {
        mockStudentDAO = new MockStudentDAO();
        logicManager = new LogicManager();
        attendanceCountProperty = new SimpleIntegerProperty();
    }

    public void loadAllStudents(Course course, LocalDateTime current) {// calculate absence here
        List<Student> allStudents = logicManager.calculateAbsencePercentage(course, current);
        studentList.clear();
        studentList.addAll(allStudents);
        enrolledStudentsLabel.setValue(allStudents.size());
    }

    public ObservableList<Student> getObsStudents() {
        return studentList;
    }

    public ObservableValue<Number> getAttendanceCountProperty() {
        return attendanceCountProperty;
    }

    public void startObserving(Course c) {
        bllComponent = new ConcreteObservable(c);
        System.out.println("startObserving");
        DataObserver observer = new DataObserver() {
            @Override
            public void update(Course c) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        attendanceCountProperty.setValue(bllComponent.getState());

                    }
                });
            }
        };
        bllComponent.attach(observer);
    }

    public int getEnrolledStudentsLabel() {
        return enrolledStudentsLabel.get();
    }

    public void setEnrolledStudentsLabel(int value) {
        enrolledStudentsLabel.set(value);
    }

    public IntegerProperty enrolledStudentsLabelProperty() {
        return enrolledStudentsLabel;
    }
    
    public void loadAllWeekdayAbsenceCount(int userId, int courseId){
        List <Integer> lst = logicManager.getWeekdayAbsenceForCourse(userId, courseId);
        absencePerWeekday.clear();
        absencePerWeekday.addAll(lst);
    }
            
    public ObservableList<Integer> getObsWeekdayAbsenceCount(){
        return absencePerWeekday;
    }
}
