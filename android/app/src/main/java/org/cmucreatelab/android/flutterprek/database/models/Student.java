package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by tasota on 9/11/2018.
 *
 * Student
 *
 * Student entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "students")
public class Student {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    private String name;

    @Nullable
    private String notes;


    @Ignore
    public Student(@NonNull String name) {
        this(UUID.randomUUID().toString(), name);
    }


    public Student(String uuid, @NonNull String name) {
        this.uuid = uuid;
        this.name = name;
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


    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }


    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

}
