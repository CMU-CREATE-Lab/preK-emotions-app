package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.UseWordsFragment;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class HeartRateFragment extends AnimateFragment {


    private int itineraryIndex;


    @Override
    public int getInflatedLayoutResource() {
        return R.layout._coping_skill__activity_heart_beating;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

    }

    @Override
    public void initializeFragment() {

        final AnimateFragment.ActivityCallback activityCallback = getActivityCallback();

        Animation slow = AnimationUtils.loadAnimation(this, R.anim.heart_beat_slow);
        Animation fast = AnimationUtils.loadAnimation(this, R.anim.heart_beat_fast);
        ImageView heartSlow = getFragmentView().findViewById(R.id.imageViewBeatingSlow);
        ImageView heartFast = getFragmentView().findViewById(R.id.imageViewBeatingFast);

        heartSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (slow)
                goToNextPostCopingSkillActivity();
            }
        });
        heartFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (fast)
                goToNextPostCopingSkillActivity();
            }
        });

        heartSlow.startAnimation(slow);
        heartFast.startAnimation(fast);

    }


    @Override
    public void displayFragment(AnimateFragment.FragmentState fragmentStateToDisplay, AnimateFragment.ActivityCallback activity) {
        super.displayFragment(fragmentStateToDisplay, activity);
        boolean display = fragmentStateToDisplay == AnimateFragment.FragmentState.PLAY;
        getFragmentView().setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            initializeFragment();
        }
    }

    private void goToNextPostCopingSkillActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
    }


    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_heart_beating_how_fast.wav";
    }



    }

}
