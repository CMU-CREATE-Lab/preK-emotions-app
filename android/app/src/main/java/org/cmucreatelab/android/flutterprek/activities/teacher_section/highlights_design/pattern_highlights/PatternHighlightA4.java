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

public class PatternHighlightA4 extends PatternHighlight {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    private static final int PRIORITY = 1;


    public PatternHighlightA4(AbstractActivity activity, List<String> studentUuids) {
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
        return context.getString(R.string.pattern_highlights_a4_title);
    }


//    @Override
//    public String generateCollapsibleDescription() {
//        // TODO customize?
//        return defaultCollapsibleDescription();
//    }


    @Override
    public void runTask(CompletableFuture<Void> task) {
        CompletableFuture<List<Session>> query1 = new CompletableFuture<>();
        CompletableFuture<List<Session>> query2 = new CompletableFuture<>();

        // TODO actions
        activity.runOnUiThread(() -> {
            System.out.println("Run task A4...");
            // ``All session emotions were Happy or Excited.`` (timeframe: 1 day)
            List<String> emotionUuids = List.of(DBConstants.EmotionUuids.HAPPY, DBConstants.EmotionUuids.EXCITED);
            Calendar calendar = Calendar.getInstance();
            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastDay(calendar);

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsWithEmotionsBetween(studentUuids, emotionUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 matched from=%d to=%d and returned with size = %d", range.from, range.to, sessions.size()));
                    query1.complete(sessions);
                    // TODO liveData.removeObserver(this);
                }
            });

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsBetween(studentUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 all sessions from=%d to=%d : size = %d", range.from, range.to, sessions.size()));
                    query2.complete(sessions);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        CompletableFuture.allOf(query1, query2).thenRun(new Runnable() {
            @Override
            public void run() {
                Log.v(Constants.LOG_TAG, "...onChanged task A4");

                try {
                    List<Session> list1 = query1.get();
                    List<Session> list2 = query2.get();
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 query1 size = %d", list1.size()));
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 query2 size = %d", list2.size()));

                    if (list1.size() == 0 || list1.size() != list2.size()) {
                        isMatch = false;
                    } else {
                        isMatch = true;

                        ArrayList<String> temp = new ArrayList<>();
                        for (Session s : list1) {
                            String uuid = s.getStudentUuid();
                            if (!temp.contains(uuid)) {
                                temp.add(uuid);
                            }
                        }

                        result = new Result(temp);
                    }
                } catch (ExecutionException e) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 thrown error %s", "ExecutionException"));
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 thrown error %s", "InterruptedException"));
                    throw new RuntimeException(e);
                }

                task.complete(null);
            }
        });
    }

}
