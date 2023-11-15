package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.animation.Animator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class RainbowCreator implements FlowerRainbowStateHandler.BleBreathListener {

    private FlowerRainbowCopingSkillActivity activity;
    private boolean rainbowIsGrowing = false;


    public RainbowCreator(FlowerRainbowCopingSkillActivity activity) {
        this.activity = activity;

        ImageView rainbowView = activity.findViewById(R.id.imageViewRainbow);
        rainbowView.setAlpha(0f);
        Drawable drawable = rainbowView.getDrawable();
        if (drawable instanceof ClipDrawable) {
            ((ClipDrawable)drawable).setLevel(5000);
        }
    }


    private void animateRainbowFadeIn() {
        ImageView rainbowView = activity.findViewById(R.id.imageViewRainbow);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rainbowView.animate().alpha(1.0f).setDuration(2000);
            }
        });
    }


    private void animateRainbowFadeOut() {
        ImageView rainbowView = activity.findViewById(R.id.imageViewRainbow);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rainbowView.animate().alpha(0f).setDuration(1000);
            }
        });
    }


    private void animateRainbowCircularReveal() {
        ImageView rainbowView = activity.findViewById(R.id.imageViewRainbow);

        // Check whether the runtime version is at least Android 5.0.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the center for the clipping circle.
            int cx = rainbowView.getWidth() / 2;
            int cy = rainbowView.getHeight() / 2;

            // Get the final radius for the clipping circle.
            float finalRadius = (float) Math.hypot(cx, cy);

            // Create the animator for this view. The start radius is 0.
            Animator anim = ViewAnimationUtils.createCircularReveal(rainbowView, cx, cy, 0f, finalRadius);

            // Make the view visible and start the animation.
            rainbowView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // Set the view to invisible without a circular reveal animation below
            // Android 5.0.
            rainbowView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onReceivedData(boolean flowerDetectsBreathing) {
        if (flowerDetectsBreathing) {
            if (!rainbowIsGrowing) {
                Log.v(Constants.LOG_TAG, "set rainbowIsGrowing to TRUE");
                rainbowIsGrowing = true;
                animateRainbowFadeIn();
            }
        } else {
            if (rainbowIsGrowing) {
                Log.v(Constants.LOG_TAG, "set rainbowIsGrowing to FALSE");
                rainbowIsGrowing = false;
                animateRainbowFadeOut();
            }
        }
    }

}
