package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.zigis.segmentedarcview.SegmentedArcView;
import com.zigis.segmentedarcview.custom.ArcSegment;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillHighlightWCIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.CopingSkillIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStudentsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.UpdateClassroomModelAsyncTask;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentAddActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentEditActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.UpdateStudentModelAsyncTask;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    private String classroomUuid;
    private String classroomName;
    private Classroom classroom;
    private int currentStudentDisplayMode = StudentHighlightWithCustomizationsIndexAdapter.MODE_WEEK;
    private CalculateHighlightInfo.OverviewDateRange currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;
    private CalculateHighlightInfo.OverviewDateRange sessionOverviewTimeFrame = CalculateHighlightInfo.OverviewDateRange.MONTH;

    private StudentHighlightWithCustomizationsIndexAdapter studentGridViewAdapter;

    private final List<String> MONTHS =
            new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May",
                            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
    private final List<String> WEEKS =
            new ArrayList<>(Arrays.asList("Week 1", "Week 2", "Week 3"));
    private final List<String> DAYS =
            new ArrayList<>(Arrays.asList("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"));


    public static final String EXTRA_CLASSROOM = "classroom";

    private void textViewDemo() {
        final TextView textViewDemo = findViewById(R.id.textViewDemo);
        final AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        // TODO use Classroom object from Intent; demo uses first classroom listed in the database
        appDatabase.classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                Log.v(Constants.LOG_TAG, "Got result from getAllClassrooms");
                if (!classrooms.isEmpty()) {
                    final Classroom classroom = classrooms.get(0);
                    appDatabase.studentDAO().getAllStudentsFromClassroom(classroom.getUuid()).observe(ClassroomHighlightsActivity.this, new Observer<List<Student>>() {
                        @Override
                        public void onChanged(List<Student> students) {
                            Log.v(Constants.LOG_TAG, "Got result from getAllStudentsFromClassroom");
                            // API 24...
                            // List<String> studentUuids = students.stream().map(Student::getUuid).collect(Collectors.toList());
                            // ...
                            ArrayList<String> studentUuids = new ArrayList<>();
                            for (Student s: students) {
                                studentUuids.add(s.getUuid());
                            }
                            // Grab all sessions/coping skills with LIST of students (for individual Student use list of size 1)
                            appDatabase.intermediateTablesDAO().getSessionsWithSessionCopingSkillsFromStudents(studentUuids).observe(ClassroomHighlightsActivity.this, new Observer<List<StudentWithSessionsAndSessionCopingSkills>>() {
                                @Override
                                public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> studentWithSessionsAndSessionCopingSkills) {
                                    Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");

                                    // create mapping of CopingSkill + Emotion with Count
                                    Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> mapCopingSkillEmotion = StudentWithSessionsAndSessionCopingSkills.countSessionCopingSkillsWithEmotion(studentWithSessionsAndSessionCopingSkills);

                                    // build string to display Mapping
                                    StringBuilder text = new StringBuilder();
                                    for (StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion copingSkillEmotion : mapCopingSkillEmotion.keySet()) {
                                        Integer count = mapCopingSkillEmotion.get(copingSkillEmotion);
                                        text.append(copingSkillEmotion.toString()).append(String.format(" -- appears %d times.\n", count));
                                    }

                                    // update the text view on the UI thread ("onChanged" means we might not be in main thread anymore)
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textViewDemo.setText(text.toString());
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private void setOverviewRings(CalculateHighlightInfo calculateHighlightInfo) {
        //list in order currrent, prev, 2 monthsago
        List<ArcViewOverlay> saList = new ArrayList<ArcViewOverlay>();
        saList.add(findViewById(R.id.monthCurr));
        saList.add(findViewById(R.id.monthPrev));
        saList.add(findViewById(R.id.monthFirst));

        //grab maps
        Map<String, Integer> thisMonthsEmotionCounts = calculateHighlightInfo.getThisSessionEmotionCounts();
        Map<String, Integer> lastMonthsEmotionCounts = calculateHighlightInfo.getLastSessionEmotionCounts();
        Map<String, Integer> twoMonthsAgoEmotionCounts = calculateHighlightInfo.getTwoSessionsAgoEmotionCounts();

        //grab percents
        List<List<Float>> percentList = new ArrayList<List<Float>>();
        percentList.add(CalculateHighlightInfo.calculateCirclePercents(thisMonthsEmotionCounts));
        percentList.add(CalculateHighlightInfo.calculateCirclePercents(lastMonthsEmotionCounts));
        percentList.add(CalculateHighlightInfo.calculateCirclePercents(twoMonthsAgoEmotionCounts));

        List<Integer> defaultColor = Arrays.asList(Color.GRAY);
        List<Float> defaultAngle = Arrays.asList(360f);
        for(int i=0; i<saList.size(); i++){

            if(percentList.get(i).isEmpty()){
                saList.get(i).setSegmentColors(defaultColor);
                saList.get(i).setSegmentAngles(defaultAngle);
                saList.get(i).setArcWidth(20f);;
            } else {
                saList.get(i).setSegmentColors(CalculateHighlightInfo.EMOTION_COLORS);
                saList.get(i).setSegmentAngles(percentList.get(i));
                saList.get(i).setArcWidth(20f);
            }
        }

    }


    private void setOverviewNames() {
        String first;
        String prev;
        String curr;

        float textSize = 48f;

        Calendar calendar = Calendar.getInstance();

        if(sessionOverviewTimeFrame == CalculateHighlightInfo.OverviewDateRange.MONTH){
            int month = calendar.get(Calendar.MONTH); // 0 = January, 11 = December
            if(month == 0){
                first = MONTHS.get(10);
                prev = MONTHS.get(11);
                curr = MONTHS.get(0);
            } else if(month ==1){
                first = MONTHS.get(11);
                prev = MONTHS.get(0);
                curr = MONTHS.get(1);
            } else {
                first = MONTHS.get(month-2);
                prev = MONTHS.get(month-1);
                curr = MONTHS.get(month);
            }

        } else if(sessionOverviewTimeFrame == CalculateHighlightInfo.OverviewDateRange.DAY){
            int day = calendar.get(Calendar.DAY_OF_WEEK) -1; //0 = Sunday, 6 = Saturday
            if(day == 0){
                first = DAYS.get(5);
                prev = DAYS.get(6);
                curr = DAYS.get(0);
            } else if (day == 1){
                first = DAYS.get(6);
                prev = DAYS.get(0);
                curr = DAYS.get(1);
            } else {
                first = DAYS.get(day-2);
                prev = DAYS.get(day-1);
                curr = DAYS.get(day);
            }

        }
        else{
            first = getWeeklyRangeLabelWithOffset(2);
            prev = getWeeklyRangeLabelWithOffset(1);
            curr = getWeeklyRangeLabelWithOffset(0);

            textSize = 16f;
        }



        String[] names = {first, prev, curr};

        List<ArcViewOverlay> saList = new ArrayList<ArcViewOverlay>();
        saList.add(findViewById(R.id.monthFirst));
        saList.add(findViewById(R.id.monthPrev));
        saList.add(findViewById(R.id.monthCurr));

        for(int i=0; i<saList.size(); i++){
            saList.get(i).setCenterText(names[i]);
            saList.get(i).setTextSize(textSize);
         //   saList.get(i).invalidate();
        }

    }
    private String getWeeklyRangeLabelWithOffset(int weekOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // Move to the start of the current week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        // Subtract weeks (0 = this week, 1 = last week, 2 = two weeks ago)
        cal.add(Calendar.WEEK_OF_YEAR, -weekOffset);
        Date startOfWeek = cal.getTime();

        // Calculate end of week
        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = endCal.getTime();

        // Format result
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
        return formatter.format(startOfWeek) + "–" + formatter.format(endOfWeek);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button buttonPlaceholder = findViewById(R.id.buttonPlaceholder);
        TextView textView = findViewById(R.id.editMyClassroom);
        textView.setText(classroomName);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonPlaceholder.setText("Back to classes");
            }
        });

        buttonPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO next activity
                Intent intent = new Intent(ClassroomHighlightsActivity.this, ClassroomIndexActivity.class);
                startActivity(intent);
            }
        });

        //fill classroom with students
        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                studentGridViewAdapter = new StudentHighlightWithCustomizationsIndexAdapter(ClassroomHighlightsActivity.this, students, listener,addNewStudentListener);

                GridView studentsGridView = findViewById(R.id.studentsGridView);
                 studentsGridView.setAdapter(studentGridViewAdapter);
                studentsGridView.post(() -> StudentHighlightWithCustomizationsIndexAdapter.setGridViewHeightBasedOnChildren(studentsGridView, 6));

               //display mode for toggle day,week,month,year
                studentGridViewAdapter.setDisplayMode(currentStudentDisplayMode);
            }
        });
        //init edit classroom button

        findViewById(R.id.editImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditClassNamePopup();
            }
        });


        //fill copping skills in most used
        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        //copingSkillsView.calculateClassCopingSkillsOverview(CalculateHighlightInfo.OverviewDateRange.WEEK, classroom);

        //update the coping skills display - need custom call because of date range and calculating percents before adapter
        updateCopingSkillsView();

        copingSkillsView.initSettingsClickListener(this, classroom);
        copingSkillsView.enableSettingsConfig(true);
        copingSkillsView.initInfoListener(this);



        initPillGroupToggles();
        initInfoButtonListeners();
        delteClassListeners();


    }
    private void updateCopingSkillsView(){
        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {

                copingSkillsView.calculateClassCopingSkillsOverview(currentCopingSkillDisplayMode, classroom, new CopingSkillsOverviewCallback() {
                    @Override
                    public void onOverviewCalculated(Map<String, Integer> copingSkillsMap) {

                        //sort the coping skills based on count
                        Collections.sort(copingSkillsWithCustomizations, new Comparator<CopingSkillWithCustomizations>() {
                            @Override
                            public int compare(CopingSkillWithCustomizations cp1, CopingSkillWithCustomizations cp2) {
                                Integer count1 = copingSkillsMap.get(cp1.copingSkill.getUuid());
                                Integer count2 = copingSkillsMap.get(cp2.copingSkill.getUuid());

                                if (count1 == null) count1 = 0;
                                if (count2 == null) count2 = 0;

                                return count2.compareTo(count1); //highest to lowest
                            }
                        });

                        // Now create percents list in the same order as copingSkillsWithCustomizations
                        List<Integer> percents = new ArrayList<>();
                        percents = CalculateHighlightInfo.calculateCopingSkillPercents(copingSkillsMap);


                        copingSkillsView.setAdapter(new CopingSkillHighlightWCIndexAdapter(ClassroomHighlightsActivity.this, copingSkillsWithCustomizations, percents));
                    }
                });
            }
        });
    }

    private void delteClassListeners(){
        findViewById(R.id.deleteClassroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClassroomDeleteDialog();
            }
        });
        findViewById(R.id.trashClassroomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClassroomDeleteDialog();
            }
        });

    }
    private void initInfoButtonListeners() {
        String title = "What is This?";
        String message = "This section explains emotional regulation techniques.";

        ImageView infoClassroom = findViewById(R.id.classroomInfo);
        ImageView monthlyOverViewInfo = findViewById(R.id.monthlyOverviewInfo);

        infoClassroom.setOnClickListener(v -> showInfoDialog(title, message));
        monthlyOverViewInfo.setOnClickListener(v -> showInfoDialog(title, message));

    }
    public void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showEditClassNamePopup(){
        // Create an EditText
        final EditText input = new EditText(this);
        input.setHint("Enter new Name");

        new AlertDialog.Builder(this)
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newText = input.getText().toString();
                    updateName(newText);
                    TextView myTextView = findViewById(R.id.editMyClassroom);
                    myTextView.setText(newText);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateName(String newName){
        //student.setName(newName);
        classroom.setName(newName);
        classroomName = newName;

        new UpdateClassroomModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateClassroomModelAsyncTask.ActionType.UPDATE, classroom, new UpdateClassroomModelAsyncTask.PostExecute() {
            @Override
            public void onPostExecute(Boolean modelSaved, Classroom classroom) {
                // deleting classroom triggers return to index
                if (!modelSaved) {
                    Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private void displayClassroomDeleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_classroom_name, null);
        builder.setView(view)
                .setTitle(R.string.alert_title_delete_classroom)
                .setMessage(String.format(getString(R.string.alert_message_delete_classroom), classroom.getName()))
                .setPositiveButton(R.string.alert_option_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String classroomName = ((EditText) view.findViewById(R.id.editTextClassroomName)).getText().toString();
                        Log.d(Constants.LOG_TAG, String.format("dialog_classroom_name onClick positive; name='%s'", classroomName));

                        // confirm deletion by checking input with class name
                        if (classroom.getName().equals(classroomName)) {
                            new UpdateClassroomModelAsyncTask(AppDatabase.getInstance(ClassroomHighlightsActivity.this), UpdateClassroomModelAsyncTask.ActionType.DELETE, classroom, new UpdateClassroomModelAsyncTask.PostExecute() {
                                @Override
                                public void onPostExecute(Boolean modelSaved, Classroom classroom) {
                                    // deleting classroom triggers return to index
                                    Intent intent = new Intent(ClassroomHighlightsActivity.this, ClassroomIndexActivity.class);
                                    startActivity(intent);
                                }
                            }).execute();
                        } else {
                            Toast.makeText(ClassroomHighlightsActivity.this, R.string.toast_delete_classroom_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.alert_option_cancel, null);
        builder.create().show();
    }

    private final StudentHighlightWithCustomizationsIndexAdapter.ClickListener listener = new StudentHighlightWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(StudentWithCustomizations studentWithCustomizations) {
            final Student student = studentWithCustomizations.student;
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());


            Intent studentEditActivity = new Intent(ClassroomHighlightsActivity.this, StudentHighlightsActivity.class);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_STUDENT, studentWithCustomizations.student);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
            startActivity(studentEditActivity);


        }
    };

    private final StudentHighlightWithCustomizationsIndexAdapter.ClickAddNewStudentListener addNewStudentListener = new StudentHighlightWithCustomizationsIndexAdapter.ClickAddNewStudentListener() {
        @Override
        public void onClick() {
            Intent studentAddActivityIntent = new Intent(ClassroomHighlightsActivity.this, StudentHighlightsActivity.class);
            String templateNameForAddStudent = "New Student";

            Student newStudent = new Student(templateNameForAddStudent, classroomUuid);
            updateModel(newStudent);
            studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_STUDENT, newStudent);
            studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
            startActivity(studentAddActivityIntent);

        }
    };

    public void updateModel(final Student student) {
        Log.d(Constants.LOG_TAG, "performing DB writes in updateModel()");
        new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.INSERT, student, null, new UpdateStudentModelAsyncTask.PostExecute() {
            @Override
            public void onPostExecute(Boolean modelSaved) {
                if (!modelSaved) {
                    Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        }).execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();

        this.classroom = (Classroom) getIntent().getSerializableExtra(EXTRA_CLASSROOM);

        this.classroomUuid = getClassroom().getUuid();
        this.classroomName = getClassroom().getName();




        // TODO delete later (demo count of coping skills with emotions)

      updateSessionOverview();

    }
    private void updateSessionOverview(){
        CalculateHighlightInfo calculateHighlightInfo = new CalculateHighlightInfo(classroom, getApplicationContext(), this);
        calculateHighlightInfo.sessionOverview(sessionOverviewTimeFrame, new HighlightCalculationCallback() {
            @Override
            public void onHighlightsCalculated() {
                setOverviewRings(calculateHighlightInfo);
                setOverviewNames();
            }
        });
    }

    private void initPillGroupToggles(){
        PillToggleGroup classroomToggle = findViewById(R.id.classroomToggle);
        classroomToggle.check(R.id.btn_week);
        classroomToggle.setOnCheckedChanged(new PillToggleGroup.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                // Set student grid view adapter
                switch (checkedId) {
                    case R.id.btn_day:
                        currentStudentDisplayMode = StudentHighlightWithCustomizationsIndexAdapter.MODE_DAY;
                        break;
                    case R.id.btn_week:
                        currentStudentDisplayMode = StudentHighlightWithCustomizationsIndexAdapter.MODE_WEEK;
                        break;
                    case R.id.btn_month:
                        currentStudentDisplayMode = StudentHighlightWithCustomizationsIndexAdapter.MODE_MONTH;
                        break;
                    case R.id.btn_year:
                        currentStudentDisplayMode = StudentHighlightWithCustomizationsIndexAdapter.MODE_YEAR;
                        break;
                }
//
                if (studentGridViewAdapter != null) {
                    studentGridViewAdapter.setDisplayMode(currentStudentDisplayMode);
                }
            }
        });

        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        copingSkillsView.setOnToggleCheckedChanged(new PillToggleGroup.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                switch (checkedId) {
                    case R.id.btn_day:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.DAY;
                        break;
                    case R.id.btn_week:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;
                        break;
                    case R.id.btn_month:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.MONTH;
                        break;
                    case R.id.btn_year:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.YEAR;
                        break;
                }
                if(currentCopingSkillDisplayMode == null){
                    currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;
                }
                updateCopingSkillsView();

            }
        });



        PillToggleGroup classOverviewToggle = findViewById(R.id.classOverviewToggle);
        classOverviewToggle.check(R.id.btn_month);

        // hides the year toggle and then rounds the month toggle corner
        classOverviewToggle.findViewById(R.id.btn_year).setVisibility(View.GONE);
        MaterialButton monthButton = classOverviewToggle.findViewById(R.id.btn_month);

        classOverviewToggle.post(() -> {
            float cornerRadiusPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    15,
                    getResources().getDisplayMetrics()
            );

            ShapeAppearanceModel shape = new ShapeAppearanceModel.Builder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, cornerRadiusPx)
                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadiusPx)
                    .build();

            monthButton.setShapeAppearanceModel(shape);

        });

        classOverviewToggle.setOnCheckedChanged(new PillToggleGroup.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                // Set student grid view adapter
                switch (checkedId) {
                    case R.id.btn_day:
                        sessionOverviewTimeFrame = CalculateHighlightInfo.OverviewDateRange.DAY;
                        break;
                    case R.id.btn_week:
                        sessionOverviewTimeFrame = CalculateHighlightInfo.OverviewDateRange.WEEK;
                        break;
                    case R.id.btn_month:
                        sessionOverviewTimeFrame = CalculateHighlightInfo.OverviewDateRange.MONTH;
                        break;

                }
//
                if (sessionOverviewTimeFrame == null) {
                    sessionOverviewTimeFrame = CalculateHighlightInfo.OverviewDateRange.MONTH;
                }
                updateSessionOverview();
            }
        });

    }

    public interface HighlightCalculationCallback {
        void onHighlightsCalculated();
    }
    public interface CopingSkillsOverviewCallback {
        void onOverviewCalculated(Map<String, Integer> copingSkillsMap);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_classroom_highlights;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}



