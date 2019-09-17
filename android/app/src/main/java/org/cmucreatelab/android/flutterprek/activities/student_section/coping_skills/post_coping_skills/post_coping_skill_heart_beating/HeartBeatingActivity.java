package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class HeartBeatingActivity extends PostCopingSkillActivity {

    private int itineraryIndex;


    private void goToNextPostCopingSkillActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_heart_beating_how_fast.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_heart_beating;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

        Animation slow = AnimationUtils.loadAnimation(this, R.anim.heart_beat_slow);
        Animation fast = AnimationUtils.loadAnimation(this, R.anim.heart_beat_fast);
        ImageView heartSlow = findViewById(R.id.imageViewBeatingSlow);
        ImageView heartFast = findViewById(R.id.imageViewBeatingFast);

        heartSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().onSelectedHeartBeat("slow");
                goToNextPostCopingSkillActivity();
            }
        });
        heartFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().onSelectedHeartBeat("fast");
                goToNextPostCopingSkillActivity();
            }
        });

        heartSlow.startAnimation(slow);
        heartFast.startAnimation(fast);
    }

}
