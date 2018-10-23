package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 9/6/2018.
 *
 * ClassroomDaoTest
 *
 * Unit tests for the classrooms table.
 */
@RunWith(AndroidJUnit4.class)
public class ClassroomDaoTest extends DaoTest {

    private ClassroomDAO classroomDAO;


    @Override
    public void initializeDaos() {
        classroomDAO = getDb().classroomDAO();
    }


    @Test
    public void insertAndGetAndDeleteClassroom() throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        Classroom classroom = new Classroom(uuid, "Test Classroom");
        classroomDAO.insert(classroom);

        Classroom dbClassroom = LiveDataTestUtil.getValue(classroomDAO.getClassroom(uuid));
        assertEquals(dbClassroom.getUuid(), classroom.getUuid());
        assertEquals(dbClassroom.getName(), classroom.getName());

        classroomDAO.delete(classroom);
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
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "classroom_1";
        Classroom classroom1 = new Classroom(uuid, "Test Classroom");
        classroomDAO.insert(classroom1);
        Classroom classroom2 = new Classroom(uuid, "Another Classroom");
        classroomDAO.insert(classroom2);
    }

}
