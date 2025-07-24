package org.cmucreatelab.android.flutterprek.database.models.embedded_models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.EmotionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

/**
 * Created by tasota on 07/24/2025.
 *
 * EmbeddedDAO
 *
 * Data access object for POJOs (Plain Old Java Objects) or that use annotations such as @Embedded or @Relation. More on this concept:
 *   https://developer.android.com/training/data-storage/room/relationships/nested
 */
@Dao
public abstract class EmbeddedDAO {


    @Query("SELECT * FROM students WHERE uuid = :studentUuid LIMIT 1")
    protected abstract Student getStudent(String studentUuid);

    @Query("SELECT * FROM customizations WHERE owner_uuid = :ownerUuid AND `key` = :keyName")
    protected abstract List<Customization> getCustomizationsOwnedByForKeyName(String ownerUuid, String keyName);

    @Transaction
    protected StudentWithCustomizations getStudentWithCustomImageFiles(String studentUuid) {
        Student student = getStudent(studentUuid);
        List<Customization> customizations = getCustomizationsOwnedByForKeyName(studentUuid, "imageFileUuid");
        return new StudentWithCustomizations(student, customizations);
    }


    // NOTE: probably should be using method "getEmotionsOwnedBy" -- see example in ChooseEmotionAbstractActivity with classroom and student uuids
    @Query("SELECT * FROM emotions")
    protected abstract List<Emotion> getAllEmotions();

    @Transaction
    public StudentWithCustomizationsAndEmotions getStudentWithEmotionsAndCustomImageFiles(String studentUuid) {
        StudentWithCustomizations student = getStudentWithCustomImageFiles(studentUuid);
        List<Emotion> emotions = getAllEmotions();
        return new StudentWithCustomizationsAndEmotions(student, emotions);
    }

}



