package org.cmucreatelab.android.flutterprek.database.models.emotion;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tasota on 10/18/2018.
 *
 * EmotionDAO
 *
 * Data access object for {@link Emotion}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface EmotionDAO {

    @Insert
    void insert(Emotion emotion);

    @Delete
    void delete(Emotion emotion);

    @Query("SELECT * FROM emotions WHERE uuid = :emotionUuid LIMIT 1")
    LiveData<Emotion> getEmotion(String emotionUuid);

    @Query("SELECT * FROM emotions WHERE owner_uuid = :ownerUuid")
    LiveData<List<Emotion>> getEmotionsOwnedBy(String ownerUuid);

    @Query("SELECT * FROM emotions WHERE owner_uuid IS NULL")
    LiveData<List<Emotion>> getEmotionsOwnedByNoOne();

}
