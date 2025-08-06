package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.R;

public class EmotionLogCalendarView extends ConstraintLayout {


    public EmotionLogCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log;
    }

}
