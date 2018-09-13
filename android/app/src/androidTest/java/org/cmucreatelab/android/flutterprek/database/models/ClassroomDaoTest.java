package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.InstantTaskExecutorRule;
import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 9/6/2018.
 *
 * ClassroomDaoTest
 *
 * Unit tests for the classrooms table.
 */
@RunWith(AndroidJUnit4.class)
public class ClassroomDaoTest {

    private ClassroomDAO classroomDAO;
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
        classroomDAO = db.classroomDAO();
    }


    @After
    public void closeDb() {
        db.close();
    }


    @Test
    public void insertAndGetClassroom() throws InterruptedException {
        Classroom classroom = new Classroom("Test Classroom");
        classroomDAO.insert(classroom);
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(classroomDAO.getAllClassrooms());
        assertEquals(allClassrooms.get(0).getUuid(), classroom.getUuid());
        assertEquals(allClassrooms.get(0).getName(), classroom.getName());
    }


    @Test
    public void getAllClassrooms() throws InterruptedException {
        Classroom classroom1 = new Classroom("Test Classroom");
        classroomDAO.insert(classroom1);
        Classroom classroom2 = new Classroom("Another Classroom");
        classroomDAO.insert(classroom2);

        // TODO @tasota this needs timestamps implemented (query is ordered by name for now)
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(classroomDAO.getAllClassrooms());
//        assertEquals(allClassrooms.get(0).getUuid(), classroom1.getUuid());
//        assertEquals(allClassrooms.get(1).getUuid(), classroom2.getUuid());
        assertEquals(allClassrooms.get(0).getUuid(), classroom2.getUuid());
        assertEquals(allClassrooms.get(1).getUuid(), classroom1.getUuid());
    }


    @Test
    public void primaryKey() throws Exception {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "classroom_1";
        Classroom classroom1 = new Classroom(uuid, "Test Classroom");
        classroomDAO.insert(classroom1);
        Classroom classroom2 = new Classroom(uuid, "Another Classroom");
        classroomDAO.insert(classroom2);
    }

}
