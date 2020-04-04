package attendance.dal.DAO;

import attendance.be.Course;
import attendance.be.Lesson;
import attendance.be.Student;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IStudentDAO {

    /**
     * Records the attendance of a lesson for a student in the database.
     *
     * @param userId The id of the student.
     * @param lesson The lesson to have attendance recorded.
     */
    void createRecord(int userId, Lesson lesson);

    /**
     * Gets the number of absent lessons for each student for a course.
     *
     * @param course The selected course.
     * @return The list of students with absent lesson count.
     */
    List<Student> getNumberOfAbsentLessons(Course course);

    /**
     * Gets the attendance records for all the courses a student is enrolled in.
     *
     * @param userId The id of the student.
     * @return A list of attendance records (Lesson objects with status).
     */
    List<Lesson> getAttendanceRecordsForAllCourses(int userId);

    /**
     * Gets all the attendance records of a student for a course.
     *
     * @param userId The id of the student.
     * @param courseId The id of the course.
     * @return A list of attendance records (Lesson objects with status).
     */
    List<Lesson> getAttendanceRecordsForACourse(int userId, int courseId);
}
