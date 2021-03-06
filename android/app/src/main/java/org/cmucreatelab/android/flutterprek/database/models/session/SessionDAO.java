package org.cmucreatelab.android.flutterprek.database.models.session;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by tasota on 10/18/2018.
 *
 * SessionDAO
 *
 * Data access object for {@link Session}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface SessionDAO {

    @Insert
    void insert(Session session);

    @Update
    void update(Session session);

    @Insert
    void insert(List<Session> sessions);

    @Delete
    void delete(Session session);

    @Query("SELECT * FROM sessions ORDER BY started_at DESC")
    LiveData<List<Session>> getAllSessions();

    @Query("SELECT * FROM sessions WHERE uuid = :sessionUuid LIMIT 1")
    LiveData<Session> getSession(String sessionUuid);

    @Query("SELECT * FROM sessions WHERE student_uuid IN (:studentUuids) ORDER BY started_at DESC")
    LiveData<List<Session>> getSessionsFromStudents(List<String> studentUuids);

    @Query("SELECT * FROM sessions WHERE emotion_uuid IN (:emotionUuids)")
    LiveData<List<Session>> getSessionsFromEmotions(List<String> emotionUuids);

    @Query("SELECT * FROM sessions WHERE emotion_uuid IS NULL")
    LiveData<List<Session>> getSessionsWithoutEmotions();

}
