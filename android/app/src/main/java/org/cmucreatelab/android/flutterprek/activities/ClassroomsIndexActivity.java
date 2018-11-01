package org.cmucreatelab.android.flutterprek.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;

public class ClassroomsIndexActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_index);

        // TODO adapter
        GridView classroomsGridView = findViewById(R.id.classroomsGridView);
        //classroomsGridView.setAdapter();
    }

}
