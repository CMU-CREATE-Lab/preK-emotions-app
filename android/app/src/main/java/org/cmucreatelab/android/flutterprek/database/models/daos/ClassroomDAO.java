package org.cmucreatelab.android.flutterprek.database.models.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.cmucreatelab.android.flutterprek.database.models.Classroom;

import java.util.List;

@Dao
public interface ClassroomDAO {

    @Insert
    void insert(Classroom classroom);

    @Query("SELECT * from classrooms ORDER BY name ASC")
//    List<Classroom> getAllClassrooms();
    LiveData<List<Classroom>> getAllClassrooms();

}
