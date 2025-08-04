package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.ArcViewOverlay;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.CopingSkillsHighlightGridView;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.SessionWithSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CalculateHighlightInfo {
    private static final String CURRENT_TIME_FRAME = "current_time_frame";
    private static final String PREVIOUS_TIME_FRAME = "previous_time_frame";
    private static final String TWO_TIME_FRAMES_AGO = "two_months_time_frame";
    private static final String OUT_OF_RANGE = "out_of_range";

    private static final List<Emotion> EMOTION_List;
    private static final List<CopingSkill> COPING_SKILL_LIST;

    static {
        List<Emotion> tempEmotionList = new ArrayList<>();
        tempEmotionList.add(new Emotion("emotion1", "Happy"));
        tempEmotionList.add(new Emotion("emotion6", "Excited"));
        tempEmotionList.add(new Emotion("emotion2", "Sad"));
        tempEmotionList.add(new Emotion("emotion3", "Mad"));
        tempEmotionList.add(new Emotion("emotion5", "Scared"));

        EMOTION_List = Collections.unmodifiableList(tempEmotionList);

        List<CopingSkill> tempCopingSkillList = new ArrayList<>();
        tempCopingSkillList.add(new CopingSkill("coping_skill_14", "Conduct"));
        tempCopingSkillList.add(new CopingSkill("coping_skill_18", "Cuddle"));
        tempCopingSkillList.add(new CopingSkill("coping_skill_1", "Flower Breathing"));
        tempCopingSkillList.add(new CopingSkill("coping_skill_5", "Jumping Jacks"));
        COPING_SKILL_LIST = Collections.unmodifiableList(tempCopingSkillList);
    }
    public static final List<Integer> EMOTION_COLORS = Arrays.asList(ColorConstants.HAPPY_COLOR,ColorConstants.EXCITED_COLOR, ColorConstants.MAD_COLOR,
            ColorConstants.SAD_COLOR,
            ColorConstants.SCARED_COLOR);

    private Classroom classroom;
    private Student student;
    private Context context;
    private AppCompatActivity activity;
    private AppDatabase appDatabase;
    private Map<String, Integer> thisSessionClassEmotionCounts = new HashMap<>();
    private Map<String, Integer> lastSessionClassEmotionCounts = new HashMap<>();
    private Map<String, Integer> twoSessionsAgoClassEmotionCounts = new HashMap<>();
    private Map<String, Integer> studentEmotionCounts = new HashMap<>();
    private Map<String, Integer> classCopingSkillsOverview = new HashMap<>();
    private List<StudentWithSessionsAndSessionCopingSkills> studentWithSessionsAndSessionCopingSkills;

    public enum OverviewDateRange {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    public CalculateHighlightInfo(Classroom classroom, Context context, AppCompatActivity activity){
        this.classroom = classroom;
        this.context = context;
        this.activity = activity;
        this.appDatabase = AppDatabase.getInstance(context.getApplicationContext());

    }

    //main calling function for filling the coping skill count maps for a class
    public void copingSkillClassOverview(OverviewDateRange range, CopingSkillsHighlightGridView.CopingSkillCalculationCallback callback){

        ArrayList<String> studentUuids = new ArrayList<>();

        appDatabase.studentDAO().getAllStudentsFromClassroom(classroom.getUuid()).observe(activity, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                Log.v(Constants.LOG_TAG, "Got result from getAllStudentsFromClassroom");

                ArrayList<String> studentUuids = new ArrayList<>();
                for (Student s : students) {
                    studentUuids.add(s.getUuid());
                }
                // Grab all sessions/coping skills with LIST of students (for individual Student use list of size 1)
                appDatabase.embeddedDAO().getSessionsWithSessionCopingSkillsFromStudents(studentUuids).observe(activity, new Observer<List<StudentWithSessionsAndSessionCopingSkills>>() {
                    @Override
                    public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> results) {
                        Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");
                        calculateClassCopingSkillsOverview(results, range);

                        if (callback != null) {
                            callback.onCopingSkillsCalculated();
                        }

                    }
                });
            }
        });
    }

    //main calling function for filling the coping skill count maps for a student
    public void copingSkillStudentOverview(OverviewDateRange range, Student student, CopingSkillsHighlightGridView.CopingSkillCalculationCallback callback){

        ArrayList<String> studentUuids = new ArrayList<>();
        studentUuids.add(student.getUuid());

        appDatabase.embeddedDAO().getSessionsWithSessionCopingSkillsFromStudents(studentUuids).observe(activity, new Observer<List<StudentWithSessionsAndSessionCopingSkills>>() {
            @Override
            public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> results) {
                Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");

                calculateClassCopingSkillsOverview(results, range);
                if (callback != null) {
                    callback.onCopingSkillsCalculated();
                }

            }
        });

    }
    private void calculateClassCopingSkillsOverview(List<StudentWithSessionsAndSessionCopingSkills> sessions, OverviewDateRange range){
        if(sessions == null || sessions.isEmpty()){
            return;
        }


        //only this week
        List<SessionWithSessionCopingSkills> currentSessions = new ArrayList<SessionWithSessionCopingSkills>();
        for(StudentWithSessionsAndSessionCopingSkills student : sessions){
            currentSessions.addAll(getSessionsByPeriod(student, range));
        }

        //  List<SessionWithSessionCopingSkills> currentSessions = getCurrentWeekSessions(sessions.get(0)); // get 0 cause one student in list
        //create count w/ emotions
        if (currentSessions == null || currentSessions.isEmpty()) {
            return;
        }



        for(SessionWithSessionCopingSkills sessionWithSkills : currentSessions){
            //check for null emotionUuid in session?
            for(SessionCopingSkill sessionCopingSkill : sessionWithSkills.sessionCopingSkills){

                for(CopingSkill copingSkill : COPING_SKILL_LIST){

                    if(sessionCopingSkill.getCopingSkillUuid().equals(copingSkill.getUuid())){
                        classCopingSkillsOverview.put(copingSkill.getUuid(), getOrDefault(classCopingSkillsOverview, copingSkill.getUuid(), 0) + 1);
                    }
                }
            }

        }

    }

    //main function for student overview - one student at a time
    public void studentOverview(Student student,OverviewDateRange dateRange, StudentHighlightWithCustomizationsIndexAdapter.HighlightCalculationCallback callback){

        ArrayList<String> studentUuids = new ArrayList<>();
        studentUuids.add(student.getUuid());

        appDatabase.embeddedDAO().getSessionsWithSessionCopingSkillsFromStudents(studentUuids).observe(activity, new Observer<List<StudentWithSessionsAndSessionCopingSkills>>() {
            @Override
            public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> results) {
                Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");
                calculateStudentOverview(results, dateRange);

                if (callback != null) {
                    callback.onHighlightsCalculated();
                }

            }
        });
    }

    //works on single student
    public void calculateStudentOverview(List<StudentWithSessionsAndSessionCopingSkills> sessions, OverviewDateRange range){
        
        if(sessions == null || sessions.isEmpty()){
            return;
        }


        //only this week
        List<SessionWithSessionCopingSkills> currentSessions = getSessionsByPeriod(sessions.get(0), range); //get 0 cause one student in list

        //create count w/ emotions
        if (currentSessions == null || currentSessions.isEmpty()) {
            return;
        }

        for(SessionWithSessionCopingSkills sessionWithSkills : currentSessions){
            //check for null emotionUuid in session?

            for (Emotion emotion : EMOTION_List) {
                if (sessionWithSkills.session.getEmotionUuid() == null) {
                    Log.w(Constants.LOG_TAG, "calculateStudentOverview found session with null emotion UUID; continuing...");
                    continue;
                }
                if (sessionWithSkills.session.getEmotionUuid().equals(emotion.getUuid())) {
                    studentEmotionCounts.put(emotion.getName(), getOrDefault(studentEmotionCounts, emotion.getName(), 0) + 1);
                }
            }
        }

    }

    //returns all the sesions withina  time fram (day, week ,month, year)
    private static List<SessionWithSessionCopingSkills> getSessionsByPeriod(StudentWithSessionsAndSessionCopingSkills student, OverviewDateRange period) {
        List<SessionWithSessionCopingSkills> filteredSessions = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        switch (period) {
            case DAY:
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end = (Calendar) start.clone();
                end.add(Calendar.DAY_OF_MONTH, 1);
                end.add(Calendar.MILLISECOND, -1);
                break;

            case WEEK:
                start.set(Calendar.DAY_OF_WEEK, start.getFirstDayOfWeek());
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end = (Calendar) start.clone();
                end.add(Calendar.DAY_OF_WEEK, 6);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.MILLISECOND, 999);
                break;

            case MONTH:
                start.set(Calendar.DAY_OF_MONTH, 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end = (Calendar) start.clone();
                end.set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH));
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.MILLISECOND, 999);
                break;

            case YEAR:
                start.set(Calendar.MONTH, Calendar.JANUARY);
                start.set(Calendar.DAY_OF_MONTH, 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end = (Calendar) start.clone();
                end.set(Calendar.MONTH, Calendar.DECEMBER);
                end.set(Calendar.DAY_OF_MONTH, 31);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.MILLISECOND, 999);
                break;
        }

        for (SessionWithSessionCopingSkills sessionWithSkills : student.sessions) {
            Session session = sessionWithSkills.session;
            Date startedAt = session.getStartedAt();
            if (startedAt == null) continue;

            Calendar sessionCal = Calendar.getInstance();
            sessionCal.setTime(startedAt);

            if (!sessionCal.before(start) && !sessionCal.after(end)) {
                filteredSessions.add(sessionWithSkills);
            }
        }

        return filteredSessions;
    }



    //main function for fill the monthly overview rings
    public void sessionOverview(OverviewDateRange range, ClassroomHighlightsActivity.HighlightCalculationCallback callback) {


       // appDatabase.studentDAO().getAllStudentsFromClassroom(classroom.getUuid()).observe(ClassroomHighlightsActivity.this, new Observer<List<Student>>() {
        appDatabase.studentDAO().getAllStudentsFromClassroom(classroom.getUuid()).observe(activity, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                Log.v(Constants.LOG_TAG, "Got result from getAllStudentsFromClassroom");
                // API 24...
                // List<String> studentUuids = students.stream().map(Student::getUuid).collect(Collectors.toList());
                // ...
                ArrayList<String> studentUuids = new ArrayList<>();
                for (Student s : students) {
                    studentUuids.add(s.getUuid());
                }
                // Grab all sessions/coping skills with LIST of students (for individual Student use list of size 1)
                appDatabase.embeddedDAO().getSessionsWithSessionCopingSkillsFromStudents(studentUuids).observe(activity, new Observer<List<StudentWithSessionsAndSessionCopingSkills>>() {
                    @Override
                    public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> results) {
                        Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");
                        calculateOverviewRings(results, range);

                        if (callback != null) {
                            callback.onHighlightsCalculated();
                        }

                    }
                });
            }
        });

    }

    //calculate the percentages for the monthly overvciew rigns
    public void calculateOverviewRings (List<StudentWithSessionsAndSessionCopingSkills> sessions, OverviewDateRange range){

        if(sessions == null){
            return;
        }
        //only wanhted months
        List<List<StudentWithSessionsAndSessionCopingSkills>> pastThreeFrameSessions = getPastTimeFramesSessions(sessions, range);


        List<StudentWithSessionsAndSessionCopingSkills> thisFramesSessions = pastThreeFrameSessions.get(0); //current frame (today)
        List<StudentWithSessionsAndSessionCopingSkills> lastFramesSessions = pastThreeFrameSessions.get(1); //previous frame (yesterday)
        List<StudentWithSessionsAndSessionCopingSkills> twoFramesAgoSessions = pastThreeFrameSessions.get(2); //two frams agao (two days ago)

        //create count w/ emotions and copping skills maps
        Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> thisMonthsCounts = StudentWithSessionsAndSessionCopingSkills.countSessionCopingSkillsWithEmotion(thisFramesSessions);
        Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> lastMonthsCounts = StudentWithSessionsAndSessionCopingSkills.countSessionCopingSkillsWithEmotion(lastFramesSessions);
        Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> twoMonthsAgoCounts = StudentWithSessionsAndSessionCopingSkills.countSessionCopingSkillsWithEmotion(twoFramesAgoSessions);


        for(Emotion emotion : EMOTION_List){
            thisSessionClassEmotionCounts.put(emotion.getName(), getEmotionTotal(thisMonthsCounts, emotion.getUuid()));
            lastSessionClassEmotionCounts.put(emotion.getName(), getEmotionTotal(lastMonthsCounts, emotion.getUuid()));
            twoSessionsAgoClassEmotionCounts.put(emotion.getName(), getEmotionTotal(twoMonthsAgoCounts, emotion.getUuid()));
        }



    }

    //returns total count of an emotion in sessions
    private static int getEmotionTotal(Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> map, String emotionUuid) {
        int total = 0;
        for (Map.Entry<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> entry : map.entrySet()) {
            if (entry.getKey().emotionUuid.equals(emotionUuid)) {
                total += entry.getValue();
            }
        }
        return total;
    }


    //helper function for getPastTimeFramesSessions
    private String getTimeFrameLabelForDate(Date dateToCheck, OverviewDateRange range) {
        if (dateToCheck == null) return OUT_OF_RANGE;

        Calendar input = Calendar.getInstance();
        input.setTime(dateToCheck);

        Calendar now = Calendar.getInstance();

        int diff = 0;

        int inputYear = input.get(Calendar.YEAR);
        int currentYear = now.get(Calendar.YEAR);

        switch (range) {
            case DAY:
                long diffMillis = now.getTimeInMillis() - input.getTimeInMillis();
                diff = (int) TimeUnit.MILLISECONDS.toDays(diffMillis);
                break;

            case WEEK:
                int inputWeek = input.get(Calendar.WEEK_OF_YEAR);
                int currentWeek = now.get(Calendar.WEEK_OF_YEAR);

                diff = (currentYear - inputYear) * 52 + (currentWeek - inputWeek);
                break;

            case MONTH:
                int inputMonth = input.get(Calendar.MONTH); // 0-based
                int currentMonth = now.get(Calendar.MONTH);

                diff = (currentYear - inputYear) * 12 + (currentMonth - inputMonth);
                break;
        }

        switch (diff) {
            case 0:
                return CURRENT_TIME_FRAME;
            case 1:
                return PREVIOUS_TIME_FRAME;
            case 2:
                return TWO_TIME_FRAMES_AGO;
            default:
                return OUT_OF_RANGE;
        }
    }

    //makes a list with three lists of sessions for each period of time (current, previous, two ago)
    private List<List<StudentWithSessionsAndSessionCopingSkills>> getPastTimeFramesSessions(
            List<StudentWithSessionsAndSessionCopingSkills> allStudents,
            OverviewDateRange range) {

        List<StudentWithSessionsAndSessionCopingSkills> current = new ArrayList<>();
        List<StudentWithSessionsAndSessionCopingSkills> previous = new ArrayList<>();
        List<StudentWithSessionsAndSessionCopingSkills> twoAgo = new ArrayList<>();

        for (StudentWithSessionsAndSessionCopingSkills student : allStudents) {
            List<SessionWithSessionCopingSkills> currentList = new ArrayList<>();
            List<SessionWithSessionCopingSkills> previousList = new ArrayList<>();
            List<SessionWithSessionCopingSkills> twoAgoList = new ArrayList<>();

            for (SessionWithSessionCopingSkills sessionWithSkills : student.sessions) {
                Session session = sessionWithSkills.session;
                Date date = session.getStartedAt();
                String label = getTimeFrameLabelForDate(date, range);

                switch (label) {
                    case CURRENT_TIME_FRAME:
                        currentList.add(sessionWithSkills);
                        break;
                    case PREVIOUS_TIME_FRAME:
                        previousList.add(sessionWithSkills);
                        break;
                    case TWO_TIME_FRAMES_AGO:
                        twoAgoList.add(sessionWithSkills);
                        break;
                }
            }

            if (!currentList.isEmpty()) {
                StudentWithSessionsAndSessionCopingSkills clone = new StudentWithSessionsAndSessionCopingSkills();
                clone.student = student.student;
                clone.sessions = currentList;
                current.add(clone);
            }

            if (!previousList.isEmpty()) {
                StudentWithSessionsAndSessionCopingSkills clone = new StudentWithSessionsAndSessionCopingSkills();
                clone.student = student.student;
                clone.sessions = previousList;
                previous.add(clone);
            }

            if (!twoAgoList.isEmpty()) {
                StudentWithSessionsAndSessionCopingSkills clone = new StudentWithSessionsAndSessionCopingSkills();
                clone.student = student.student;
                clone.sessions = twoAgoList;
                twoAgo.add(clone);
            }
        }

        List<List<StudentWithSessionsAndSessionCopingSkills>> result = new ArrayList<>();
        result.add(current);
        result.add(previous);
        result.add(twoAgo);
        return result;
    }


    //returns in order of highest percentage coping skill
    public static List<Integer> calculateCopingSkillPercents(Map<String, Integer> map){

        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);
        percentFormat.setMinimumFractionDigits(0);

        List<Float> counts = new ArrayList<>(Arrays.asList(0f, 0f, 0f, 0f));
        List<Integer> percents = new ArrayList<>(Arrays.asList(0, 0, 0, 0));

        float total = 0f;

        // Collect counts from map
        for (int i = 0; i < COPING_SKILL_LIST.size() && i < 4; i++) {
            String uuid = COPING_SKILL_LIST.get(i).getUuid();
            if (map.containsKey(uuid)) {
                float count = map.get(uuid);
                counts.set(i, count);
                total += count;
            }
        }

        if (total == 0) {
            return percents;
        }

        //format as percents
        for (int i = 0; i < counts.size(); i++) {
            percents.set(i, Math.round(counts.get(i) / total * 100));
        }

        // sort highest to lowest
        Collections.sort(percents);
        Collections.reverse(percents);

        return percents;


    }
    //returns list of percents for filling circle
    //in order of happy,excited, mad, sad, scared
    public static List<Float> calculateCirclePercents(Map<String, Integer> map){
        int total = 0;
        float happyCount =0;
        float sadCount =0;
        float madCount =0;
        float excitedCount =0;
        float scaredCount =0;
        if(map.containsKey("Happy")){
            happyCount += map.get("Happy");
        }
        if(map.containsKey("Sad")){
            sadCount += map.get("Sad");
        }
        if(map.containsKey("Mad")){
            madCount += map.get("Mad");
        }
        if(map.containsKey("Excited")){
            excitedCount += map.get("Excited");
        }
        if(map.containsKey("Scared")){
            scaredCount += map.get("Scared");
        }
        total = (int) (happyCount + sadCount + madCount + excitedCount + scaredCount);
        List<Float> percents = new ArrayList<>();
        if(total == 0){
            return percents;
        }
        //times 360 for full circle
        percents.add(happyCount/total * 360);
        percents.add(excitedCount/total *360);
        percents.add(madCount/total *360);
        percents.add(sadCount/total * 360);
        percents.add(scaredCount/total* 360);
        return percents;

    }

    //fills an ArcViewOverlay
    public static void setArc(Map<String, Integer> map, ArcViewOverlay arcView, float arcWidth, boolean isAddStudent){
        if(!isAddStudent && !map.isEmpty()){
            // List<Integer> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE);
            List<Float> angles = CalculateHighlightInfo.calculateCirclePercents(map);
            arcView.setSegmentColors(CalculateHighlightInfo.EMOTION_COLORS);
            arcView.setSegmentAngles(angles);
            arcView.setArcWidth(arcWidth);

        } else {
            List<Integer> colors = Arrays.asList(Color.GRAY);
            List<Float> angles = Arrays.asList(360f);
            arcView.setSegmentColors(colors);
            arcView.setSegmentAngles(angles);
            arcView.setArcWidth(arcWidth);
        }
    }
    private static <K> int getOrDefault(Map<K, Integer> map, K key, int defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public Map<String, Integer> getThisSessionEmotionCounts() {
        return thisSessionClassEmotionCounts;
    }
    public Map<String, Integer> getLastSessionEmotionCounts() {
        return lastSessionClassEmotionCounts;
    }
    public Map<String, Integer> getTwoSessionsAgoEmotionCounts() {
        return twoSessionsAgoClassEmotionCounts;
    }

    public Map<String, Integer> getStudentEmotionCounts() {
        return studentEmotionCounts;
    }

    public Map<String, Integer> getClassCopingSkillsCounts() {
        return classCopingSkillsOverview;
    }
}
