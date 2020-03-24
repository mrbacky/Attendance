package attendance.gui.model;

import attendance.be.Student;
import attendance.bll.LogicManager;
import attendance.dal.Mock.MockStudentDAO;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class StudentModel {

    private static StudentModel studentModel;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private final MockStudentDAO mockStudentDAO;
    private LogicManager logicManager;

    public static StudentModel getInstance() {
        if (studentModel == null) {
            studentModel = new StudentModel();
        }
        return studentModel;
    }

    private StudentModel() {
        mockStudentDAO = new MockStudentDAO();
        logicManager = new LogicManager();
        

    }

    public void loadAllStudents() {
        List<Student> allStudents = mockStudentDAO.getStudents();
                                    //logicManager.getAbsentStudents();
        studentList.clear();
        studentList.addAll(allStudents);
    }

    public ObservableList<Student> getObsStudents() {
        return studentList;
    }

}