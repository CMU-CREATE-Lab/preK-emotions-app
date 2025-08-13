package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.AppHeaderFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseClassroomActivity;

public abstract class TeacherSectionActivityWithHeader extends AbstractActivity {

    private AppHeaderFragment headerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.headerFragment = (AppHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.appHeader));
        this.headerFragment.setHeaderTransparency(false);
        initializeTeacherSectionHeaderBackgroundColor();

        findViewById(R.id.imageStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageStudent();
            }
        });
    }


    public void onClickImageStudent() {
        if (!activityShouldHandleOnClickEvents()) {
            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
            return;
        }

        GlobalHandler.getInstance(getApplicationContext()).isTeacherMode = false;

        // bring to student section by default
        Intent intent = new Intent(this, ChooseClassroomActivity.class);
        // clear the stack entirely and create new root: https://stackoverflow.com/questions/7075349/android-clear-activity-stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void initializeTeacherSectionHeaderBackgroundColor() {
        headerFragment.getView().setBackgroundColor(getHeaderBackgroundColor());
    }


    // TODO default should be colorPrimary, using magenta to stick out for old views to be deleted
//    public int getHeaderBackgroundColor() {
//        return getResources().getColor(R.color.colorPrimary);
//    }
    public int getHeaderBackgroundColor() {
        return Color.parseColor("#FF6EC7");
    }


}



