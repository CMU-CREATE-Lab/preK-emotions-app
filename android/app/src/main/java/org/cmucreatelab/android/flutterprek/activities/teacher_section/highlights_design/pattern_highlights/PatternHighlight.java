package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.content.Context;

import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class PatternHighlight {

//    public final AbstractActivity activity;
//    public final List<String> studentUuids;
    public boolean isMatch = false;
    public Result result;

    public static class Result {
        public final List<String> studentUuids;
        public final String title;
//    // TODO displayed grouping? (e.g. A1: "All the students who match the pattern, Ring is emotions for the current week.")

        public Result(List<String> studentUuids, String title) {
            this.studentUuids = studentUuids;
            this.title = title;
        }
    }

    // ...


//    public PatternHighlight(AbstractActivity activity, List<String> studentUuids) {
//        this.activity = activity;
//        this.studentUuids = studentUuids;
//        // TODO listener?
//    }


    public abstract void runTask(CompletableFuture<Void> task);


    public abstract int getPriority();


    public static String defaultCollapsibleDescription() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.highlights_design_info_description_pattern_highlights_skills_student);
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
