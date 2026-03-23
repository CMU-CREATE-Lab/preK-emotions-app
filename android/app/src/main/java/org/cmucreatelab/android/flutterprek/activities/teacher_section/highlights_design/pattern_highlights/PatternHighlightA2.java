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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PatternHighlightA2 extends PatternHighlight {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    private static final int PRIORITY = 2;


    public PatternHighlightA2(AbstractActivity activity, List<String> studentUuids) {
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
        return context.getString(R.string.pattern_highlights_a2_title);
    }


//    @Override
//    public String generateCollapsibleDescription() {
//        // TODO customize?
//        return defaultCollapsibleDescription();
//    }


    @Override
    public void runTask(CompletableFuture<Void> task) {
        activity.runOnUiThread(() -> {
            System.out.println("Run task A2...");
            // ``Students who picked Scared in the past 7 days.``
            List<String> emotionUuids = List.of(DBConstants.EmotionUuids.SCARED);
            Calendar calendar = Calendar.getInstance();
            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastSevenDays(calendar);

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsWithEmotionsBetween(studentUuids, emotionUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.v(Constants.LOG_TAG, "...onChanged task A2");
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A2 matched from=%d to=%d and returned with size = %d", range.from, range.to, sessions.size()));
                    // --- Confirm match and result
                    if (sessions.size() == 0) {
                        isMatch = false;
                    } else {
                        isMatch = true;
                        ArrayList<String> temp = new ArrayList<>();
                        for (Session s : sessions) {
                            String uuid = s.getStudentUuid();
                            if (!temp.contains(uuid)) {
                                temp.add(uuid);
                            }
                        }

                        result = new Result(temp, generateTitle());
                    }
                    task.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });
    }

}
