package org.cmucreatelab.android.flutterprek.database.models.classroom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tasota on 9/6/2018.
 *
 * ClassroomDAO
 *
 * Data access object for {@link Classroom}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface ClassroomDAO {

    @Insert
    void insert(Classroom classroom);

    @Query("SELECT * from classrooms ORDER BY name ASC")
    LiveData<List<Classroom>> getAllClassrooms();

    @Query("SELECT * FROM classrooms WHERE uuid = :classroomUuid LIMIT 1")
    LiveData<Classroom> getClassroom(String classroomUuid);

}
