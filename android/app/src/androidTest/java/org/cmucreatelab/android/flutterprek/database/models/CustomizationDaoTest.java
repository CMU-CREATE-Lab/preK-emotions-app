package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.customization.CustomizationDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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


    @Override
    public void initializeDaos() {
        customizationDAO = getDb().customizationDAO();
    }


    @Before
    public void populateDb() {
        String[][] data = new String[][]{
                {"uuid_01", "foo", "bar", null, null},
                {"uuid_02", "foo", "bar", null, null},
                {"uuid_03", "foo", "bar", "classroom_1", null},
                {"uuid_04", "foo", "bar", "classroom_1", "customization_01"},
                {"uuid_05", "foo", "bar", "classroom_2", "customization_01"},
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
    public void insertAndGetCustomization() throws InterruptedException {
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
    }


    @Test
    public void getCustomizationsOwnedBy() throws InterruptedException {
        List<Customization> ownedBy1 = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy("classroom_1"));
        List<Customization> ownedBy2 = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy("classroom_2"));
        List<Customization> ownedBy3 = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedBy("classroom_3"));

        assertEquals(2, ownedBy1.size());
        assertEquals(1, ownedBy2.size());
        assertEquals(0, ownedBy3.size());
    }


    @Test
    public void getCustomizationsBasedOn() throws InterruptedException {
        List<Customization> basedOnList = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsBasedOn("customization_01"));

        assertEquals(2, basedOnList.size());
    }


    @Test
    public void getCustomizationsOwnedByNoOneAndBasedOnNothing() throws InterruptedException {
        List<Customization> ownedByNoOne = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsOwnedByNoOne());
        List<Customization> basedOnNothing = LiveDataTestUtil.getValue(customizationDAO.getCustomizationsBasedOnNothing());

        assertEquals(2, ownedByNoOne.size());
        assertEquals(3, basedOnNothing.size());
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
