package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.CalendarUtil;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DBConstants;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PatternHighlightManager {

    // 1. Instantiate with a classroom/students
    // 2. Run through a set of patterns (PatternHighlightQuery list), to determine where you have a match
    // 3. Based on the patterns that match, return a pattern object that can be used by PatternHighlightsView.
    //      Need to determine priority (or pseudo-randomization?) and what to do when there's nothing (or a default)

    // ...

    // TODO CompletableFuture implementation? (note this requires API Level 24, otherwise you're stuck with below)
    // ExecutorService, CountDownLatch
    // ...
    // (SEE BELOW; DELETE LATER)
    public interface TaskListener {
        void onAllTasksCompleted();
    }

    public interface ResultListener {
        void onResult(PatternHighlight.Result result);
    }

    public void foo (AbstractActivity activity, List<String> studentUuids) {
        // Task listener to handle all tasks completion
        TaskListener listener = () -> System.out.println("All tasks completed!");

        CompletableFuture<Void> taskT0 = new CompletableFuture<>();
        activity.runOnUiThread(() -> {
            System.out.println("Run task T0...");
            AppDatabase.getInstance(activity).studentDAO().getAllStudents().observe(activity, new Observer<List<Student>>() {
                @Override
                public void onChanged(List<Student> students) {
                    Log.v(Constants.LOG_TAG, "...onChanged task T0");
                    taskT0.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        CompletableFuture<Void> taskT1 = new CompletableFuture<>();
        activity.runOnUiThread(() -> {
            System.out.println("Run task T1...");
            AppDatabase.getInstance(activity).studentDAO().getAllStudents().observe(activity, new Observer<List<Student>>() {
                @Override
                public void onChanged(List<Student> students) {
                    Log.v(Constants.LOG_TAG, "...onChanged task T1");
                    taskT1.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        // ``Students who picked Scared in the past 7 days.``
        CompletableFuture<Void> taskA2 = new CompletableFuture<>();
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
                    taskA2.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        // ``Students who picked (Sad, Mad, Scared) more than 3 times in a week`` (Timeframe: current week (1-7 days))
        CompletableFuture<Void> taskA1 = new CompletableFuture<>();
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
                    taskA1.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

        // ``All session emotions were Happy or Excited.`` (timeframe: 1 day)
        CompletableFuture<Void> taskA4 = new CompletableFuture<>();
        PatternHighlightA4 phA4 = new PatternHighlightA4(activity, studentUuids);
//        activity.runOnUiThread(() -> {
//            // TODO FIX this just grabs the happy sessions, it does NOT check ALL sessions for happy/excited
//            System.out.println("Run task A4...");
//            // ``All session emotions were Happy or Excited.`` (timeframe: 1 day)
//            List<String> emotionUuids = List.of(DBConstants.EmotionUuids.HAPPY, DBConstants.EmotionUuids.EXCITED);
//            Calendar calendar = Calendar.getInstance();
//            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastDay(calendar);
//
//            // TODO compare THIS query for equality? (with a 'getSessionsBetween()' type call, but catches any/all emotionUuids)
//            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsWithEmotionsBetween(studentUuids, emotionUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
//                @Override
//                public void onChanged(List<Session> sessions) {
//                    Log.v(Constants.LOG_TAG, "...onChanged task A4");
//                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A4 matched from=%d to=%d and returned with size = %d", range.from, range.to, sessions.size()));
//                    taskA4.complete(null);
//                    // TODO liveData.removeObserver(this);
//                }
//            });
//        });
        phA4.runTask(taskA4);

        CompletableFuture.allOf(taskT0, taskT1, taskA2, taskA1, taskA4).thenRun(listener::onAllTasksCompleted);
    }


    public void calculate(AbstractActivity activity, List<String> studentUuids, PatternHighlightManager.ResultListener resultListener) {
        // define all patterns to query
        PatternHighlightA1 phA1 = new PatternHighlightA1(activity, studentUuids);
        PatternHighlightA2 phA2 = new PatternHighlightA2(activity, studentUuids);
        PatternHighlightA4 phA4 = new PatternHighlightA4(activity, studentUuids);

        List<PatternHighlight> list = List.of(phA1, phA2, phA4);

        // Task listener to handle all tasks completion
        TaskListener listener = () -> {
            System.out.println("All tasks completed!");
            PatternHighlight.Result result = new PatternHighlight.Result(new ArrayList<>(), "No matches");
//            if (phA4.isMatch) {
//                Log.d(Constants.LOG_TAG, "(DEBUG TaskListener) A4 matches");
//                Log.d(Constants.LOG_TAG, String.format("(DEBUG TaskListener) priority = %d", phA4.getPriority()));
//                Log.d(Constants.LOG_TAG, String.format("(DEBUG TaskListener) Result size = %d", phA4.result.studentUuids.size()));
//                result = phA4.result;
//            }
            int currentPriority = 0;
            for (PatternHighlight patternHighlight: list) {
                if (patternHighlight.isMatch && patternHighlight.getPriority() > currentPriority) {
                    Log.d(Constants.LOG_TAG, "(DEBUG TaskListener) found new match with priority %d");
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG TaskListener) found new match with priority %d, result size = %d", patternHighlight.getPriority(), patternHighlight.result.studentUuids.size()));
                    result = patternHighlight.result;
                }
            }
            resultListener.onResult(result);
        };

        // for each pattern, define tasks to run
        CompletableFuture<Void> taskA1 = new CompletableFuture<>();
        phA1.runTask(taskA1);
        CompletableFuture<Void> taskA2 = new CompletableFuture<>();
        phA2.runTask(taskA2);
        CompletableFuture<Void> taskA4 = new CompletableFuture<>();
        phA4.runTask(taskA4);

        CompletableFuture.allOf(taskA1, taskA2, taskA4).thenRun(listener::onAllTasksCompleted);
    }

}
