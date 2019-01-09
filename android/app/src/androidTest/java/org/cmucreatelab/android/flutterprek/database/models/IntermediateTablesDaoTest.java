package org.cmucreatelab.android.flutterprek.database.models;

import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkillDAO;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.emotion.EmotionDAO;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.EmotionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.IntermediateTablesDAO;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.SessionDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/18/2018.
 *
 * EmotionDaoTest
 *
 * Unit tests for the emotions table.
 */
@RunWith(AndroidJUnit4.class)
public class IntermediateTablesDaoTest extends DaoTest {

    private IntermediateTablesDAO intermediateTablesDAO;
    private SessionDAO sessionDAO;
    private EmotionDAO emotionDAO;
    private CopingSkillDAO copingSkillDAO;

    private static final String test_uuid = "test1";
    private static final String emotion_uuid1 = "emotion1";
    private static final String coping_skill_uuid1 = "copingskill1";
    private static final String student_uuid1 = "student1";
    private static final String session_uuid1 = "session1";


    @Override
    public void initializeDaos() {
        intermediateTablesDAO = getDb().intermediateTablesDAO();
        sessionDAO = getDb().sessionDAO();
        emotionDAO = getDb().emotionDAO();
        copingSkillDAO = getDb().copingSkillDAO();
    }


    private void populateDbWithEmotions() {
        String[][] data = new String[][]{
                {"emotion1", "happy"},
                {"emotion2", "sad"},
                {"emotion3", "mad"},
        };
        for (int i=0; i<data.length; i++) {
            Emotion emotion = new Emotion(data[i][0], data[i][1]);
            emotionDAO.insert(emotion);
        }
    }


    private void populateDbWithCopingSkills() {
        String[][] data = new String[][]{
                {"copingskill1", "think about it"},
                {"copingskill2", "try it out"},
                {"copingskill3", "relax"},
        };
        for (int i=0; i<data.length; i++) {
            CopingSkill copingSkill = new CopingSkill(data[i][0], data[i][1]);
            copingSkillDAO.insert(copingSkill);
        }
    }


    private void populateDbForEmotionsCopingSkills() {
        populateDbWithEmotions();
        populateDbWithCopingSkills();
        // intermediate table
        String[][] data = new String[][]{
                {"sadthinkaboutit", "emotion2", "copingskill1"},
                {"sadtryitout", "emotion2", "copingskill2"},
                {"madrelax", "emotion3", "copingskill3"},
        };
        for (int i=0; i<data.length; i++) {
            EmotionCopingSkill emotionCopingSkill = new EmotionCopingSkill(data[i][0], data[i][1], data[i][2]);
            intermediateTablesDAO.insert(emotionCopingSkill);
        }
    }


    private void populateDb() {
        // TODO set up our database for queries on it
    }


    private void createEmotionAndCopingSkillForTest() {
        Emotion emotion = new Emotion(emotion_uuid1, "Emotional");
        emotionDAO.insert(emotion);
        CopingSkill copingSkill = new CopingSkill(coping_skill_uuid1, "Think About It");
        copingSkillDAO.insert(copingSkill);
    }


    @Test
    public void insertAndDeleteEmotionCopingSkill() {
        createEmotionAndCopingSkillForTest();
        EmotionCopingSkill emotionCopingSkill = new EmotionCopingSkill(test_uuid, emotion_uuid1, coping_skill_uuid1);
        intermediateTablesDAO.insert(emotionCopingSkill);
        intermediateTablesDAO.delete(emotionCopingSkill);
    }


    @Test
    public void insertAndDeleteItineraryItem() {
        ItineraryItem itineraryItem = new ItineraryItem(test_uuid, student_uuid1, 1, "capability", "");
        intermediateTablesDAO.insert(itineraryItem);
        intermediateTablesDAO.delete(itineraryItem);
    }


    @Test
    public void insertAndDeleteSessionCopingSkill() {
        SessionCopingSkill sessionCopingSkill = new SessionCopingSkill(test_uuid, session_uuid1, coping_skill_uuid1, new Date());
        intermediateTablesDAO.insert(sessionCopingSkill);
        intermediateTablesDAO.delete(sessionCopingSkill);
    }


    @Test
    public void getCopingSkillsForEmotion() throws InterruptedException {
        populateDbForEmotionsCopingSkills();

        assertEquals(0, LiveDataTestUtil.getValue(copingSkillDAO.getCopingSkillsForEmotion("emotion1")).size());
        assertEquals(2, LiveDataTestUtil.getValue(copingSkillDAO.getCopingSkillsForEmotion("emotion2")).size());
        assertEquals(1, LiveDataTestUtil.getValue(copingSkillDAO.getCopingSkillsForEmotion("emotion3")).size());
    }

}
