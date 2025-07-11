package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    private String classroomUuid;
    private String classroomName;
    private Classroom classroom;
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


    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        Button buttonPlaceholder = findViewById(R.id.buttonPlaceholder);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPlaceholder.setText("ClassroomHighlightsActivity");
                buttonPlaceholder.setText("GoTo Student");
            }
        });

        buttonPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO next activity
                Intent intent = new Intent(ClassroomHighlightsActivity.this, StudentHighlightsActivity.class);
                startActivity(intent);
            }
        });

        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                 studentsGridView.setAdapter(new StudentHighlightWithCustomizationsIndexAdapter(ClassroomHighlightsActivity.this, students, listener));

                 Log.v("penguin", String.valueOf(students.size()));
            }
        });
    }

    private final StudentHighlightWithCustomizationsIndexAdapter.ClickListener listener = new StudentHighlightWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(StudentWithCustomizations studentWithCustomizations) {
            final Student student = studentWithCustomizations.student;
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());

//            Intent studentEditActivity = new Intent(ClassroomShowStudentsActivity.this, StudentEditActivity.class);
//            studentEditActivity.putExtra(StudentEditActivity.EXTRA_STUDENT, studentWithCustomizations.student);
//            studentEditActivity.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
//            startActivity(studentEditActivity);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Classroom from Intent?
        this.classroom = (Classroom) getIntent().getSerializableExtra(EXTRA_CLASSROOM);

        this.classroomUuid = getClassroom().getUuid();
        this.classroomName = getClassroom().getName();


        // TODO STUDENTS adapter
        //...
//        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
//            @Override
//            public void onChanged(@Nullable List<Classroom> classrooms) {
//                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
//                classroomsGridView.setAdapter(new ClassroomIndexAdapter(ClassroomHighlightsActivity.this, classrooms, ClassroomHighlightsActivity.this));
//            }
//        });

        // TODO delete later (demo count of coping skills with emotions)
        textViewDemo();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_classroom_highlights;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}



