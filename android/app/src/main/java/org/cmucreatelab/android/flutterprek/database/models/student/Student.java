package org.cmucreatelab.android.flutterprek.database.models.student;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by tasota on 9/11/2018.
 *
 * Student
 *
 * Student entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "students", foreignKeys = @ForeignKey(entity = Classroom.class, parentColumns = "uuid", childColumns = "classroom_uuid", onDelete = CASCADE))
public class Student {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    private String name;

    @Nullable
    private String notes;

    @NonNull
    @ColumnInfo(name="classroom_uuid")
    private String classroomUuid;

    @Nullable
    @ColumnInfo(name="picture_filepath")
    private String pictureFilepath;


    @Ignore
    public Student(@NonNull String name, @NonNull String classroomUuid) {
        this(UUID.randomUUID().toString(), name, classroomUuid);
    }


    public Student(String uuid, @NonNull String name, @NonNull String classroomUuid) {
        this.uuid = uuid;
        this.name = name;
        this.classroomUuid = classroomUuid;
    }


    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getName() {
        return name;
    }


    @Nullable
    public String getNotes() {
        return notes;
    }


    public String getClassroomUuid() {
        return classroomUuid;
    }


    // NOTE this probably should never get called
    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }


    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }


    public void setClassroomUuid(String classroomUuid) {
        this.classroomUuid = classroomUuid;
    }

}
