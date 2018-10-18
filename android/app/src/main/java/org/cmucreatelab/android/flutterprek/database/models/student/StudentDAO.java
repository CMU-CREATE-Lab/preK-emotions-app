package org.cmucreatelab.android.flutterprek.database.models.student;

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
 * Data access object for {@link Student}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface StudentDAO {

    @Insert
    void insert(Student student);

    @Query("SELECT * FROM students ORDER BY name ASC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE uuid = :studentUuid LIMIT 1")
    LiveData<Student> getStudent(String studentUuid);

}
