package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import java.util.ArrayList;

public class VectorAnimator {

    private AbstractActivity activity;
    private ArrayList<ImageView> imageViewsToAnimate;

    private static class AnimatedVector {
        public ImageView imageView;
        public Animation rotate, scaleFast, anim1;

        public AnimatedVector(Context context, ImageView imageView) {
            this.imageView = imageView;
            this.scaleFast = AnimationUtils.loadAnimation(context, R.anim.star_scale_fast);
            this.rotate = AnimationUtils.loadAnimation(context, R.anim.star_rotate);
            this.anim1 = AnimationUtils.loadAnimation(context, R.anim.star_anim1);
            // TODO set/handle AnimationListener?
        }

        public void startAnimation() {
//            AnimationSet animationSet = new AnimationSet(false);
//            animationSet.addAnimation(scaleFast);
//            animationSet.addAnimation(rotate);
//            imageView.startAnimation(animationSet);
            imageView.startAnimation(anim1);
        }
    }


    public VectorAnimator(AbstractActivity activity) {
        this.activity = activity;
        this.imageViewsToAnimate = new ArrayList<>();
    }


    public void addImageView(ImageView imageView) {
        imageViewsToAnimate.add(imageView);
    }


    public void startAnimations() {
        // TODO AnimatedVector instances from imageViewsToAnimate
        final Handler handler = new Handler();
        final ArrayList<Runnable> toRun = new ArrayList<>();
        final ArrayList<AnimatedVector> listAnimatedVectors = new ArrayList<>();

        for (ImageView imageView: imageViewsToAnimate) {
            AnimatedVector animatedVector = new AnimatedVector(activity.getApplicationContext(), imageView);
            listAnimatedVectors.add(animatedVector);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ImageView imageView: imageViewsToAnimate) {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });

        //for (ImageView imageView: imageViewsToAnimate) {
        for (AnimatedVector animatedVector: listAnimatedVectors) {
            toRun.add(new Runnable() {
                @Override
                public void run() {
                    //final AnimatedVector animatedVector = new AnimatedVector(activity.getApplicationContext(), imageView);
                    animatedVector.imageView.setVisibility(View.VISIBLE);
                    animatedVector.startAnimation();
                }
            });
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int delay = 0;
                for (Runnable runnable: toRun) {
                    delay += 80;
                    handler.postDelayed(runnable, delay);
                }
            }
        });
    }

}
