package org.cmucreatelab.android.flutterprek.database.models.embedded_models;

import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class StudentWithCustomizationsAndEmotions {

    public StudentWithCustomizations student;

    public List<Emotion> emotions;


    public StudentWithCustomizationsAndEmotions(StudentWithCustomizations student, List<Emotion> emotions) {
        this.student = student;
        this.emotions = emotions;
    }

}
