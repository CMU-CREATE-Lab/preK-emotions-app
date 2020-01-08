package org.cmucreatelab.android.flutterprek.database.models.student;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.io.Serializable;
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
@Entity(tableName = "students", indices = @Index("classroom_uuid"), foreignKeys = @ForeignKey(entity = Classroom.class, parentColumns = "uuid", childColumns = "classroom_uuid", onDelete = CASCADE))
public class Student implements Serializable {

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
    @ColumnInfo(name="picture_file_uuid")
    private String pictureFileUuid;

    @NonNull
    @ColumnInfo(name="sequence_id")
    private int sequenceId;


    @Ignore
    public Student(@NonNull String name, @NonNull String classroomUuid) {
        this(UUID.randomUUID().toString(), name, classroomUuid);
    }


    @Ignore
    public Student(String uuid, @NonNull String name, @NonNull String classroomUuid) {
        this(uuid, name, classroomUuid, 1);
    }


    public Student(String uuid, @NonNull String name, @NonNull String classroomUuid, @NonNull int sequenceId) {
        this.uuid = uuid;
        this.name = name;
        this.classroomUuid = classroomUuid;
        this.sequenceId = sequenceId;
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


    @Nullable
    public String getPictureFileUuid() {
        return pictureFileUuid;
    }


    public int getSequenceId() {
        return sequenceId;
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


    public void setPictureFileUuid(@Nullable String pictureFileUuid) {
        this.pictureFileUuid = pictureFileUuid;
    }


    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

}
