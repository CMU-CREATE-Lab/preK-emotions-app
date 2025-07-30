package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

public class HighlightsViewDrawer extends ConstraintLayout {

    public enum Row {
        APP_SETTINGS,
        CLASSES_INDEX,
        CLASS_SHOW
    };

    private ConstraintLayout constraintNavigateBack;
    private HighlightsViewDrawerItem constraintRowAppSettings, constraintRowClassesIndex, constraintRowClassShow;

    // TODO navigation types? (app_settings, classes_index, class_show, exit_to_students_section, ...+student_show?)
    private boolean isNavigateBack;
    private Classroom classroom;


    private void setDrawerItem(@NonNull HighlightsViewDrawerItem drawerItem, boolean isDisplayed) {
        drawerItem.setVisibility( isDisplayed ? VISIBLE : INVISIBLE);
    }


    private void setItemSelected(HighlightsViewDrawerItem selectedDrawerItem) {
        constraintRowAppSettings.setHighlightsDrawerItemSelected(false);
        constraintRowClassesIndex.setHighlightsDrawerItemSelected(false);
        constraintRowClassShow.setHighlightsDrawerItemSelected(false);
        if (selectedDrawerItem != null) selectedDrawerItem.setHighlightsDrawerItemSelected(true);
    }


    public HighlightsViewDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout._highlights_design__view_drawer, this);

        this.constraintNavigateBack = findViewById(R.id.constraintNavigateBack);
        this.constraintRowAppSettings = findViewById(R.id.constraintRowAppSettings);
        this.constraintRowClassesIndex = findViewById(R.id.constraintRowClassesIndex);
        this.constraintRowClassShow = findViewById(R.id.constraintRowClassShow);

        setNavigateBack(true);
        setDrawerItem(constraintRowAppSettings, true);
        setDrawerItem(constraintRowClassesIndex, true);
        setDrawerItem(constraintRowClassShow, false);

        // default all click events to "constraintNavigateBack" layout
        findViewById(R.id.imageButtonBackArrow).setClickable(false);
        findViewById(R.id.imageButtonBackArrow).setFocusable(false);
        findViewById(R.id.textViewBack).setClickable(false);
        findViewById(R.id.textViewBack).setFocusable(false);
        constraintNavigateBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNavigateBack();
            }
        });
    }


    public void setNavigateBack(boolean isVisible) {
        this.isNavigateBack = isVisible;
        constraintNavigateBack.setVisibility( isVisible ? VISIBLE : INVISIBLE);
    }


    public void setHighlighted(Row row) {
        switch (row) {
            case APP_SETTINGS:
                setItemSelected(constraintRowAppSettings);
                break;
            case CLASSES_INDEX:
                setItemSelected(constraintRowClassesIndex);
                break;
            case CLASS_SHOW:
                setItemSelected(constraintRowClassShow);
                break;
            default:
                Log.w(Constants.LOG_TAG, "setHighlighted row not implemented.");
                setItemSelected(null);
                break;
        }
    }


    public void setTextForClassNameRow(String className) {
        constraintRowClassShow.setHighlightsDrawerItemText(className);
    }


    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
        setTextForClassNameRow(classroom.getName());
        setDrawerItem(constraintRowClassShow, true);
        // TODO navigation
    }


    public void onClickNavigateBack() {
//        if (!activityShouldHandleOnClickEvents()) {
//            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
//            return;
//        }
        if (!isNavigateBack) {
            Log.w(Constants.LOG_TAG, "ignoring onClickNavigateBack when isNavigateBack is set to false");
            return;
        }

        Log.v(Constants.LOG_TAG, "constraintNavigateBack clicked.");
    }

}



