package org.cmucreatelab.android.flutterprek.database.models.emotion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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

    @Insert
    void insert(List<Emotion> emotions);

    @Delete
    void delete(Emotion emotion);

    @Query("SELECT * FROM emotions")
    LiveData<List<Emotion>> getAllEmotions();

    @Query("SELECT * FROM emotions WHERE uuid = :emotionUuid LIMIT 1")
    LiveData<Emotion> getEmotion(String emotionUuid);

    @Query("SELECT * FROM emotions WHERE owner_uuid = :ownerUuid")
    LiveData<List<Emotion>> getEmotionsOwnedBy(String ownerUuid);

    @Query("SELECT * FROM emotions WHERE owner_uuid IS NULL")
    LiveData<List<Emotion>> getEmotionsOwnedByNoOne();

    @Query("SELECT * FROM emotions WHERE owner_uuid IN (:ownerUuids) OR owner_uuid IS NULL")
    LiveData<List<Emotion>> getEmotionsOwnedBy(List<String> ownerUuids);

}



