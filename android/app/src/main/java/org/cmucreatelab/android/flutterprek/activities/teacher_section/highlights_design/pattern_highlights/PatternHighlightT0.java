package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.content.Context;

import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import java.util.List;

public class PatternHighlightT0 extends PatternHighlightQuery {


    public PatternHighlightT0(AbstractActivity activity, List<String> studentUuids) {
        super(activity, studentUuids);
    }


    @Override
    public void calculatePatternResults() {
        // TODO select all students in classroom, limit to 0
    }


    @Override
    public String generateTitle() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.pattern_highlights_t0_title);
    }


    @Override
    public String generateCollapsibleDescription() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.pattern_highlights_t0_description);
    }

}
