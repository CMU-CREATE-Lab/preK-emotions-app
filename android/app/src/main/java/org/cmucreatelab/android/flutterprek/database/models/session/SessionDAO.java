package org.cmucreatelab.android.flutterprek.database.models.session;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM sessions WHERE student_uuid IN (:studentUuids) AND emotion_uuid IN (:emotionUuids) ORDER BY started_at DESC")
    LiveData<List<Session>> getSessionsFromStudentsWithEmotions(List<String> studentUuids, List<String> emotionUuids);

    @Query("SELECT * FROM sessions WHERE emotion_uuid IS NULL")
    LiveData<List<Session>> getSessionsWithoutEmotions();

}



