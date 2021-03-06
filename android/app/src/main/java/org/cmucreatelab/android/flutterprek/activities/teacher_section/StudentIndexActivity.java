package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentIndexAdapterWithSessionCount;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.session_index.SessionIndexActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

// TODO Student index does not actually use the "Main" drawer
public class StudentIndexActivity extends TeacherSectionActivityWithHeader implements StudentIndexAdapterWithSessionCount.ClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).studentDAO().getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentIndexAdapterWithSessionCount(StudentIndexActivity.this, students, StudentIndexActivity.this));
            }
        });

        FloatingActionButton fabNewClassroom = findViewById(R.id.fabNewStudent);
        fabNewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("activity", "fabNewStudent.onClick");
                if (!activityShouldHandleOnClickEvents()) {
                    Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                    return;
                }
            }
        });
        fabNewClassroom.hide();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_students_index;
    }


    @Override
    public void onClick(Student student) {
        Log.i("activity", "clicked student " + student.getName());
        if (!activityShouldHandleOnClickEvents()) {
            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
            return;
        }
        Intent intent = new Intent(this, SessionIndexActivity.class);
        intent.putExtra(SessionIndexActivity.STUDENT_KEY, student);
        startActivity(intent);
    }

}
