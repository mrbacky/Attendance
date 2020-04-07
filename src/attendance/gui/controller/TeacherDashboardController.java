/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.controller;

import attendance.be.Course;
import attendance.be.Lesson;
import attendance.be.Student;
import attendance.be.User;
import attendance.gui.model.CourseModel;
import attendance.gui.model.LessonModel;
import attendance.gui.model.ModelException;
import attendance.gui.model.StudentModel;
import attendance.gui.model.UserModel;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class TeacherDashboardController implements Initializable {

    @FXML
    private TableView<Student> tbvStudentAbsence;
    @FXML
    private TableColumn<Student, String> studentName;
    @FXML
    private TableColumn<Student, Integer> absence;
    @FXML
    private TableColumn<Student, Integer> lessonCount;

    private StudentModel studentModel;
    private CourseModel courseModel;
    @FXML
    private ComboBox<Course> comboBoxCourses;
    @FXML
    private Label lblstudentname;
    @FXML
    private Label lblStudentsPresent;
    @FXML
    private Button BtnRefreshStudents;
    @FXML
    private Label lblTotalOfStudents;
    @FXML
    private Label lblRequestCount;

    private User user;
    private UserModel userModel;
    @FXML
    private ComboBox<Course> comboBoxCourses1;
    @FXML
    private TableView<Lesson> secondTableView;
    @FXML
    private TableColumn<Lesson, String> dateColumn;
    @FXML
    private TableColumn<Lesson, String> dayColumn;
    @FXML
    private TableColumn<Lesson, String> attendanceColumn;
    private LessonModel lessonModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//      get models
        this.studentModel = StudentModel.getInstance();
        this.userModel = UserModel.getInstance();
        this.courseModel = CourseModel.getInstance();
        this.lessonModel = LessonModel.getInstance();

//      load lists from backend
//        studentModel.loadAllStudents();
        //  courseModel.loadAllCourses(user.getId());
        //  
        setUser();
        courseModel.loadAllCourses(user.getId());
        setCoursesIntoComboBox();

        setTableViewsForCourseOverview();
        setTotalStudentLabel();
        setPresentStudentLabel();
        listenToCourseSelection();
        setSecondTableView();

        comboBoxCourses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Course>() {
            @Override
            public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
                comboBoxCourses1.setValue(newValue);

            }
        });
    }

    private void setTotalStudentLabel() {
        lblTotalOfStudents.textProperty().bind(Bindings.convert(studentModel.enrolledStudentsLabelProperty()));
    }

    private void setPresentStudentLabel() {
        lblStudentsPresent.textProperty().bind(Bindings.convert(studentModel.getAttendanceCountProperty()));
        studentModel.startObserving(comboBoxCourses.getSelectionModel().getSelectedItem());
    }

    private void setTableViewsForCourseOverview() {

        studentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        absence.setCellValueFactory(new PropertyValueFactory<>("absencePercentage"));
        lessonCount.setCellValueFactory(new PropertyValueFactory<>("absenceCount"));

        // TODO: change the method to using current date LATER.
        studentModel.loadAllStudents(comboBoxCourses.getSelectionModel().getSelectedItem(), LocalDateTime.parse("2020-03-09T14:29:00"));
        tbvStudentAbsence.setItems(studentModel.getObsStudents());
    }

    private void setSecondTableView() {

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("statusType"));

        // TODO: change the method to using current date LATER.
    }

    private void listenToCourseSelection() {
        comboBoxCourses.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal)
                -> {
            studentModel.loadAllStudents(newVal, LocalDateTime.parse("2020-03-09T14:29:00"));
            studentModel.startObserving(newVal);
        });
    }

    private void setCoursesIntoComboBox() {
        comboBoxCourses.getItems().clear();
        comboBoxCourses.getItems().addAll(courseModel.getObsCourses());
        comboBoxCourses.getSelectionModel().select(user.getCurrentSelectedCourse());

    }

    @FXML
    private void getSelectedStudent(MouseEvent event) {
        Student selectedStudent = tbvStudentAbsence.getSelectionModel().getSelectedItem();
        boolean isNotNull = dataChange(selectedStudent);

        if (isNotNull) {
            selectFirstCourse();
            loadInSecondTableViewData(selectedStudent, comboBoxCourses1.getSelectionModel().getSelectedItem());
        }
    }

    private void selectFirstCourse() {
        comboBoxCourses1.getItems().clear();
        courseModel.loadAllCourses(tbvStudentAbsence.getSelectionModel().getSelectedItem().getId());
        comboBoxCourses1.getItems().addAll(courseModel.getObsCourses());
        comboBoxCourses1.getSelectionModel().select(0);
    }

    private boolean dataChange(Student selectedStudent) {

        if (selectedStudent != null) {
            lblstudentname.setText(selectedStudent.getName());
            return true;

        }
        return false;
    }

    private void loadInSecondTableViewData(Student stud, Course curs) {
        lessonModel.loadAllStudenLessons(stud.getId(), curs.getId());
        secondTableView.setItems(lessonModel.getObsLessons());
    }

    private void setUser() {
        try {
            this.user = userModel.getCurrentUser();
        } catch (ModelException ex) {
            Logger.getLogger(TeacherDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void changeCourse(ActionEvent event) {
        Student selectedStudent = tbvStudentAbsence.getSelectionModel().getSelectedItem();
        boolean isNotNull = dataChange(selectedStudent);
        if (isNotNull) {
            loadInSecondTableViewData(selectedStudent, comboBoxCourses1.getSelectionModel().getSelectedItem());
        }
    }

}
