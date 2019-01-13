package org.cmucreatelab.android.flutterprek.activities.coping_skill_static;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class StaticCopingSkillActivity extends AbstractCopingSkillActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_coping_skill);

        // close
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.LOG_TAG, "clicked buttonClose; now finishing activity");
                //finish();
                Intent chooseStudentActivity = new Intent(StaticCopingSkillActivity.this, ChooseStudentActivity.class);
                chooseStudentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(chooseStudentActivity);
            }
        });

        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO reset
            }
        });
        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent chooseStudentActivity = new Intent(StaticCopingSkillActivity.this, ChooseStudentActivity.class);
                chooseStudentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(chooseStudentActivity);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }

}
