package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;

import java.util.List;

public class ClassroomWithCustomizations {

    @Embedded
    public Classroom classroom;

    @Relation(parentColumn = "uuid", entityColumn = "owner_uuid", entity = Customization.class)
    public List<Customization> customizations;


    public String getSessionMode() {
        for (Customization customization: customizations) {
            if (customization.getKey().equals("sessionMode")) {
                return customization.getValue();
            }
        }
        return null;
    }

}
