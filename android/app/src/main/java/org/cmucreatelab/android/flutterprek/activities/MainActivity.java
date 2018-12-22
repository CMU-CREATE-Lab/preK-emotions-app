package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomViewModel;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.database.models.student.StudentViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ClassroomViewModel classroomViewModel;
    private StudentViewModel studentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classroomViewModel = ViewModelProviders.of(this).get(ClassroomViewModel.class);
        classroomViewModel.getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable final List<Classroom> classrooms) {
                Log.i("flutterprek","onChanged classrooms");
            }
        });

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable final List<Student> students) {
                Log.i("flutterprek","onChanged students");
            }
        });

        // button to navigate to classrooms index
        Button buttonClassrooms = findViewById(R.id.buttonClassrooms);
        buttonClassrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classroomsIndexActivity = new Intent(MainActivity.this, ClassroomsIndexActivity.class);
                startActivity(classroomsIndexActivity);
            }
        });

        Button buttonWeb = findViewById(R.id.buttonWeb);
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIndexActivity = new Intent(MainActivity.this, WebIndexActivity.class);
                startActivity(webIndexActivity);
            }
        });
    }
}
