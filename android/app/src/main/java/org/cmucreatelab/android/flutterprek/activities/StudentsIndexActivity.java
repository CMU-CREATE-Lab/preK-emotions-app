package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class StudentsIndexActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_index);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).studentDAO().getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentIndexAdapter(students));
            }
        });

        FloatingActionButton fabNewClassroom = findViewById(R.id.fabNewStudent);
        fabNewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("activity", "fabNewStudent.onClick");
            }
        });
    }

}
