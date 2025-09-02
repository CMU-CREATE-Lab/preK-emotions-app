package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.collapsible_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.R;

public class EmotionLogCollapsibleView extends CollapsibleInfoView {


    public EmotionLogCollapsibleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    // Display the collapsible view by default
    @Override
    public boolean isCollapsedOnInitialization() {
        return false;
    }


    // NOTE: make sure 'collapsibleLayout' is defined in the XML
    @Override
    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_collapsible_info_calendar_emotion_log;
    }

}