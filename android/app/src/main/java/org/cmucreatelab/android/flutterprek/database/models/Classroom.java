package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "classrooms")
public class Classroom {

    @PrimaryKey
    private int id;

    @NonNull
    private String name;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    @NonNull
    public String getName() {
        return name;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }

}
