package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    private String classroomUuid;
    private String classroomName;
    private Classroom classroom;
    private final List<String> MONTHS =
            new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May",
                            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));

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
                                    Log.v("penguin", "data tedst");
                                    Log.v("penguin", String.valueOf(mapCopingSkillEmotion.size()));
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

    private void setRings() {
        List<ArcViewOverlay> saList = new ArrayList<ArcViewOverlay>();
        saList.add(findViewById(R.id.monthFirst));
        saList.add(findViewById(R.id.monthPrev));
        saList.add(findViewById(R.id.monthCurr));

        List<Integer> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE);
        List<Float> angles = Arrays.asList(150f, 120f, 90f);
        for (ArcViewOverlay arcView : saList) {
            arcView.setSegmentColors(colors);
            arcView.setSegmentAngles(angles);
            arcView.setArcWidth(20f);

        }

    }

    private void setMonthNames() {
        String first;
        String prev;
        String curr;

        Calendar calendar = Calendar.getInstance();
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

        String[] months = {first, prev, curr};

        List<ArcViewOverlay> saList = new ArrayList<ArcViewOverlay>();
        saList.add(findViewById(R.id.monthFirst));
        saList.add(findViewById(R.id.monthPrev));
        saList.add(findViewById(R.id.monthCurr));

        for(int i=0; i<saList.size(); i++){
            saList.get(i).setCenterText(months[i]);
        }

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
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                 studentsGridView.setAdapter(new StudentHighlightWithCustomizationsIndexAdapter(ClassroomHighlightsActivity.this, students, listener,addNewStudentListener));
                studentsGridView.post(() -> StudentHighlightWithCustomizationsIndexAdapter.setGridViewHeightBasedOnChildren(studentsGridView, 6));

            }
        });

        findViewById(R.id.editImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditClassNamePopup();
            }
        });

        //init edit classroom button

        //fill copping skills in most used
        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {
                //Reorder list
                List<Integer> percents = new ArrayList<>();
                percents.add(60);
                percents.add(20);
                percents.add(10);
                percents.add(10);
                //GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsView.setAdapter(new CopingSkillHighlightWCIndexAdapter(ClassroomHighlightsActivity.this, copingSkillsWithCustomizations,percents));
            }
        });
        copingSkillsView.initSettingsClickListener(this, classroom);
        copingSkillsView.enableSettingsConfig(true);
        copingSkillsView.initInfoListener(this);

//        //edit coping skills
//        ImageView editCopingSkills = findViewById(R.id.editCopingSkills);
//        editCopingSkills.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ClassroomHighlightsActivity.this, EditCopingSkillsHighlightIndex.class);
//                intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom); // if needed
//                startActivity(intent);
//            }
//        });

        initInfoButtonListeners();
        delteClassListeners();
        setMonthNames();
        setRings();

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
        ImageView montlyOverViewInfo = findViewById(R.id.monthlyOverviewInfo);

        infoClassroom.setOnClickListener(v -> showInfoDialog(title, message));
        montlyOverViewInfo.setOnClickListener(v -> showInfoDialog(title, message));

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
        textViewDemo();
       // CalculateHighlightInfo.test(this, getApplicationContext(), classroom);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_classroom_highlights;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}



