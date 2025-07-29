package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import org.cmucreatelab.android.flutterprek.R;

public class HighlightsViewAppHeader extends ConstraintLayout {


    public HighlightsViewAppHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout._highlights_design__view_app_header, this);
    }

}



