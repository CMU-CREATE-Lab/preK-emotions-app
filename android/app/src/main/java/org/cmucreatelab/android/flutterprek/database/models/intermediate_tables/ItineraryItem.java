package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * Created by tasota on 10/8/2018.
 *
 * ItineraryItem
 *
 * ItineraryItem entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "itinerary_items", indices = {@Index("owner_uuid"), @Index("capability_id")})
public class ItineraryItem {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    @ColumnInfo(name="owner_uuid")
    private String ownerUuid;

    @NonNull
    @ColumnInfo(name="sequence_id")
    private int sequenceId;

    @NonNull
    @ColumnInfo(name="capability_id")
    private String capabilityId;

    @NonNull
    @ColumnInfo(name="capability_parameters")
    private JSONObject capabilityParameters;


    public ItineraryItem(@NonNull String uuid, @NonNull String ownerUuid, @NonNull int sequenceId, @NonNull String capabilityId, @NonNull JSONObject capabilityParameters) {
        this.uuid = uuid;
        this.ownerUuid = ownerUuid;
        this.sequenceId = sequenceId;
        this.capabilityId = capabilityId;
        this.capabilityParameters = capabilityParameters;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getOwnerUuid() {
        return ownerUuid;
    }


    @NonNull
    public int getSequenceId() {
        return sequenceId;
    }


    @NonNull
    public String getCapabilityId() {
        return capabilityId;
    }


    @NonNull
    public JSONObject getCapabilityParameters() {
        return capabilityParameters;
    }


    public void setOwnerUuid(@NonNull String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public void setSequenceId(@NonNull int sequenceId) {
        this.sequenceId = sequenceId;
    }


    public void setCapabilityId(@NonNull String capabilityId) {
        this.capabilityId = capabilityId;
    }


    public void setCapabilityParameters(@NonNull JSONObject capabilityParameters) {
        this.capabilityParameters = capabilityParameters;
    }

}
