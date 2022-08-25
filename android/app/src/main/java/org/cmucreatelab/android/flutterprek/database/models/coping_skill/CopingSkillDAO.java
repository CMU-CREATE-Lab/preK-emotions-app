package org.cmucreatelab.android.flutterprek.database.models.coping_skill;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;

import java.util.List;

/**
 * Created by tasota on 10/17/2018.
 *
 * CopingSkillDAO
 *
 * Data access object for {@link CopingSkill}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface CopingSkillDAO {

    @Insert
    void insert(CopingSkill copingSkill);

    @Insert
    void insert(List<CopingSkill> copingSkills);

    @Delete
    void delete(CopingSkill copingSkill);

    @Query("SELECT * from coping_skills ORDER BY name ASC")
    LiveData<List<CopingSkill>> getAllCopingSkills();

    @Query("SELECT * from coping_skills ORDER BY name ASC")
    LiveData<List<CopingSkillWithCustomizations>> getAllCopingSkillsWithCustomizations();

    @Query("SELECT * FROM coping_skills WHERE uuid = :copingSkillUuid LIMIT 1")
    LiveData<CopingSkill> getCopingSkill(String copingSkillUuid);

    @Query("SELECT coping_skills.* from coping_skills " +
            "INNER JOIN emotions_coping_skills ON (emotions_coping_skills.coping_skill_uuid = coping_skills.uuid) " +
            "WHERE emotions_coping_skills.emotion_uuid = :emotionUuid AND emotions_coping_skills.owner_uuid IS NULL ORDER BY emotions_coping_skills.sequence_id ASC, name ASC")
    LiveData<List<CopingSkill>> getCopingSkillsForEmotion(String emotionUuid);

    @Query("SELECT coping_skills.* from coping_skills " +
            "INNER JOIN emotions_coping_skills ON (emotions_coping_skills.coping_skill_uuid = coping_skills.uuid) " +
            "LEFT JOIN customizations ON (customizations.owner_uuid = coping_skills.uuid AND customizations.`key` = 'showAfterFirst') " +
            "WHERE emotions_coping_skills.emotion_uuid = :emotionUuid AND emotions_coping_skills.owner_uuid IS NULL AND customizations.`key` IS NULL ORDER BY emotions_coping_skills.sequence_id ASC, name ASC")
    LiveData<List<CopingSkill>> getCopingSkillsForEmotionOnFirst(String emotionUuid);

    @Query("SELECT coping_skills.* from coping_skills " +
            "INNER JOIN emotions_coping_skills ON (emotions_coping_skills.coping_skill_uuid = coping_skills.uuid) " +
            "WHERE emotions_coping_skills.emotion_uuid = :emotionUuid AND (emotions_coping_skills.owner_uuid IS NULL OR emotions_coping_skills.owner_uuid = :ownerUuid) ORDER BY emotions_coping_skills.sequence_id ASC, name ASC")
    LiveData<List<CopingSkill>> getCopingSkillsForEmotion(String ownerUuid, String emotionUuid);

    @Query("SELECT coping_skills.* from coping_skills " +
            "INNER JOIN emotions_coping_skills ON (emotions_coping_skills.coping_skill_uuid = coping_skills.uuid) " +
            "LEFT JOIN customizations ON (customizations.owner_uuid = coping_skills.uuid AND customizations.`key` = 'showAfterFirst') " +
            "WHERE emotions_coping_skills.emotion_uuid = :emotionUuid AND (emotions_coping_skills.owner_uuid IS NULL OR emotions_coping_skills.owner_uuid = :ownerUuid) AND customizations.`key` IS NULL ORDER BY emotions_coping_skills.sequence_id ASC, name ASC")
    LiveData<List<CopingSkill>> getCopingSkillsForEmotionOnFirst(String ownerUuid, String emotionUuid);

}
