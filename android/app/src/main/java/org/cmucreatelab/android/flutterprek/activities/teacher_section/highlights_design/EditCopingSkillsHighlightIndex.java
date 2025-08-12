package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills.CopingSkillEditActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.HighlightsViewDrawer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.List;

public class EditCopingSkillsHighlightIndex extends HighlightsDesignActivityWithHeaderAndDrawer {

    private Classroom classroom;

    public static final String EXTRA_CLASSROOM = "classroom";

    private final CopingSkillWithCustomizationsIndexAdapter.ClickListener clickListener = new CopingSkillWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(CopingSkillWithCustomizations copingSkillWithCustomizations, List<ItineraryItem> itineraryItems, View view) {
            Intent intent = new Intent(EditCopingSkillsHighlightIndex.this, CopingSkillEditActivity.class);
            intent.putExtra(CopingSkillEditActivity.COPING_SKILL_KEY, copingSkillWithCustomizations);
            startActivity(intent);
        }
    };


    private void updateGridViews() {
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {
                GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsGridView.setAdapter(new CopingSkillWithCustomizationsIndexAdapter(EditCopingSkillsHighlightIndex.this, copingSkillsWithCustomizations, clickListener));
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
        setUpDrawer();

        // get classroom and update the drawer
        this.classroom = (Classroom) getIntent().getSerializableExtra(EXTRA_CLASSROOM);
        getDrawerHighlights().setClassroom(classroom);
        getDrawerHighlights().setHighlighted(HighlightsViewDrawer.Row.CLASS_SHOW);
        setBackNavigationForDrawer(true, String.format("Back to %s", (classroom == null) ? "Classes" : classroom.getName()), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                if (classroom != null) {
//                    Intent intent = new Intent(EditCopingSkillsHighlightIndex.this, ClassroomHighlightsActivity.class);
//                    intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                } else {
//                    Log.e(Constants.LOG_TAG, "EditCopingSkillsHighlightIndex navigate back in HighlightsViewDrawer but classroom is null; default to classes index.");
//                    Intent intent = new Intent(EditCopingSkillsHighlightIndex.this, org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomIndexActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
            }
        });

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


//    @Override
//    public DrawerTeacherClassroomFragment.Section getSectionForDrawer() {
//        return DrawerTeacherClassroomFragment.Section.COPING_SKILLS;
//    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_coping_skills_index;
    }

}



