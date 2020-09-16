package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    private static final int MIN_SWIPPING_DISTANCE = 50;
    private static final int MAX_SWIPPING_DISTANCE = 250;
    private static final int THRESHOLD_VELOCITY = 1000;
    private CuddleCopingSkillActivity cuddleCopingSkillActivity;
    private  CuddleCopingSkillAnimation cuddleCopingSkillAnimation;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        Log.e("Swipe ", "X1: " + e1.getX());
        Log.e("Swipe ", "X2: " + e2.getX());
        Log.e("Swipe ", "Y1: " + e1.getY());
        Log.e("Swipe ", "Y2: " + e2.getY());
        float dy = e1.getY() - e2.getY();
        float dx = e1.getX() - e2.getY();
        double distance = Math.sqrt((dy*dy + dx*dx));
        Log.e("Swipe ", "Distance : " + distance);
        Log.e("Swipe ", "VelocityX: " + velocityX);
        Log.e("Swipe ", "VelocityY: " + velocityY);
        double velocity = Math.sqrt(velocityX*velocityX + velocityY*velocityY);
        Log.e("Swipe ", "Velocity: " + velocity);
        if (distance > MIN_SWIPPING_DISTANCE && distance < MAX_SWIPPING_DISTANCE && Math.abs(velocity) < THRESHOLD_VELOCITY)
        {
            //cuddleCopingSkillAnimation.animateHeart();
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