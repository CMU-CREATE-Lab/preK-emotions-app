package org.cmucreatelab.android.flutterprek.database.models.student;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;

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

    @Update
    void update(Student student);

    @Insert
    void insert(List<Student> students);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students ORDER BY sequence_id ASC, name ASC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE classroom_uuid = :classroomUuid ORDER BY sequence_id ASC, name ASC")
    LiveData<List<Student>> getAllStudentsFromClassroom(String classroomUuid);

    @Query("SELECT * FROM students ORDER BY sequence_id ASC, name ASC")
    LiveData<List<StudentWithCustomizations>> getAllStudentsWithCustomizations();

     @Query("SELECT * FROM students WHERE classroom_uuid = :classroomUuid ORDER BY sequence_id ASC, name ASC")
    LiveData<List<StudentWithCustomizations>> getAllStudentsWithCustomizationsFromClassroom(String classroomUuid);

    @Query("SELECT * FROM students WHERE uuid = :studentUuid LIMIT 1")
    LiveData<Student> getStudent(String studentUuid);

}
