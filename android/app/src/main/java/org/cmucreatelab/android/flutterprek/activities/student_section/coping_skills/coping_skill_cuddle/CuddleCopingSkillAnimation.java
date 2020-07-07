package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

import android.graphics.Point;
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
    private static final long TEMPO = 500;
    private Display display;
    private ImageView heartImageView;

    public void startAnimation() {
        // Set the position of the heart on screen
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float xPos = (float) (0.29 * (float) width);
        float yPos = (float) (0.447 * (float) height);

        heartImageView.setX(xPos);
        heartImageView.setY(yPos);

        heartImageView.setVisibility(View.VISIBLE);

        // Rotate and move up the screen
        AnimationSet heartMov = new AnimationSet(true);

        RotateAnimation rot = new RotateAnimation(0, 15,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                1.0f);
        rot.setDuration(TEMPO);
        rot.setRepeatCount(1);
        rot.setRepeatMode(2);
        heartMov.addAnimation(rot);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 10, 0.0f, 0.0f);
        animation.setDuration(TEMPO);
        animation.setRepeatCount(1);
        animation.setRepeatMode(2);
        heartMov.addAnimation(animation);
        heartImageView.startAnimation(heartMov);

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
