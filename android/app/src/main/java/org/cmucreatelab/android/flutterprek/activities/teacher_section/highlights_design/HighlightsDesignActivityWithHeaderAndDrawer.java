package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomIndexActivity;

public abstract class HighlightsDesignActivityWithHeaderAndDrawer extends AbstractActivity {

    private HighlightsViewAppHeader appHeaderHighlights;
    private HighlightsViewDrawer drawerHighlights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.appHeaderHighlights = findViewById(R.id.appHeaderHighlights);
        this.drawerHighlights = findViewById(R.id.drawerHighlights);

        // TODO remove later (provided for backward navigation only)
        drawerHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent classroomsIndexActivity = new Intent(getApplicationContext(), ClassroomIndexActivity.class);
                classroomsIndexActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(classroomsIndexActivity);
            }
        });
    }


    public HighlightsViewAppHeader getAppHeaderHighlights() {
        return appHeaderHighlights;
    }


    public HighlightsViewDrawer getDrawerHighlights() {
        return drawerHighlights;
    }

}



