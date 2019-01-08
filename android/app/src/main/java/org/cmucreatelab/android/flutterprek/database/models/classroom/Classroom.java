package org.cmucreatelab.android.flutterprek.database.models.classroom;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tasota on 9/6/2018.
 *
 * Classroom
 *
 * Classroom entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "classrooms")
public class Classroom implements Serializable {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    private String name;


    @Ignore
    public Classroom(@NonNull String name) {
        this(UUID.randomUUID().toString(), name);
    }


    public Classroom(String uuid, @NonNull String name) {
        this.uuid = uuid;
        this.name = name;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getName() {
        return name;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }

}
