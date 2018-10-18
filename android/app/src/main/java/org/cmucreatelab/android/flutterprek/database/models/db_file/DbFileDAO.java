package org.cmucreatelab.android.flutterprek.database.models.db_file;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tasota on 10/18/2018.
 *
 * DbFileDAO
 *
 * Data access object for {@link DbFile}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface DbFileDAO {

    @Insert
    void insert(DbFile dbFile);

    @Query("SELECT * FROM db_files")
    LiveData<List<DbFile>> getAllDbFiles();

    @Query("SELECT * FROM db_files WHERE uuid = :dbFileUuid LIMIT 1")
    LiveData<DbFile> getDbFile(String dbFileUuid);

    @Query("SELECT * FROM db_files WHERE file_type = :type")
    LiveData<List<DbFile>> getDbFilesOfType(String type);

}
