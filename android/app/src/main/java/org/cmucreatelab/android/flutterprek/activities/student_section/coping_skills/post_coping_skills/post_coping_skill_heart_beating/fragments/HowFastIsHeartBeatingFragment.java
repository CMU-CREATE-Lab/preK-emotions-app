package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;

public class HowFastIsHeartBeatingFragment extends HeartBeatingFragment  {

    @Override
    public int getInflatedLayoutResource() {
        return R.layout._coping_skill__fragment_how_fast_is_heart_beating;
    }


    @Override
    public void initializeFragment() {
        View fragmentView = getFragmentView();
        Animation slow = AnimationUtils.loadAnimation(getActivity(), R.anim.heart_beat_slow);
        Animation fast = AnimationUtils.loadAnimation(getActivity(), R.anim.heart_beat_fast);
        ImageView heartSlow = fragmentView.findViewById(R.id.imageViewBeatingSlow);
        ImageView heartFast = fragmentView.findViewById(R.id.imageViewBeatingFast);

        final HeartBeatingFragment.ActivityCallback activityCallback = getActivityCallback();
        getFragmentView().findViewById(R.id.imageViewBeatingSlow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalHandler.getInstance(getActivity().getApplicationContext()).getSessionTracker().onSelectedHeartBeat("slow");
                activityCallback.goToNextActivity();
            }
        });
        getFragmentView().findViewById(R.id.imageViewBeatingFast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalHandler.getInstance(getActivity().getApplicationContext()).getSessionTracker().onSelectedHeartBeat("fast");
                activityCallback.goToNextActivity();
            }
        });

        heartSlow.startAnimation(slow);
        heartFast.startAnimation(fast);
    }
}
