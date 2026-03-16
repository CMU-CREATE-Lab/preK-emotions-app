package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.pattern_highlights;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.MindfulnestApplication;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

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

    public void foo (AbstractActivity activity, List<String> studentUuids) {
        // Task listener to handle all tasks completion
        TaskListener listener = () -> System.out.println("All tasks completed!");

        // Create CompletableFutures for each asynchronous task

        // example query (without Observer.onChanged)
//        CompletableFuture<Void> taskA1 = CompletableFuture.runAsync(() -> {
//            try {
//                // Simulate task A1 (e.g., database query)
//                Thread.sleep(1000);
//                System.out.println("Finished task A1");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

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
            AppDatabase.getInstance(activity).studentDAO().getStudent("student_uuid_dne").observe(activity, new Observer<Student>() {
                @Override
                public void onChanged(Student student) {
                    Log.v(Constants.LOG_TAG, "...onChanged task A2");
                    taskA2.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });

//        // Combine all futures to wait for all tasks to complete
//        CompletableFuture.allOf(taskA1, taskA2, taskA3, taskT0).join();
//        // After all tasks are completed, notify listener
//        listener.onAllTasksCompleted();

        // ``Avoid using .join() on Android main thread — it blocks UI.``
        CompletableFuture.allOf(taskT0, taskT1, taskA2).thenRun(listener::onAllTasksCompleted);
    }

}
