package org.cmucreatelab.android.flutterprek.database.models.db_file;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by tasota on 10/9/2018.
 */
@Entity(tableName = "db_files")
public class DbFile {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    @ColumnInfo(name="file_type")
    private String fileType;

    @NonNull
    @ColumnInfo(name="file_path")
    private String filePath;

}
