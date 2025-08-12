package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseClassroomActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.settings.HighlightsDesignAppSettingsMainActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.HighlightsViewAppHeader;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.HighlightsViewDrawer;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

public abstract class HighlightsDesignActivityWithHeaderAndDrawer extends AbstractActivity {

    private HighlightsViewAppHeader appHeaderHighlights;
    private HighlightsViewDrawer drawerHighlights;


    private void initOnClickListenersForDrawer() {
        drawerHighlights.constraintRowAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighlightsDesignActivityWithHeaderAndDrawer.this, HighlightsDesignAppSettingsMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        drawerHighlights.constraintRowClassesIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighlightsDesignActivityWithHeaderAndDrawer.this, org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        drawerHighlights.constraintRowClassShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classroom classroom = drawerHighlights.getClassroom();
                if (classroom != null) {
                    Intent intent = new Intent(HighlightsDesignActivityWithHeaderAndDrawer.this, ClassroomHighlightsActivity.class);
                    intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Log.e(Constants.LOG_TAG, "clicked constraintRowClassShow in HighlightsViewDrawer but classroom is null.");
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.appHeaderHighlights = findViewById(R.id.appHeaderHighlights);
        this.drawerHighlights = findViewById(R.id.drawerHighlights);

        findViewById(R.id.imageStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageStudent();
            }
        });
        // TODO remove later (provided for backward navigation only)
        findViewById(R.id.imageInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageInfo();
            }
        });

        initOnClickListenersForDrawer();
    }


    // TODO @Dante can't we just do this in onCreate?
    protected void setUpDrawer(){
        this.drawerHighlights = findViewById(R.id.drawerHighlights);
    }


    public HighlightsViewAppHeader getAppHeaderHighlights() {
        return appHeaderHighlights;
    }


    public HighlightsViewDrawer getDrawerHighlights() {
        return drawerHighlights;
    }


    public void onClickImageStudent() {
        if (!activityShouldHandleOnClickEvents()) {
            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
            return;
        }

        // TODO confirmation dialog "Leave Teacher Section?"
        // ...

        GlobalHandler.getInstance(getApplicationContext()).isTeacherMode = false;

        // bring to student section by default
        Intent intent = new Intent(this, ChooseClassroomActivity.class);
        // clear the stack entirely and create new root: https://stackoverflow.com/questions/7075349/android-clear-activity-stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void onClickImageInfo() {
        if (!activityShouldHandleOnClickEvents()) {
            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
            return;
        }

        Intent classroomsIndexActivity = new Intent(getApplicationContext(), ClassroomIndexActivity.class);
        classroomsIndexActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(classroomsIndexActivity);
    }


    public void setBackNavigationForDrawer(boolean isVisible, String text, View.OnClickListener onClickListener) {
        if (!isVisible) {
            drawerHighlights.setNavigateBack(false, null, null);
            return;
        }
        if ((text == null) || text.isEmpty()) {
            Log.w(Constants.LOG_TAG, "isNavigationDisplayedInDrawer for Activity is true but the text is blank or null");
        }
        drawerHighlights.setNavigateBack(isVisible, text, onClickListener);
    }

}



