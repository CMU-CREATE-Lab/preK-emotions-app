package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.emotion.EmotionDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/18/2018.
 *
 * EmotionDaoTest
 *
 * Unit tests for the emotions table.
 */
@RunWith(AndroidJUnit4.class)
public class EmotionDaoTest extends DaoTest {

    private EmotionDAO emotionDAO;

    private static final String owner_uuid= "owner1";


    @Override
    public void initializeDaos() {
        emotionDAO = getDb().emotionDAO();
    }


    private void populateDb() {
        String[][] data = new String[][]{
                {"uuid_01", "happy", null},
                {"uuid_02", "sad", null},
                {"uuid_03", "happy", owner_uuid},
        };
        for (int i=0; i<data.length; i++) {
            Emotion emotion = new Emotion(data[i][0], data[i][1]);
            emotion.setOwnerUuid(data[i][2]);
            emotionDAO.insert(emotion);
        }
    }


    // TODO test imageFileUuid


    @Test
    public void insertAndGetAndDeleteEmotion() throws InterruptedException {
        String uuid = "emotion1";
        Emotion emotion = new Emotion(uuid, "happy");
        emotion.setOwnerUuid(owner_uuid);
        emotion.setImageFileUuid("image_uuid_01");
        emotionDAO.insert(emotion);

        Emotion dbEmotion = LiveDataTestUtil.getValue(emotionDAO.getEmotion(uuid));
        assertEquals(emotion.getUuid(), dbEmotion.getUuid());
        assertEquals(emotion.getName(), dbEmotion.getName());
        assertEquals(emotion.getOwnerUuid(), dbEmotion.getOwnerUuid());
        assertEquals(emotion.getImageFileUuid(), dbEmotion.getImageFileUuid());

        emotionDAO.delete(emotion);
    }


    @Test
    public void getEmotionsOwnedByOwnerOrNull() throws InterruptedException {
        populateDb();
        assertEquals(1, LiveDataTestUtil.getValue(emotionDAO.getEmotionsOwnedBy(owner_uuid)).size());
        assertEquals(2, LiveDataTestUtil.getValue(emotionDAO.getEmotionsOwnedByNoOne()).size());
        ArrayList<String> list = new ArrayList<>();
        list.add(owner_uuid);
        assertEquals(3, LiveDataTestUtil.getValue(emotionDAO.getEmotionsOwnedBy(list)).size());
    }


    @Test
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "emotion1";
        emotionDAO.insert(new Emotion(uuid, "happy"));
        emotionDAO.insert(new Emotion(uuid, "sad"));
    }

}
