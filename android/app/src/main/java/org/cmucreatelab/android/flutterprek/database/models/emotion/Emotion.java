package org.cmucreatelab.android.flutterprek.database.models.emotion;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by tasota on 10/3/2018.
 *
 * Emotion
 *
 * Emotion entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "emotions", indices = {@Index("owner_uuid"), @Index("image_file_uuid")})
public class Emotion {

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


    public Emotion(@NonNull String uuid, @NonNull String name) {
        this.uuid = uuid;
        this.name = name;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @Nullable
    public String getOwnerUuid() {
        return ownerUuid;
    }


    @NonNull
    public String getName() {
        return name;
    }


    @Nullable
    public String getImageFileUuid() {
        return imageFileUuid;
    }


    public void setOwnerUuid(@Nullable String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public void setName(@NonNull String name) {
        this.name = name;
    }


    public void setImageFileUuid(@Nullable String imageFileUuid) {
        this.imageFileUuid = imageFileUuid;
    }

}
