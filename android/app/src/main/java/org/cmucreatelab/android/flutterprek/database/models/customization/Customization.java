package org.cmucreatelab.android.flutterprek.database.models.customization;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by tasota on 10/8/2018.
 *
 * Customization
 *
 * Customization entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "customizations")
public class Customization {

    @PrimaryKey
    @NonNull
    private String uuid;

    @Nullable
    @ColumnInfo(name="owner_uuid")
    private String ownerUuid;

    @Nullable
    @ColumnInfo(name="based_on_uuid")
    private String basedOnUuid;

    @NonNull
    private String key;

    @NonNull
    private String value;

}
