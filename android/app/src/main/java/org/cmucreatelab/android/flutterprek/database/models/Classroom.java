package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by tasota on 9/6/2018.
 *
 * Classroom
 *
 * Classroom entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "classrooms")
public class Classroom {

    @PrimaryKey
    private int id;

    @NonNull
    private String name;


    public Classroom(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }


    @NonNull
    public String getName() {
        return name;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }

}
