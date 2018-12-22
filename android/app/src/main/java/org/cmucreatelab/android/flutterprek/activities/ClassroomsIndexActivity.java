package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.ArrayList;
import java.util.List;

public class ClassroomsIndexActivity extends AppCompatActivity {


    final ArrayList<String> myStringArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_index);

        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable List<Classroom> classrooms) {
                ArrayList<String> classroomNames = new ArrayList<>();
                for (Classroom c: classrooms) {
                    classroomNames.add(c.getName());
                }
                classroomNames.add("Foo");
                classroomNames.add("Bar");
                myStringArray.clear();
                myStringArray.addAll(classroomNames);
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
//                classroomsGridView.setAdapter(new ArrayAdapter<>(ClassroomsIndexActivity.this,
//                        android.R.layout., myStringArray));
                classroomsGridView.setAdapter(new ClassroomIndexAdapter(myStringArray));
            }
        });

        FloatingActionButton fabNewClassroom = findViewById(R.id.fabNewClassroom);
        fabNewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("activity", "fabNewClassroom.onClick");
            }
        });
    }

}
