package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {


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
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_placeholder;
    }

}
