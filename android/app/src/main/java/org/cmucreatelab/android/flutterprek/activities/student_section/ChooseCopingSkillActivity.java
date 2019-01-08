package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.StudentSectionNavigationHandler;
import org.cmucreatelab.android.flutterprek.activities.coping_skill_flower.FlowerCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;

import java.util.List;

public class ChooseCopingSkillActivity extends StudentSectionActivityWithHeader {

    private final CopingSkillIndexAdapter.ClickListener listener = new CopingSkillIndexAdapter.ClickListener() {
        @Override
        public void onClick(CopingSkill copingSkill) {
            Log.d(Constants.LOG_TAG, "onClick coping skill = " + copingSkill.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.copingSkillUuid = copingSkill.getUuid();
            // TODO replace with coping skill lookup; hardcoded coping skill for now
            Intent flowerCopingSKillActivity = new Intent(ChooseCopingSkillActivity.this, FlowerCopingSkillActivity.class);
            startActivity(flowerCopingSKillActivity);
        }
    };


    private LiveData<List<CopingSkill>> getLiveDataFromQuery(String classroomUuid, String studentUuid, String emotionUuid) {
        // TODO determine coping skills to display based on current student/emotion
        return AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkills();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StudentSectionNavigationHandler navigationHandler = GlobalHandler.getInstance(this).studentSectionNavigationHandler;
        LiveData<List<CopingSkill>> liveData = getLiveDataFromQuery(navigationHandler.classroomUuid, navigationHandler.studentUuid, navigationHandler.emotionUuid);
        liveData.observe(this, new Observer<List<CopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkill> copingSkills) {
                GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsGridView.setAdapter(new CopingSkillIndexAdapter(ChooseCopingSkillActivity.this, copingSkills, listener));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_coping_skill;
    }

}
