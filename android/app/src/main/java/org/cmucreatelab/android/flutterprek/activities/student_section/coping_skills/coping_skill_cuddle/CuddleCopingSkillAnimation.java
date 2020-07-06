package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class CuddleCopingSkillAnimation {

    private BackgroundTimer timerToDisplayHearts;
    private CuddleCopingSkillActivity cuddleCopingSkillActivity;
    private static final long HEART_DELAY = 1000;
    private Display display;
    private ImageView heartImageView;

    public void startAnimation() {
        // Set the position of the heart on screen


        heartImageView.setVisibility(View.VISIBLE);

    }

    public void initAnimation() {
        display = cuddleCopingSkillActivity.getWindowManager().getDefaultDisplay();
        heartImageView = cuddleCopingSkillActivity.findViewById(R.id.heart1);
    }

    public CuddleCopingSkillAnimation(final CuddleCopingSkillActivity cuddleCopingSkillActivity) {
        this.cuddleCopingSkillActivity = cuddleCopingSkillActivity;

        initAnimation();

        timerToDisplayHearts = new BackgroundTimer(HEART_DELAY, new BackgroundTimer.TimeExpireListener(){
            @Override
            public void timerExpired() {
                timerToDisplayHearts.stopTimer();
                startAnimation();
            }
        });
        timerToDisplayHearts.startTimer();
    }
}
