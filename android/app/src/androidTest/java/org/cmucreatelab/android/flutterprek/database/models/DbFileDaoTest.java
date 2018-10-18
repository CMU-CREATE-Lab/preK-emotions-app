package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFileDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/18/2018.
 *
 * DbFileDaoTest
 *
 * Unit tests for the db_files table.
 */
@RunWith(AndroidJUnit4.class)
public class DbFileDaoTest extends DaoTest {

    private DbFileDAO dbFileDAO;

    private static final String filetype_image = "image";
    private static final String filetype_audio = "audio";


    @Override
    public void initializeDaos() {
        dbFileDAO = getDb().dbFileDAO();
    }


    private void populateDb() {
        String[][] data = new String[][]{
                {"uuid_01", filetype_image, "/res/image1.jpg"},
                {"uuid_02", filetype_image, "/res/image2.jpg"},
                {"uuid_03", filetype_audio, "/res/audio1.mp3"},
        };
        for (int i=0; i<data.length; i++) {
            dbFileDAO.insert(new DbFile(data[i][0], data[i][1], data[i][2]));
        }
    }


    @Test
    public void insertAndGetDbFile() throws InterruptedException {
        String uuid = "file1";
        DbFile dbFileObject = new DbFile(uuid, filetype_image, "/res/filename.png");
        dbFileDAO.insert(dbFileObject);

        DbFile dbFile = LiveDataTestUtil.getValue(dbFileDAO.getDbFile(uuid));
        assertEquals(dbFile.getUuid(), dbFileObject.getUuid());
        assertEquals(dbFile.getFileType(), dbFileObject.getFileType());
        assertEquals(dbFile.getFilePath(), dbFileObject.getFilePath());
    }


    @Test
    public void getAllDbFiles() throws InterruptedException {
        populateDb();
        assertEquals(3, LiveDataTestUtil.getValue(dbFileDAO.getAllDbFiles()).size());
    }


    @Test
    public void getDbFilesOfType() throws InterruptedException {
        populateDb();
        assertEquals(2, LiveDataTestUtil.getValue(dbFileDAO.getDbFilesOfType(filetype_image)).size());
        assertEquals(1, LiveDataTestUtil.getValue(dbFileDAO.getDbFilesOfType(filetype_audio)).size());
    }


    @Test
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "file1";
        dbFileDAO.insert(new DbFile(uuid, "1", "one"));
        dbFileDAO.insert(new DbFile(uuid, "2", "two"));
    }

}
