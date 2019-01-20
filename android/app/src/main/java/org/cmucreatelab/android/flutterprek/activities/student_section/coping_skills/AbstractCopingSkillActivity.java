package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.HeartBeatingActivity;

public abstract class AbstractCopingSkillActivity extends AbstractActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // close
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.LOG_TAG, "clicked buttonClose; now finishing activity");
                finish();
            }
        });
    }


    /**
     * Hide navigation buttons to make the activity take up the entire screen.
     */
    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }


//    /**
//     * When finishing Coping Skill activities, always return to the Choose Student page.
//     */
//    @Override
//    public void finish() {
//        Intent chooseStudentActivity = new Intent(this, ChooseStudentActivity.class);
//        chooseStudentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(chooseStudentActivity);
//    }
    /**
     * When finishing Coping Skill activities, begin the follow-up coping skill prompts.
     */
    @Override
    public void finish() {
        // heart beating
        Intent heartBeatingActivity = new Intent(this, HeartBeatingActivity.class);
        heartBeatingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(heartBeatingActivity);
    }

}
