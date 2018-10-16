package org.cmucreatelab.android.flutterprek.database.models.db_file;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by tasota on 10/9/2018.
 */
@Entity(tableName = "db_files", indices = @Index("file_type"))
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


    public DbFile(@NonNull String uuid, @NonNull String fileType, @NonNull String filePath) {
        this.uuid = uuid;
        this.fileType = fileType;
        this.filePath = filePath;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getFileType() {
        return fileType;
    }


    @NonNull
    public String getFilePath() {
        return filePath;
    }


    public void setFileType(@NonNull String fileType) {
        this.fileType = fileType;
    }


    public void setFilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }

}
