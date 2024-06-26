package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tasota on 10/18/2018.
 *
 * IntermediateTablesDAO
 *
 * Data access object for some of the intermediate tables of {@link org.cmucreatelab.android.flutterprek.database.AppDatabase}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface IntermediateTablesDAO {

    @Insert
    void insert(EmotionCopingSkill emotionCopingSkill);
    @Insert
    void insertEmotionCopingSkillList(List<EmotionCopingSkill> emotionsCopingSkills);
    @Delete
    void delete(EmotionCopingSkill emotionCopingSkill);

    @Insert
    void insert(ItineraryItem itineraryItem);
    @Insert
    void insertItineraryItemList(List<ItineraryItem> itineraryItems);
    @Delete
    void delete(ItineraryItem itineraryItem);

    @Insert
    void insert(SessionCopingSkill sessionCopingSkill);
    @Insert
    void insertSessionCopingSkillList(List<SessionCopingSkill> sessionsCopingSkills);
    @Delete
    void delete(SessionCopingSkill sessionCopingSkill);

    @Query("SELECT * FROM emotions_coping_skills")
    LiveData<List<EmotionCopingSkill>> getAllEmotionCopingSkills();

    @Query("SELECT * FROM itinerary_items")
    LiveData<List<ItineraryItem>> getAllItineraryItems();

    @Query("SELECT * FROM sessions_coping_skills")
    LiveData<List<SessionCopingSkill>> getAllSessionCopingSkills();

    @Query("SELECT * FROM sessions_coping_skills WHERE session_uuid = :sessionUuid ORDER BY started_at ASC")
    LiveData<List<SessionCopingSkill>> getSessionCopingSkillsFromSessionUuid(String sessionUuid);

    @Query("SELECT itinerary_items.* FROM itinerary_items " +
            "WHERE itinerary_items.owner_uuid = :emotionUuid ORDER BY sequence_id ASC")
    LiveData<List<ItineraryItem>> getItineraryItemsForEmotion(String emotionUuid);

    @Query("SELECT itinerary_items.* FROM itinerary_items " +
            "WHERE itinerary_items.owner_uuid = :copingSkillUuid ORDER BY sequence_id ASC")
    LiveData<List<ItineraryItem>> getItineraryItemsForCopingSkill(String copingSkillUuid);

    @Query("SELECT itinerary_items.* FROM itinerary_items " +
            "WHERE itinerary_items.owner_uuid = :copingSkillUuid " +
            "AND itinerary_items.capability_id NOT LIKE 'post_coping_skill_%' " +
            "ORDER BY sequence_id ASC")
    LiveData<List<ItineraryItem>> getItineraryItemsForCopingSkillWithoutPostCopingSkills(String copingSkillUuid);

}
