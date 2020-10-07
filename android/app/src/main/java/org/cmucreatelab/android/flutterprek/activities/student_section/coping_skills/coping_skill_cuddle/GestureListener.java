package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    private static final int MIN_SWIPPING_DISTANCE = 50;
    //private static final int MAX_SWIPPING_DISTANCE = 250;
    private static final int THRESHOLD_VELOCITY = 1000;
    private CuddleCopingSkillActivity cuddleCopingSkillActivity;
    private  CuddleCopingSkillAnimation cuddleCopingSkillAnimation;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        // Find distance and velocity of swipe
        float dy = e1.getY() - e2.getY();
        float dx = e1.getX() - e2.getY();
        double distance = Math.sqrt((dy*dy + dx*dx));
        double velocity = Math.sqrt(velocityX*velocityX + velocityY*velocityY);

        // Check distance/velocity isn't too short/fast
        if (distance > MIN_SWIPPING_DISTANCE /*&& distance < MAX_SWIPPING_DISTANCE*/ && Math.abs(velocity) < THRESHOLD_VELOCITY)
        {
            cuddleCopingSkillAnimation.startAnimation();
            return false;
        }
        return false;
    }

    public GestureListener(final CuddleCopingSkillActivity cuddleCopingSkillActivity, final CuddleCopingSkillAnimation cuddleCopingSkillAnimation) {
        this.cuddleCopingSkillActivity = cuddleCopingSkillActivity;
        this.cuddleCopingSkillAnimation = cuddleCopingSkillAnimation;
    }
}