package org.cmucreatelab.android.flutterprek.database.models.customization;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tasota on 10/8/2018.
 *
 * Customization
 *
 * Customization entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "customizations", indices = {@Index("owner_uuid"), @Index("based_on_uuid")})
public class Customization implements Serializable {

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


    @Ignore
    public Customization(@NonNull String key, @NonNull String value) {
        this(UUID.randomUUID().toString(), key, value);
    }


    public Customization(@NonNull String uuid, @NonNull String key, @NonNull String value) {
        this.uuid = uuid;
        this.key = key;
        this.value = value;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @Nullable
    public String getOwnerUuid() {
        return ownerUuid;
    }


    @Nullable
    public String getBasedOnUuid() {
        return basedOnUuid;
    }


    @NonNull
    public String getKey() {
        return key;
    }


    @NonNull
    public String getValue() {
        return value;
    }


    public void setOwnerUuid(@Nullable String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public void setBasedOnUuid(@Nullable String basedOnUuid) {
        this.basedOnUuid = basedOnUuid;
    }


    public void setKey(@NonNull String key) {
        this.key = key;
    }


    public void setValue(@NonNull String value) {
        this.value = value;
    }

}
