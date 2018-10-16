package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by tasota on 10/8/2018.
 *
 * ItineraryItem
 *
 * ItineraryItem entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "itinerary_items", indices = {@Index("owner_uuid"),@Index("capability_id")})
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
    private String capabilityParameters;

}
