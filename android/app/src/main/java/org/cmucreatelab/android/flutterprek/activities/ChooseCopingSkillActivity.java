package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;

import java.util.List;

public class ChooseCopingSkillActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coping_skill);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkills().observe(this, new Observer<List<CopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkill> copingSkills) {
                GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsGridView.setAdapter(new CopingSkillIndexAdapter(ChooseCopingSkillActivity.this, copingSkills));
            }
        });
    }

}
