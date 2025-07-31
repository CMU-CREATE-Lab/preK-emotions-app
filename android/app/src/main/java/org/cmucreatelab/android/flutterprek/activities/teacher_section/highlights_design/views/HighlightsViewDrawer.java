package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

public class HighlightsViewDrawer extends ConstraintLayout {

    public enum Row {
        APP_SETTINGS,
        CLASSES_INDEX,
        CLASS_SHOW
    };

    public final ConstraintLayout constraintNavigateBack;
    public final HighlightsViewDrawerItem constraintRowAppSettings, constraintRowClassesIndex, constraintRowClassShow;

    // TODO navigation types? (app_settings, classes_index, class_show, exit_to_students_section, ...+student_show?)
    private boolean isNavigateBack;
    private Classroom classroom;
    private View.OnClickListener clickListenerForNavigateBack = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.w(Constants.LOG_TAG, "clickListenerForNavigateBack triggered but not implemented");
        }
    };


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

        // NOTE: back navigation is disabled by default and must be set with setNavigateBack()
        setNavigateBack(false, null, null);
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
                onClickNavigateBack(v);
            }
        });
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
    }


    public void setNavigateBack(boolean isVisible, String text, View.OnClickListener onClickListener) {
        this.isNavigateBack = isVisible;
        constraintNavigateBack.setVisibility( isVisible ? VISIBLE : INVISIBLE);
        if (text != null) ((TextView) findViewById(R.id.textViewBack)).setText(text);
        if (onClickListener != null) this.clickListenerForNavigateBack = onClickListener;
    }


    public Classroom getClassroom() {
        return classroom;
    }


    public void onClickNavigateBack(View v) {
//        if (!activityShouldHandleOnClickEvents()) {
//            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
//            return;
//        }
        if (!isNavigateBack) {
            Log.w(Constants.LOG_TAG, "ignoring onClickNavigateBack when isNavigateBack is set to false");
            return;
        }

        Log.v(Constants.LOG_TAG, "constraintNavigateBack clicked.");
        // pass onclick to set attribute (if implemented)
        clickListenerForNavigateBack.onClick(v);
    }

}



