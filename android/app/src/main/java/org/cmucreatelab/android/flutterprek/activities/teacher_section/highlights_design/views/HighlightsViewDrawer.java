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

public class HighlightsViewDrawer extends ConstraintLayout {

    private ConstraintLayout constraintNavigateBack;


    public HighlightsViewDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout._highlights_design__view_drawer, this);

        this.constraintNavigateBack = findViewById(R.id.constraintNavigateBack);
        constraintNavigateBack.setVisibility(VISIBLE);

        // default click events to entire constraint layout
        findViewById(R.id.imageButtonBackArrow).setClickable(false);
        findViewById(R.id.imageButtonBackArrow).setFocusable(false);
        findViewById(R.id.textViewBack).setClickable(false);
        findViewById(R.id.textViewBack).setFocusable(false);
        constraintNavigateBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Constants.LOG_TAG, "constraintNavigateBack clicked.");
            }
        });
    }

}



