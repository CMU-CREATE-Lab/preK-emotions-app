package org.cmucreatelab.android.flutterprek.database.models.customization;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by tasota on 10/17/2018.
 *
 * CustomizationDAO
 *
 * Data access object for {@link Customization}. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface CustomizationDAO {

    @Insert
    void insert(Customization customization);

    @Update
    void update(Customization customization);

    @Insert
    void insert(List<Customization> customizations);

    @Delete
    void delete(Customization customization);

    @Query("SELECT * from customizations")
    LiveData<List<Customization>> getAllCustomizations();

    @Query("SELECT * FROM customizations WHERE uuid = :customizationUuid LIMIT 1")
    LiveData<Customization> getCustomization(String customizationUuid);

    @Query("SELECT * FROM customizations WHERE owner_uuid = :ownerUuid")
    LiveData<List<Customization>> getCustomizationsOwnedBy(String ownerUuid);

    @Query("SELECT * FROM customizations WHERE based_on_uuid = :basedOnUuid")
    LiveData<List<Customization>> getCustomizationsBasedOn(String basedOnUuid);

    @Query("SELECT * FROM customizations WHERE owner_uuid IS NULL")
    LiveData<List<Customization>> getCustomizationsOwnedByNoOne();

    @Query("SELECT * FROM customizations WHERE based_on_uuid IS NULL")
    LiveData<List<Customization>> getCustomizationsBasedOnNothing();

}
