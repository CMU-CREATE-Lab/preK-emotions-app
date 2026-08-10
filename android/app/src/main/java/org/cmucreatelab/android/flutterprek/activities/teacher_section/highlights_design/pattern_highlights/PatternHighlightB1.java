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

public class PatternHighlightB1 extends PatternHighlight {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    private static final int PRIORITY = 8;
    // ``30% or more of all sessions over the past 7 days include the jumping jacks coping skill``
    private static final double MATCH_THRESHOLD_RATIO = 0.30;


    public PatternHighlightB1(AbstractActivity activity, List<String> studentUuids) {
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
        return context.getString(R.string.pattern_highlights_b1_title);
    }


    @Override
    public void runTask(CompletableFuture<Void> task) {
        CompletableFuture<List<Session>> query1 = new CompletableFuture<>();
        CompletableFuture<List<Session>> query2 = new CompletableFuture<>();

        activity.runOnUiThread(() -> {
            System.out.println("Run task B1...");
            // ``sessions that include the jumping jacks coping skill`` (timeframe: past 7 days)
            Calendar calendar = Calendar.getInstance();
            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastSevenDays(calendar);

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsWithCopingSkillBetween(studentUuids, DBConstants.CopingSkillUuids.JUMPING_JACKS, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 jumping jacks sessions from=%d to=%d : size = %d", range.from, range.to, sessions.size()));
                    query1.complete(sessions);
                    // TODO liveData.removeObserver(this);
                }
            });

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsBetween(studentUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 all sessions from=%d to=%d : size = %d", range.from, range.to, sessions.size()));
                    query2.complete(sessions);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        CompletableFuture.allOf(query1, query2).thenRun(new Runnable() {
            @Override
            public void run() {
                Log.v(Constants.LOG_TAG, "...onChanged task B1");

                try {
                    List<Session> jumpingJacksSessions = query1.get();
                    List<Session> allSessions = query2.get();
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 jumping jacks sessions size = %d", jumpingJacksSessions.size()));
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 all sessions size = %d", allSessions.size()));

                    if (allSessions.isEmpty() || jumpingJacksSessions.size() < allSessions.size() * MATCH_THRESHOLD_RATIO) {
                        isMatch = false;
                    } else {
                        isMatch = true;

                        // highlight the students whose sessions actually included jumping jacks
                        ArrayList<String> temp = new ArrayList<>();
                        for (Session s : jumpingJacksSessions) {
                            String uuid = s.getStudentUuid();
                            if (!temp.contains(uuid)) {
                                temp.add(uuid);
                            }
                        }

                        result = new Result(temp, generateTitle());
                    }
                } catch (ExecutionException e) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 thrown error %s", "ExecutionException"));
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) B1 thrown error %s", "InterruptedException"));
                    throw new RuntimeException(e);
                }

                task.complete(null);
            }
        });
    }

}
