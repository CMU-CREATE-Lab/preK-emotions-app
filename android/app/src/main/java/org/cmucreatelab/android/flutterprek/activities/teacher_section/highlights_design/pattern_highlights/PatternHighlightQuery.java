package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.content.Context;

import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import java.util.List;

public abstract class PatternHighlightQuery {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    public static String defaultCollapsibleDescription() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.highlights_design_info_description_pattern_highlights_skills_student);
    }

    public interface Listener {
        // TODO PARAMS: map? enum result code? (success vs failure vs null/not calculated)?
        // TODO class/instance type? Otherwise all will extend from same thing
        void onCalculatedPatternResults(List<String> studentUuids);
    }

    public abstract void calculatePatternResults();


//    public PatternHighlightQuery() {
//        // TODO defaults, listener
//    }


    public PatternHighlightQuery(AbstractActivity activity, List<String> studentUuids) {
        this.activity = activity;
        this.studentUuids = studentUuids;
        // TODO listener?
    }

    public String generateTitle() {
        //return "These students have selected uncomfortable emotions many times this week.";
        //pattern_highlights_title
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.pattern_highlights_title);
    }


    public String generateCollapsibleDescription() {
        // TODO customize?
        return defaultCollapsibleDescription();
    }

}
