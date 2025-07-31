package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.collapsible_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.R;

public class CopingSkillsInfoCollapsibleView extends CollapsibleInfoView {


    public CopingSkillsInfoCollapsibleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setTextForCopingSkillsDescription(String text) {
        TextView textViewCollapsible = findViewById(R.id.textViewCollapsible);
        textViewCollapsible.setText(text);
    }


    // NOTE: make sure 'collapsibleLayout' is defined in the XML
    @Override
    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_collapsible_info_coping_skills;
    }

}