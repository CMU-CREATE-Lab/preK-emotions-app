package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import org.cmucreatelab.android.flutterprek.R;

public class HighlightsViewDrawer extends ConstraintLayout {


    public HighlightsViewDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout._highlights_design__view_drawer, this);
    }

}
