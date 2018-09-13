package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tasota on 9/11/2018.
 *
 * StudentDAO
 *
 * Data access object for a Student. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface StudentDAO {

    @Insert
    void insert(Student student);

    @Query("SELECT * from students ORDER BY name ASC")
    LiveData<List<Student>> getAllStudents();

}
