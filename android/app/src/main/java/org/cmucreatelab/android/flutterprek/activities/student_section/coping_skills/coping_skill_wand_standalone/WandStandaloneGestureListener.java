package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand.WandCopingSkillActivity;

public class WandStandaloneGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int MIN_SWIPPING_DISTANCE = 50;
    private static final int MAX_SWIPPING_DISTANCE = 250;
    private static final int THRESHOLD_VELOCITY = 1000;
    private WandStandaloneActivity wandStandaloneActivity;
    private TextView debugScreen;

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
        debugScreen.setText("Velocity X: " + Float.toString(velocityX) + "\nVelocity Y: " + Float.toString(velocityY));
        double velocity = Math.sqrt(velocityX*velocityX + velocityY*velocityY);
        Log.e("Swipe ", "Velocity: " + velocity);
        if (distance > MIN_SWIPPING_DISTANCE && distance < MAX_SWIPPING_DISTANCE && Math.abs(velocity) < THRESHOLD_VELOCITY)
        {
            //cuddleCopingSkillAnimation.animateHeart();

            return false;
        }
        return false;
    }

    public WandStandaloneGestureListener(final WandStandaloneActivity wandStandaloneActivity) {
        this.wandStandaloneActivity = wandStandaloneActivity;
        debugScreen = this.wandStandaloneActivity.findViewById(R.id.textViewDebug);
    }

}
