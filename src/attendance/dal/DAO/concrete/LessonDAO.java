package attendance.dal.DAO.concrete;

import attendance.dal.DAO.interfaces.ILessonDAO;
import attendance.dal.DAO.interfaces.ICourseDAO;
import attendance.dal.DAO.concrete.CourseDAO;
import attendance.be.Course;
import attendance.be.Lesson;
import attendance.be.User;
import attendance.dal.DBConnectionProvider;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author annem
 */
public class LessonDAO implements ILessonDAO {

    private final DBConnectionProvider connection;
    private final ICourseDAO courseDAO;

    public LessonDAO() {
        connection = new DBConnectionProvider();
        courseDAO = new CourseDAO();
    }

    @Override
    public List<Lesson> getLessonsForToday(User student, LocalDate current) {
        List<Lesson> lessons = new ArrayList<>();

        String sql = "SELECT CC.id, C.name, CC.startTime, CC.endTime "
                + "FROM CourseCalendar CC "
                + "JOIN Course C "
                + "ON CC.courseId = C.id "
                + "WHERE ";

        List<Course> courses = courseDAO.getCourses(student);

        String sqlFinal = preparedStatement(sql, courses);

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sqlFinal);
            int i = 0;
            for (Course c : courses) {
                pstmt.setInt(i + 1, c.getId());
                i++;
            }
            pstmt.setDate(i + 1, Date.valueOf(current));
            pstmt.setDate(i + 2, Date.valueOf(current.plusDays(1)));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String courseName = rs.getString("name");
                LocalDateTime start = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("endTime").toLocalDateTime();
                String status = checkStatus(student, id);
                if (status.contains("PRESENT")) {
                    lessons.add(new Lesson(id, courseName, start, end, Lesson.StatusType.PRESENT));
                } else if (status.contains("ABSENT")) {
                    lessons.add(new Lesson(id, courseName, start, end, Lesson.StatusType.ABSENT));
                } else {
                    lessons.add(new Lesson(id, courseName, start, end, Lesson.StatusType.UNREGISTERED));
                }
            }
            return lessons;
        } catch (SQLServerException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Builds the conditions of the SQL PreparedStatement for
     * getLessonsForToday().
     *
     * @param sql The SQL PreparedStatement.
     * @param courses The list of courses.
     * @return The conditions for the SQL.
     */
    private String preparedStatement(String sql, List<Course> courses) {
        boolean firstItem = true;
        for (Course c : courses) {
            if (firstItem) {
                sql += "(CC.courseId = ? ";
                firstItem = false;
            } else {
                sql += " OR CC.courseId = ?";
            }
        }
        sql += ") AND (CC.startTime >= ? AND CC.startTime < ?)";
        return sql;
    }

    /**
     * Checks the status of the lessons for the current day.
     *
     * @param user The user.
     * @param lessonId The id of the lesson.
     * @return
     */
    private String checkStatus(User user, int lessonId) {
        String sql = "SELECT status "
                + "FROM AttendanceRecord "
                + "WHERE userId = ? AND courseCalendarId = ?;";
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, lessonId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString(1);
                return status;
            }
        } catch (SQLServerException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String status = "UNREGISTERED";
        return status;
    }

    @Override
    public int getNumberOfConductedLessons(Course course, LocalDateTime current) {
        String sql = "SELECT COUNT(id) FROM CourseCalendar WHERE courseId = ? AND endTime <= ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, course.getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(current));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count;
            }
        } catch (SQLServerException ex) {
            Logger.getLogger(LessonDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LessonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
