package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.cmucreatelab.android.flutterprek.Constants;
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
                Log.i(Constants.LOG_TAG,"onChanged classrooms");
            }
        });

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable final List<Student> students) {
                Log.i(Constants.LOG_TAG,"onChanged students");
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
        // students index
        Button buttonStudents = findViewById(R.id.buttonStudents);
        buttonStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentsIndexActivity = new Intent(MainActivity.this, StudentIndexActivity.class);
                startActivity(studentsIndexActivity);
            }
        });
        // choose classroom
        Button buttonChooseClass = findViewById(R.id.buttonChooseClassroom);
        buttonChooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseClassroomActivity = new Intent(MainActivity.this, ChooseClassroomActivity.class);
                startActivity(chooseClassroomActivity);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_emotions:
                Intent chooseEmotionActivity = new Intent(MainActivity.this, ChooseEmotionActivity.class);
                startActivity(chooseEmotionActivity);
                return true;
        }
        return false;
    }

}
