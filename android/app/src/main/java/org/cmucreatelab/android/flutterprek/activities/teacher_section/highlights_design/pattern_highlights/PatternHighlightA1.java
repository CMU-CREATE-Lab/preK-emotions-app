package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.CalendarUtil;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DBConstants;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PatternHighlightA1 extends PatternHighlight {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    private static final int PRIORITY = 10;

    // ``more than 3 times in a week`` == 4 or more occurrences
    private static final int MATCH_THRESHOLD = 4;


    public PatternHighlightA1(AbstractActivity activity, List<String> studentUuids) {
        this.activity = activity;
        this.studentUuids = studentUuids;
        // TODO listener?
    }


    @Override
    public int getPriority() {
        return PRIORITY;
    }


    @Override
    public String generateTitle() {
        Context context = MindfulnestApplication.getInstanceOfApplicationContext();
        return context.getString(R.string.pattern_highlights_a1_title);
    }


//    @Override
//    public String generateCollapsibleDescription() {
//        // TODO customize?
//        return defaultCollapsibleDescription();
//    }


    @Override
    public void runTask(CompletableFuture<Void> task) {
        activity.runOnUiThread(() -> {
            System.out.println("Run task A1...");
            // ``(Sad, Mad, Scared)``
            List<String> emotionUuids = List.of(DBConstants.EmotionUuids.SCARED, DBConstants.EmotionUuids.SAD, DBConstants.EmotionUuids.MAD);
            Calendar calendar = Calendar.getInstance();
            // ``Timeframe: current week (1-7 days)``
            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastWeek(calendar);

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsWithEmotionsBetween(studentUuids, emotionUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.v(Constants.LOG_TAG, "...onChanged task A1");
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A1 matched from=%d to=%d and returned with size = %d", range.from, range.to, sessions.size()));
                    // --- Confirm match and result
//                    // TODO still needs to actually count for 4 or more occurrences
//                    isMatch = false;

                    // Count matching sessions (Sad/Mad/Scared, this week) per student, then
                    // keep only students who hit the threshold.
                    Map<String, Integer> countsByStudent = new HashMap<>();
                    for (Session s : sessions) {
                        String uuid = s.getStudentUuid();
                        countsByStudent.put(uuid, countsByStudent.getOrDefault(uuid, 0) + 1);
                    }

                    ArrayList<String> matchedStudentUuids = new ArrayList<>();
                    for (Map.Entry<String, Integer> entry : countsByStudent.entrySet()) {
                        if (entry.getValue() >= MATCH_THRESHOLD) {
                            matchedStudentUuids.add(entry.getKey());
                        }
                    }

                    if (matchedStudentUuids.isEmpty()) {
                        isMatch = false;
                    } else {
                        isMatch = true;
                        result = new Result(matchedStudentUuids, generateTitle());
                    }
                    // ...

                    task.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });
    }

}
