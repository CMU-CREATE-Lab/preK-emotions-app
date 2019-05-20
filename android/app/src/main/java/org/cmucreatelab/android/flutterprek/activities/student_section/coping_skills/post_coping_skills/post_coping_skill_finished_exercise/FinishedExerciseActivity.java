package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_finished_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class FinishedExerciseActivity extends PostCopingSkillActivity {

    private int itineraryIndex;


    private void goToNextPostCopingSkillActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
    }


    // TODO hardcoded for now but should be defined elsewhere in the future
    private void determineDisplayForFeelingUnchanged() {
        String emotionUuid = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionUuid;
        int imageResource;
        String title;

        if (emotionUuid.equals("emotion2")) {
            // sad
            imageResource = R.drawable.ic_sad;
            title = "Sad";
        } else if (emotionUuid.equals("emotion3")) {
            // mad
            imageResource = R.drawable.ic_mad;
            title = "Mad";
        } else if (emotionUuid.equals("emotion5")) {
            imageResource = R.drawable.ic_scared;
            title = "Scared";
        } else {
            // hide by default (happy)
            findViewById(R.id.viewFeelingUnchanged).setVisibility(View.GONE);
            return;
        }
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(imageResource);
        ((TextView)findViewById(R.id.text1)).setText(title);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_finished_exercise.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_finished_exercise;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

        determineDisplayForFeelingUnchanged();

        findViewById(R.id.viewFeelingUnchanged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (unchanged)
                goToNextPostCopingSkillActivity();
            }
        });
        findViewById(R.id.viewFeelingOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (ok)
                goToNextPostCopingSkillActivity();
            }
        });
        findViewById(R.id.viewFeelingHappy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (happy)
                goToNextPostCopingSkillActivity();
            }
        });
    }

}
