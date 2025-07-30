package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.R;

public class HighlightsViewDrawerItem extends ConstraintLayout {

    private View highlightView;
    private TextView textView;

    private static final boolean DEFAULT_IS_SELECTED = false;
    private static final int DEFAULT_TEXT_RES = R.string.highlights_design_drawer_item_class_name_placeholder;


    public void setHighlightsDrawerItemSelected(boolean isSelected) {
        if (isSelected) {
            highlightView.setVisibility(VISIBLE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        } else {
            highlightView.setVisibility(INVISIBLE);
            textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
        }
    }


    public void setHighlightsDrawerItemText(String text) {
        textView.setText(text);
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText(DEFAULT_TEXT_RES);
        }
    }


    public HighlightsViewDrawerItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout._highlights_design__view_drawer_item, this);
        this.highlightView = findViewById(R.id.highlightView);
        this.textView = findViewById(R.id.textView);

        // NOTE: these XML must be defined, or the view will not inflate properly

        // app:highlights_drawer_item_selected
        // app:highlights_drawer_item_text
        initializeWithAttributeSet(context, attrs);
    }


    private void initializeWithAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightsViewDrawerItem);

            // app:highlights_drawer_item_selected
            boolean isSelected = a.getBoolean(R.styleable.HighlightsViewDrawerItem_highlights_drawer_item_selected, DEFAULT_IS_SELECTED);
            setHighlightsDrawerItemSelected(isSelected);

            // app:highlights_drawer_item_text
            String text = a.getString(R.styleable.HighlightsViewDrawerItem_highlights_drawer_item_text);
            setHighlightsDrawerItemText(text);

            a.recycle();
        }
    }

}



