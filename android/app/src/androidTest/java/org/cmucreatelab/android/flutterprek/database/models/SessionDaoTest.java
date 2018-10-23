package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.session.SessionDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/18/2018.
 *
 * SessionDaoTest
 *
 * Unit tests for the sessions table.
 */
@RunWith(AndroidJUnit4.class)
public class SessionDaoTest extends DaoTest {

    private SessionDAO sessionDAO;

    private static final String student_uuid1 = "student1";
    private static final String student_uuid2 = "student2";
    private static final String emotion_uuid = "emotion1";


    @Override
    public void initializeDaos() {
        sessionDAO = getDb().sessionDAO();
    }


    private void populateDb() {
        Date timestamp = new Date();
        String[][] data = new String[][]{
                {"uuid_01", student_uuid1, null},
                {"uuid_02", student_uuid1, emotion_uuid},
                {"uuid_03", student_uuid2, emotion_uuid},
                {"uuid_04", student_uuid2, emotion_uuid},
        };
        for (int i=0; i<data.length; i++) {
            Session session = new Session(data[i][0], data[i][1], timestamp);
            session.setEmotionUuid(data[i][2]);
            sessionDAO.insert(session);
        }
    }


    @Test
    public void insertAndGetAndDeleteSession() throws InterruptedException {
        String uuid = "session1";
        Session session = new Session(uuid, student_uuid1, new Date());
        session.setEndedAt(new Date());
        session.setEmotionUuid(emotion_uuid);
        sessionDAO.insert(session);

        Session dbSession = LiveDataTestUtil.getValue(sessionDAO.getSession(uuid));
        assertEquals(session.getUuid(), dbSession.getUuid());
        assertEquals(session.getStudentUuid(), dbSession.getStudentUuid());
        assertEquals(session.getStartedAt(), dbSession.getStartedAt());
        assertEquals(session.getEndedAt(), dbSession.getEndedAt());
        assertEquals(session.getEmotionUuid(), dbSession.getEmotionUuid());

        sessionDAO.delete(session);
    }


    @Test
    public void getAllSessions() throws InterruptedException {
        populateDb();
        assertEquals(4, LiveDataTestUtil.getValue(sessionDAO.getAllSessions()).size());
    }


    @Test
    public void getSessionsFromOneOrMultipleStudents() throws InterruptedException {
        populateDb();
        List<String> list = new ArrayList<>();
        assertEquals(0, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromStudents(list)).size());
        list.add(student_uuid1);
        assertEquals(2, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromStudents(list)).size());
        list.add(student_uuid2);
        assertEquals(4, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromStudents(list)).size());
    }


    @Test
    public void getSessionsFromEmotions() throws InterruptedException {
        populateDb();
        List<String> list = new ArrayList<>();
        assertEquals(0, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromEmotions(list)).size());
        list.add(student_uuid1);
        assertEquals(0, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromEmotions(list)).size());
        assertEquals(1, LiveDataTestUtil.getValue(sessionDAO.getSessionsWithoutEmotions()).size());
        list.add(emotion_uuid);
        assertEquals(3, LiveDataTestUtil.getValue(sessionDAO.getSessionsFromEmotions(list)).size());
    }


    @Test
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "session1";
        sessionDAO.insert(new Session(uuid, student_uuid1, new Date()));
        sessionDAO.insert(new Session(uuid, student_uuid2, new Date()));
    }

}
