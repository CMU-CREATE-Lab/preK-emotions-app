package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.StudentWithSessionsAndSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculateHighlightInfo {

    public static void test(AppCompatActivity activity, Context context, Classroom classroom) {

        AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        //appDatabase.studentDAO().getAllStudentsFromClassroom(classroom.getUuid()).observe(ClassroomHighlightsActivity.this, new Observer<List<Student>>() {
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
                    public void onChanged(List<StudentWithSessionsAndSessionCopingSkills> studentWithSessionsAndSessionCopingSkills) {
                        Log.v(Constants.LOG_TAG, "Got result from getSessionsWithSessionCopingSkillsFromStudents");

                        // create mapping of CopingSkill + Emotion with Count
                        Map<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> mapCopingSkillEmotion = StudentWithSessionsAndSessionCopingSkills.countSessionCopingSkillsWithEmotion(studentWithSessionsAndSessionCopingSkills);

                        StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion maxKey = null;
                        int maxValue = Integer.MIN_VALUE;

                        for (Map.Entry<StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion, Integer> entry : mapCopingSkillEmotion.entrySet()) {
                            if (entry.getValue() > maxValue) {
                                maxValue = entry.getValue();
                                maxKey = entry.getKey();
                            }
                        }

                        Log.v("penguin", "maxKey = " + maxKey.toString());
//                        // build string to display Mapping
//                        StringBuilder text = new StringBuilder();
//                        for (StudentWithSessionsAndSessionCopingSkills.CopingSkillEmotion copingSkillEmotion : mapCopingSkillEmotion.keySet()) {
//                            Integer count = mapCopingSkillEmotion.get(copingSkillEmotion);
//                            text.append(copingSkillEmotion.toString()).append(String.format(" -- appears %d times.\n", count));
//                        }

                    }
                });
            }
        });

    }
}
