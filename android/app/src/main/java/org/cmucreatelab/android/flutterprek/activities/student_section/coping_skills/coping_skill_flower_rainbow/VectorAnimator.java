package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import java.util.ArrayList;

public class VectorAnimator {

    private AbstractActivity activity;
    private ArrayList<ImageView> imageViewsToAnimate;
    private AnimationListener animationListener;

    private static int starAnimationResource = R.anim.star_anim2;
    private static long delayBetweenStarAnimationsInMilliseconds = 40;

    private static class AnimatedVector {
        public ImageView imageView;
        public Animation anim;

        public AnimatedVector(Context context, ImageView imageView) {
            this.imageView = imageView;
//            this.scaleFast = AnimationUtils.loadAnimation(context, R.anim.star_scale_fast);
//            this.rotate = AnimationUtils.loadAnimation(context, R.anim.star_rotate);
            this.anim = AnimationUtils.loadAnimation(context, starAnimationResource);
        }

        public void startAnimation() {
//            AnimationSet animationSet = new AnimationSet(false);
//            animationSet.addAnimation(scaleFast);
//            animationSet.addAnimation(rotate);
//            imageView.startAnimation(animationSet);
            imageView.startAnimation(anim);
        }
    }

    public interface AnimationListener {
        /**
         * Notifies when every animation is started
         * @param vectorAnimator the instance that the callback is related to
         */
        void OnAllAnimationsStarted(VectorAnimator vectorAnimator);
    }


    public VectorAnimator(AbstractActivity activity) {
        this(activity, null);
    }


    public VectorAnimator(AbstractActivity activity, AnimationListener animationListener) {
        this.activity = activity;
        this.imageViewsToAnimate = new ArrayList<>();
        this.animationListener = animationListener;
    }


    public void addImageView(ImageView imageView) {
        imageViewsToAnimate.add(imageView);
    }


    public void startAnimations() {
        //startAnimations(12);
        startAnimations(imageViewsToAnimate.size());
    }


    public void startAnimations(int numberOfStars) {
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
        int starCount = 0;
        AnimatedVector lastAnimatedVector = null;
        for (AnimatedVector animatedVector: listAnimatedVectors) {
            starCount++;
            if (starCount > numberOfStars) {
                break;
            }
            lastAnimatedVector = animatedVector;
            toRun.add(new Runnable() {
                @Override
                public void run() {
                    //final AnimatedVector animatedVector = new AnimatedVector(activity.getApplicationContext(), imageView);
                    animatedVector.imageView.setVisibility(View.VISIBLE);
                    animatedVector.startAnimation();
                }
            });
        }
        lastAnimatedVector.anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // does nothing
                if (animationListener != null) animationListener.OnAllAnimationsStarted(VectorAnimator.this);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // callback that all animations have ended
                //if (animationListener != null) animationListener.onAnimationEnd(VectorAnimator.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // does nothing
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int delay = 0;
                for (Runnable runnable: toRun) {
                    delay += delayBetweenStarAnimationsInMilliseconds;
                    handler.postDelayed(runnable, delay);
                }
            }
        });
    }

}
