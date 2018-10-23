package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomDAO;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.database.models.student.StudentDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 9/13/2018.
 *
 * StudentDaoTest
 *
 * Unit tests for the students table.
 */
@RunWith(AndroidJUnit4.class)
public class StudentDaoTest extends DaoTest {

    private StudentDAO studentDAO;
    private ClassroomDAO classroomDAO;


    @Override
    public void initializeDaos() {
        classroomDAO = getDb().classroomDAO();
        studentDAO = getDb().studentDAO();
    }


    @Test
    public void insertAndGetAndDeleteStudent() throws InterruptedException {
        Classroom classroom = insertAndGetClassroomForTest();
        String uuid = UUID.randomUUID().toString();
        Student student = new Student(uuid, "Alice", classroom.getUuid());
        student.setNotes("Has a secret");
        studentDAO.insert(student);

        Student dbStudent = LiveDataTestUtil.getValue(studentDAO.getStudent(uuid));
        assertEquals(dbStudent.getUuid(), student.getUuid());
        assertEquals(dbStudent.getName(), student.getName());
        assertEquals(dbStudent.getNotes(), student.getNotes());

        studentDAO.delete(student);
    }


    @Test
    public void getAllStudents() throws InterruptedException {
        Classroom classroom = insertAndGetClassroomForTest();
        Student student1 = new Student("Alice", classroom.getUuid());
        studentDAO.insert(student1);
        Student student2 = new Student("Bob", classroom.getUuid());
        studentDAO.insert(student2);

        List<Student> allStudents = LiveDataTestUtil.getValue(studentDAO.getAllStudents());
        assertEquals(allStudents.get(0).getUuid(), student1.getUuid());
        assertEquals(allStudents.get(1).getUuid(), student2.getUuid());
    }


    @Test
    public void primaryKey() throws Exception {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        Classroom classroom = insertAndGetClassroomForTest();
        String uuid = "student_uuid";
        Student student1 = new Student(uuid,"Alice", classroom.getUuid());
        studentDAO.insert(student1);
        Student student2 = new Student(uuid,"Bob", classroom.getUuid());
        studentDAO.insert(student2);
    }


    private Classroom insertAndGetClassroomForTest() {
        Classroom classroom = new Classroom("classroom_1","Test Classroom");
        classroomDAO.insert(classroom);
        return classroom;
    }

}
