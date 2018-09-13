package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.models.Student;
import org.cmucreatelab.android.flutterprek.database.models.StudentDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 9/13/2018.
 *
 * StudentDaoTest
 *
 * Unit tests for the students table.
 */
@RunWith(AndroidJUnit4.class)
public class StudentDaoTest {

    private StudentDAO studentDAO;
    private AppDatabase db;

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule public final ExpectedException expectedException = ExpectedException.none();


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        studentDAO = db.studentDAO();
    }


    @After
    public void closeDb() {
        db.close();
    }


    @Test
    public void insertAndGetStudent() throws InterruptedException {
        Student student = new Student("Alice");
        student.setNotes("Has a secret");
        studentDAO.insert(student);

        List<Student> allStudents = LiveDataTestUtil.getValue(studentDAO.getAllStudents());
        assertEquals(allStudents.get(0).getUuid(), student.getUuid());
        assertEquals(allStudents.get(0).getName(), student.getName());
        assertEquals(allStudents.get(0).getNotes(), student.getNotes());
    }


    @Test
    public void getAllStudents() throws InterruptedException {
        Student student1 = new Student("Alice");
        studentDAO.insert(student1);
        Student student2 = new Student("Bob");
        studentDAO.insert(student2);

        List<Student> allStudents = LiveDataTestUtil.getValue(studentDAO.getAllStudents());
        assertEquals(allStudents.get(0).getUuid(), student1.getUuid());
        assertEquals(allStudents.get(1).getUuid(), student2.getUuid());
    }


    @Test
    public void primaryKey() throws Exception {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "student_uuid";
        Student student1 = new Student(uuid,"Alice");
        studentDAO.insert(student1);
        Student student2 = new Student(uuid,"Bob");
        studentDAO.insert(student2);
    }

}
