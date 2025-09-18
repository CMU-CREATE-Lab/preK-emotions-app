package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.content.Context;

import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;

public class PatternHighlightA1 {

    // Students who picked (Sad, Mad, Scared) more than 3 times in a week
    // Emotions: (Sad, Mad, Scared) but needs to be 4 or more times of the same one.
    // Timeframe: current week (1-7 days)
    // Displayed Grouping: All the students who match the pattern, Ring is emotions for the current week.

    public static final int PRIORITY = 10;


    public static String defaultCollapsibleDescription() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.highlights_design_info_description_pattern_highlights_skills_student);
    }


    public void calculatePatternResults() {
        // TODO query DB to see if pattern is matched
    }


    public String generateTitle() {
        return "These students have selected uncomfortable emotions many times this week.";
    }


    public String generateCollapsibleDescription() {
        // TODO customize?
        return defaultCollapsibleDescription();
    }

}
