package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizationsAndEmotions;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

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

    @Query("SELECT itinerary_items.* FROM itinerary_items " +
            "WHERE itinerary_items.owner_uuid = :copingSkillUuid " +
            "AND itinerary_items.capability_id NOT LIKE 'post_coping_skill_heart_beating' " +
            "ORDER BY sequence_id ASC")
    LiveData<List<ItineraryItem>> getItineraryItemsForCopingSkillWithoutHeartBeatPrompt(String copingSkillUuid);


    // TODO refactor old methods (move out to embedded DAO)

    @Transaction
    @Query("SELECT * FROM students WHERE uuid IN (:studentUuids)")
    LiveData<List<StudentWithSessionsAndSessionCopingSkills>> getSessionsWithSessionCopingSkillsFromStudents(List<String> studentUuids);
//    @Query("SELECT * FROM sessions WHERE student_uuid IN (:studentUuids) ORDER BY started_at DESC")
//    LiveData<List<Session>> getSessionsFromStudents(List<String> studentUuids);

    // TODO do a named query with specific customization? (getStudentWith ... CustomEmotionImageFiles) (requires default @Transaction like below)
    @Transaction
    @Query("SELECT * FROM students WHERE uuid = :studentUuid LIMIT 1")
    StudentWithCustomizations getStudentWithCustomizations(String studentUuid);

    // NOTE: probably should be using method "getEmotionsOwnedBy" -- see example in ChooseEmotionAbstractActivity with classroom and student uuids
    @Query("SELECT * FROM emotions")
    List<Emotion> getAllEmotions();

    @Transaction
    default StudentWithCustomizationsAndEmotions getStudentWithEmotionsAndCustomImageFiles(String studentUuid) {
        StudentWithCustomizations student = getStudentWithCustomizations(studentUuid);
        List<Emotion> emotions = getAllEmotions();
        return new StudentWithCustomizationsAndEmotions(student, emotions);
    }

}



