package org.cmucreatelab.android.flutterprek.database.models;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.LiveDataTestUtil;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkillDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tasota on 10/17/2018.
 *
 * CopingSkillDaoTest
 *
 * Unit tests for the coping_skills table.
 */
@RunWith(AndroidJUnit4.class)
public class CopingSkillDaoTest extends DaoTest {

    private CopingSkillDAO copingSkillDAO;


    @Override
    public void initializeDaos() {
        copingSkillDAO = getDb().copingSkillDAO();
    }


    // TODO tests for ownerUuid, imageFileUuid


    @Test
    public void insertAndGetCopingSkill() throws InterruptedException {
        String uuid = "cs1";

        CopingSkill copingSkill = new CopingSkill(uuid,"Try it out");
        copingSkill.setOwnerUuid("owner1");
        copingSkill.setImageFileUuid("img1");
        copingSkillDAO.insert(copingSkill);

        CopingSkill dbCopingSkill = LiveDataTestUtil.getValue(copingSkillDAO.getCopingSkill(uuid));
        assertEquals(copingSkill.getUuid(), dbCopingSkill.getUuid());
        assertEquals(copingSkill.getName(), dbCopingSkill.getName());
        assertEquals(copingSkill.getOwnerUuid(), dbCopingSkill.getOwnerUuid());
        assertEquals(copingSkill.getImageFileUuid(), dbCopingSkill.getImageFileUuid());
    }


    @Test
    public void getAllCopingSkills() throws InterruptedException {
        copingSkillDAO.insert(new CopingSkill("Think about it"));
        copingSkillDAO.insert(new CopingSkill(""));
        List<CopingSkill> allCopingSkills = LiveDataTestUtil.getValue(copingSkillDAO.getAllCopingSkills());
        assertEquals(2, allCopingSkills.size());
    }


    @Test
    public void primaryKey() {
        // An exception that indicates that an integrity constraint was violated.
        expectedException.expect(SQLiteConstraintException.class);

        String uuid = "cs1";
        copingSkillDAO.insert(new CopingSkill(uuid, "Think about it"));
        copingSkillDAO.insert(new CopingSkill(uuid, "Try it out"));
    }

}
