package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

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
    @Delete
    void delete(EmotionCopingSkill emotionCopingSkill);

    @Insert
    void insert(ItineraryItem itineraryItem);
    @Delete
    void delete(ItineraryItem itineraryItem);

    @Insert
    void insert(SessionCopingSkill sessionCopingSkill);
    @Delete
    void delete(SessionCopingSkill sessionCopingSkill);

}
