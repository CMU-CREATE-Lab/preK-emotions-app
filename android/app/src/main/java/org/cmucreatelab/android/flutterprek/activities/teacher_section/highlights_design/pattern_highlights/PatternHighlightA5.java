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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class PatternHighlightA5 extends PatternHighlight {

    public final AbstractActivity activity;
    public final List<String> studentUuids;

    private static final int PRIORITY = 9;

    // A5. Same Emotion, Multiple days
    // ``Students who only select (sad/mad/scared) for at least two consecutive days, and chosen it every day``
    // A student matches when their most-recent calendar days form a run of at least this many
    // consecutive, calendar-adjacent days on which they picked one and the SAME target emotion
    // (sad/mad/scared) every time.
    private static final int MINIMUM_CONSECUTIVE_DAYS = 2;

    // ``The emotions to look for are: sad, mad, and scared.``
    private static final Set<String> TARGET_EMOTION_UUIDS = new HashSet<>(Arrays.asList(
            DBConstants.EmotionUuids.SAD,
            DBConstants.EmotionUuids.MAD,
            DBConstants.EmotionUuids.SCARED
    ));


    public PatternHighlightA5(AbstractActivity activity, List<String> studentUuids) {
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
        return context.getString(R.string.pattern_highlights_a5_title);
    }


    @Override
    public void runTask(CompletableFuture<Void> task) {
        activity.runOnUiThread(() -> {
            System.out.println("Run task A5...");
            // Timeframe: past 7 days (a window wide enough to observe a run of consecutive days,
            // consistent with the sibling patterns).
            Calendar calendar = Calendar.getInstance();
            CalendarUtil.BetweenRange range = CalendarUtil.generateBetweenRangeOfPastSevenDays(calendar);

            AppDatabase.getInstance(activity).sessionDAO().getSessionsFromStudentsBetween(studentUuids, range.from, range.to).observe(activity, new Observer<List<Session>>() {
                @Override
                public void onChanged(List<Session> sessions) {
                    Log.v(Constants.LOG_TAG, "...onChanged task A5");
                    Log.d(Constants.LOG_TAG, String.format("(DEBUG task) A5 matched from=%d to=%d and returned with size = %d", range.from, range.to, sessions.size()));

                    List<String> matchedStudentUuids = findStudentsWithConsecutiveSameEmotionDays(sessions);

                    if (matchedStudentUuids.isEmpty()) {
                        isMatch = false;
                    } else {
                        isMatch = true;
                        result = new Result(matchedStudentUuids, generateTitle());
                    }
                    task.complete(null);
                    // TODO liveData.removeObserver(this);
                }
            });
        });
    }


    /**
     * Groups the given sessions per student and per calendar day, then selects each student whose
     * most-recent calendar days form a run of at least {@link #MINIMUM_CONSECUTIVE_DAYS} consecutive,
     * calendar-adjacent days on which they picked one and the SAME target emotion (sad/mad/scared)
     * every time.
     *
     * The run is anchored at the student's most recent active day and walked backward one calendar
     * day at a time: a day only extends the run if the student had at least one session that day and
     * every session that day was the same target emotion as the anchor day. A skipped calendar day,
     * a day with a different emotion, or a day mixing emotions all break the run. Different students
     * may match on different emotions, but a single student's run must be one consistent emotion.
     */
    private List<String> findStudentsWithConsecutiveSameEmotionDays(List<Session> sessions) {
        // studentUuid -> (startOfDayMillis -> distinct emotion uuids selected that day)
        Map<String, TreeMap<Long, Set<String>>> perStudentDayEmotions = new HashMap<>();

        for (Session session : sessions) {
            String studentUuid = session.getStudentUuid();
            long dayKey = startOfDayMillis(session.getStartedAt());
            // represent a missing emotion as a non-target token so it can never anchor/extend a run
            String emotionToken = session.getEmotionUuid() == null ? "" : session.getEmotionUuid();

            TreeMap<Long, Set<String>> dayEmotions = perStudentDayEmotions.get(studentUuid);
            if (dayEmotions == null) {
                dayEmotions = new TreeMap<>();
                perStudentDayEmotions.put(studentUuid, dayEmotions);
            }

            Set<String> emotionsForDay = dayEmotions.get(dayKey);
            if (emotionsForDay == null) {
                emotionsForDay = new HashSet<>();
                dayEmotions.put(dayKey, emotionsForDay);
            }
            emotionsForDay.add(emotionToken);
        }

        List<String> matched = new ArrayList<>();
        for (Map.Entry<String, TreeMap<Long, Set<String>>> entry : perStudentDayEmotions.entrySet()) {
            TreeMap<Long, Set<String>> dayEmotions = entry.getValue();

            // anchor the run at the student's most recent active day
            long anchorDay = dayEmotions.lastKey();
            String anchorEmotion = pureTargetEmotion(dayEmotions.get(anchorDay));
            if (anchorEmotion == null) {
                continue;
            }

            // walk backward one calendar day at a time, requiring the same target emotion each day
            int streak = 1;
            Calendar cursor = Calendar.getInstance();
            cursor.setTimeInMillis(anchorDay);
            while (true) {
                cursor.add(Calendar.DAY_OF_MONTH, -1);
                Set<String> emotionsForDay = dayEmotions.get(cursor.getTimeInMillis());
                if (emotionsForDay == null || !anchorEmotion.equals(pureTargetEmotion(emotionsForDay))) {
                    break;
                }
                streak++;
            }

            if (streak >= MINIMUM_CONSECUTIVE_DAYS) {
                matched.add(entry.getKey());
            }
        }

        return matched;
    }


    /**
     * Returns the single target emotion (sad/mad/scared) selected on a day, or {@code null} if the
     * day mixed emotions or included any non-target emotion.
     */
    private String pureTargetEmotion(Set<String> emotionsForDay) {
        if (emotionsForDay.size() != 1) {
            return null;
        }
        String only = emotionsForDay.iterator().next();
        return TARGET_EMOTION_UUIDS.contains(only) ? only : null;
    }


    private static long startOfDayMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

}
