package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.customization.CustomizationDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/17/2018.
 *
 * CustomizationDaoTest
 *
 * Unit tests for the customizations table.
 */
@RunWith(AndroidJUnit4.class)
public class CustomizationDaoTest extends DaoTest {

    private CustomizationDAO customizationDAO;

    private static final String customization_uuid1 = "customization_01";
    private static final String customization_uuid2 = "customization_02";
    private static final String classroom_uuid1 = "classroom_1";
    private static final String classroom_uuid2 = "classroom_2";
    private static final String classroom_uuid3 = "classroom_3";


    @Override
    public void initializeDaos() {
        customizationDAO = getDb().customizationDAO();
    }


    private void populateDb() {
        String[][] data = new String[][]{
                {"uuid_01", "foo", "bar", null, null},
                {"uuid_02", "foo", "bar", null, null},
                {"uuid_03", "foo", "bar", classroom_uuid1, null},
                {"uuid_04", "foo", "bar", classroom_uuid1, customization_uuid1},
                {"uuid_05", "foo", "bar", classroom_uuid2, customization_uuid1},
        };
        for (int i=0; i<data.length; i++) {
            Customization customization = new Customization(data[i][0], data[i][1], data[i][2]);
            customization.setOwnerUuid(data[i][3]);
            customization.setBasedOnUuid(data[i][4]);
            customizationDAO.insert(customization);
        }
    }


    // TODO tests for ownerUuid, basedOnUuid


    @Test
    public void insertAndGetAndDeleteCustomization() throws InterruptedException {
        String uuid = "customization1";
        Customization customization = new Customization(uuid, "foo", "bar");
        customization.setBasedOnUuid("basedon1");
        customizationDAO.insert(customization);

        Customization dbCustomization = LiveDataTestUtil.getValue(customizationDAO.getCustomization(uuid));
        assertEquals(customization.getBasedOnUuid(), dbCustomization.getBasedOnUuid());
        assertEquals(customization.getKey(), dbCustomization.getKey());
        assertEquals(customization.getOwnerUuid(), dbCustomization.getOwnerUuid());
        assertEquals(customization.getUuid(), dbCustomization.getUuid());
        assertEquals(customization.getValue(), dbCustomization.getValue());

        customizationDAO.delete(customization);
    }


    @Test
    public void getCustomizationsOwnedBy() throws InterruptedException {
        populateDb();

        assertEquals(2, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy(classroom_uuid1)).size());
        assertEquals(1, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy(classroom_uuid2)).size());
        assertEquals(0, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy(classroom_uuid3)).size());
    }


    @Test
    public void getCustomizationsBasedOn() throws InterruptedException {
        populateDb();

        assertEquals(2, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsBasedOn(customization_uuid1)).size());
        assertEquals(0, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsBasedOn(customization_uuid2)).size());
    }


    @Test
    public void getCustomizationsOwnedByNoOneAndBasedOnNothing() throws InterruptedException {
        populateDb();

        assertEquals(2, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedByNoOne()).size());
        assertEquals(3, LiveDataTestUtil.getValue(customizationDAO.getCustomizationsBasedOnNothing()).size());
    }


    @Test
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "customization_1";
        customizationDAO.insert(new Customization(uuid, "1", "one"));
        customizationDAO.insert(new Customization(uuid, "2", "two"));
    }

}
