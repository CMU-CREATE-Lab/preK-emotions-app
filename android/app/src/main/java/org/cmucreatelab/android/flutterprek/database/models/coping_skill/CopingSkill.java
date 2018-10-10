package org.cmucreatelab.android.flutterprek.database.models.coping_skill;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by tasota on 10/3/2018.
 *
 * CopingSkill
 *
 * CopingSkill entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "coping_skills")
public class CopingSkill {

    @PrimaryKey
    @NonNull
    private String uuid;

    @Nullable
    @ColumnInfo(name="owner_uuid")
    private String ownerUuid;

    @NonNull
    private String name;

    @Nullable
    @ColumnInfo(name="image_file_uuid")
    private String imageFileUuid;

}
