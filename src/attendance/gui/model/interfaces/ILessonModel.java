/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.gui.model.interfaces;

import attendance.be.Lesson;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public interface ILessonModel {

    void loadAllLessons(int userId, LocalDate current);

    ObservableList<Lesson> getObsLessons();

    void createRecord(int userId, Lesson lessonToInsert);
    
    void loadAllRecords(int userId);
    
    ObservableList<Lesson> getObsRecords();
    
    void filterByCourse(int userId, int courseId);
    
    int calculateAbsenceLabel(List<Lesson> list);
    
    int getAbsencePercentageLabel();
    
    void setAbsencePercentageLabel(int value);
    
    IntegerProperty absencePercentageLabelProperty();
    
    
    
}
