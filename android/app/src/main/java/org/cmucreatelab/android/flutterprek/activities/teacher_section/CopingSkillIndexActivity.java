package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherClassroomFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills.CopingSkillEditActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.List;

public class CopingSkillIndexActivity extends ManageClassroomActivityWithHeaderAndDrawer {

    private final CopingSkillWithCustomizationsIndexAdapter.ClickListener clickListener = new CopingSkillWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(CopingSkillWithCustomizations copingSkillWithCustomizations, List<ItineraryItem> itineraryItems, View view) {
            Intent intent = new Intent(CopingSkillIndexActivity.this, CopingSkillEditActivity.class);
            intent.putExtra(CopingSkillEditActivity.COPING_SKILL_KEY, copingSkillWithCustomizations);
            startActivity(intent);
        }
    };


    private void updateGridViews() {
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {
                GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsGridView.setAdapter(new CopingSkillWithCustomizationsIndexAdapter(CopingSkillIndexActivity.this, copingSkillsWithCustomizations, clickListener));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateGridViews();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fabNewCopingSkill = findViewById(R.id.fabNewCopingSkill);
        fabNewCopingSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("activity", "fabNewCopingSkill.onClick");
                if (!activityShouldHandleOnClickEvents()) {
                    Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                    return;
                }
            }
        });
    }


    @Override
    public DrawerTeacherClassroomFragment.Section getSectionForDrawer() {
        return DrawerTeacherClassroomFragment.Section.COPING_SKILLS;
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_coping_skills_index;
    }

}
