package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizations;

import java.util.List;

public class DeterminePatternHighlights {
    private Classroom classroom;
    private List<StudentWithCustomizations> studentsWithPatterns;

    public DeterminePatternHighlights(Classroom classroom){
        this.classroom = classroom;

    }

/*    IDEA - have multiple different methods for determining different types pattern highlights
        and then randomly pick one in PatternHighlightsView.
        Changed PatternHighlight method everytime? day? week?

     */
}
