package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.content.Context;
import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;

import java.io.Serializable;
import java.util.List;

public class CopingSkillWithCustomizations implements Serializable {

    public static final String CUSTOMIZATION_IS_DISABLED = "isDisabled";

    @Embedded
    public CopingSkill copingSkill;

    // TODO this only tracks coping skills disabled without a specific (ownerUuid) classroom UUID, for now
    @Relation(parentColumn = "uuid", entityColumn = "based_on_uuid", entity = Customization.class)
    public List<Customization> customizations;


    public boolean hasCustomization(String key) {
        for (Customization customization: customizations) {
            if (customization.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }


    public boolean isDisabled() {
        for (Customization customization: customizations) {
            if (customization.getKey().equals(CUSTOMIZATION_IS_DISABLED) && customization.getValue().equals("1")) {
                return true;
            }
        }
        return false;
    }


    public void isDisabled(final Context appContext, boolean flag) {
        for (Customization customization: customizations) {
            if (customization.getKey().equals(CUSTOMIZATION_IS_DISABLED)) {
                String value = flag ? "1" : "";
                customization.setKey(value);
                final Customization model = customization;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(appContext).customizationDAO().update(model);
                    }
                });
            }
        }
    }

}
