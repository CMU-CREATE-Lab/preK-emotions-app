package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButtonToggleGroup;

import org.cmucreatelab.android.flutterprek.R;

public class PillToggleGroup extends LinearLayout {

    private MaterialButtonToggleGroup toggleGroup;

    public PillToggleGroup(Context context) {
        super(context);
        init(context);
    }

    public PillToggleGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PillToggleGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.pill_toggle_group, this, true);
        toggleGroup = findViewById(R.id.toggleGroup);
    }

    public void setOnCheckedChanged(final OnCheckedChangedListener listener) {
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked && listener != null) {
                    listener.onCheckedChanged(checkedId);
                }
            }
        });
    }

    public void check(int id) {
        toggleGroup.check(id);
    }

    public int getCheckedId() {
        return toggleGroup.getCheckedButtonId();
    }

    public interface OnCheckedChangedListener {
        void onCheckedChanged(int checkedId);
    }
}