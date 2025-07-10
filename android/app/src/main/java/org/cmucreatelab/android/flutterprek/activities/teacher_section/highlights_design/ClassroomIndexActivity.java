package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.ClassroomIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ClassroomIndexActivity extends HighlightsDesignActivityWithHeaderAndDrawer implements ClassroomIndexAdapter.ClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO replace adapter
        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable List<Classroom> classrooms) {
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
                classroomsGridView.setAdapter(new ClassroomIndexAdapter(ClassroomIndexActivity.this, classrooms, ClassroomIndexActivity.this));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_classrooms_index;
    }


    @Override
    public void onClick(Classroom classroom) {
        Log.i(Constants.LOG_TAG, String.format("onClick classroom name=%s with uuid=%s", classroom.getName(), classroom.getUuid()));

        // send to next activity
        //startClassroomShowStatsActivity(classroom);
        // TODO placeholder for now
        Intent intent = new Intent(this, ClassroomHighlightsActivity.class);
        startActivity(intent);
    }

}



