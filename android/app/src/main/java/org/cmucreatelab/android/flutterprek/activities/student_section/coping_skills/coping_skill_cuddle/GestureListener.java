package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    private static final int MIN_SWIPPING_DISTANCE = 50;
    private static final int THRESHOLD_VELOCITY = 100;
    private CuddleCopingSkillActivity cuddleCopingSkillActivity;
    private  CuddleCopingSkillAnimation cuddleCopingSkillAnimation;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if ((Math.abs(e1.getX() - e2.getX()) > MIN_SWIPPING_DISTANCE || Math.abs(e2.getX() - e1.getX()) > MIN_SWIPPING_DISTANCE) && Math.abs(velocityX) < THRESHOLD_VELOCITY)
        {
            try {
                cuddleCopingSkillAnimation.startAnimation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public GestureListener(final CuddleCopingSkillActivity cuddleCopingSkillActivity, final CuddleCopingSkillAnimation cuddleCopingSkillAnimation) {
        this.cuddleCopingSkillActivity = cuddleCopingSkillActivity;
        this.cuddleCopingSkillAnimation = cuddleCopingSkillAnimation;
    }
}