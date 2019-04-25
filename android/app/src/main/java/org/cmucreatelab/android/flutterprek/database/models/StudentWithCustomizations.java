package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class StudentWithCustomizations {

    @Embedded
    public Student student;

    @Relation(parentColumn = "uuid", entityColumn = "owner_uuid", entity = Customization.class)
    public List<Customization> customizations;


    public boolean disableAudio() {
        for (Customization customization: customizations) {
            if (customization.getKey().equals("disableAudio")) {
                return true;
            }
        }
        return false;
    }

}
