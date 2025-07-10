package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {


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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Classroom from Intent?

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

}



