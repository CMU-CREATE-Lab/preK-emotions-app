package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DBConstants;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;

import java.util.Arrays;
import java.util.List;

public class PatternHighlightA1 {

    // Students who picked (Sad, Mad, Scared) more than 3 times in a week
    // Emotions: (Sad, Mad, Scared) but needs to be 4 or more times of the same one.
    // Timeframe: current week (1-7 days)
    // Displayed Grouping: All the students who match the pattern, Ring is emotions for the current week.

    public static final int PRIORITY = 10;

    private static final List<String> emotionUuids = Arrays.asList(
            DBConstants.EmotionUuids.SAD,
            DBConstants.EmotionUuids.MAD,
            DBConstants.EmotionUuids.SCARED
    );

    public interface Listener {
        // TODO PARAMS: map? enum result code? (success vs failure vs null/not calculated)?
        void onCalculatedPatternResults(List<String> studentUuids);
    }

    private final AbstractActivity activity;
    private final List<String> studentUuids;



    public static String defaultCollapsibleDescription() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.highlights_design_info_description_pattern_highlights_skills_student);
    }


    public PatternHighlightA1(AbstractActivity activity, List<String> studentUuids) {
        this.activity = activity;
        this.studentUuids = studentUuids;

        calculatePatternResults();
    }


    public void calculatePatternResults() {
        // TODO query DB to see if pattern is matched
        AppDatabase.getInstance(MindfulnestApplication.getInstanceOfApplicationContext()).sessionDAO().getSessionsFromStudentsWithEmotions(studentUuids, emotionUuids).observe(activity, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                // TODO create map studentUuid -> sessions
            }
        });
    }


    public String generateTitle() {
        return "These students have selected uncomfortable emotions many times this week.";
    }


    public String generateCollapsibleDescription() {
        // TODO customize?
        return defaultCollapsibleDescription();
    }

}
