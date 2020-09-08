package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class CuddleCopingSkillAnimation {

    private BackgroundTimer timerToDisplayHearts;
    private BackgroundTimer timerForFade;
    private CuddleCopingSkillActivity cuddleCopingSkillActivity;
    private static final long HEART_DELAY = 1000;
    private static final long TEMPO = 500;
    private static final long FADE_DURATION = 500;
    private static final long TRANS_DURATION = 1000;
    private Display display;
    private ImageView heartImageView;
    private ImageView sheepImageView;
    private Animation fadeIn;
    private boolean timer_trigger = false;
    private GestureDetector gdt;
    private int right;
    private float xPos;

    public void animateHeart() {
        Random rnd = new Random();
        right = rnd.nextInt(2);

        // Set the position of the heart on screen
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final int height = size.y;
        if (right == 0) {
            xPos = (float) (0.29 * (float) width);
        } else {
            xPos = (float) (0.599 * (float) width);
        }
        final float yPos = (float) (0.447 * (float) height);

        heartImageView.setX(xPos);
        heartImageView.setY(yPos);

        heartImageView.setVisibility(View.VISIBLE);

        AnimationSet heartAnimation = new AnimationSet(true);
        fadeIn = AnimationUtils.loadAnimation(cuddleCopingSkillActivity.getApplicationContext(), R.anim.heart_fade_in);
        heartAnimation.addAnimation(fadeIn);

        Animation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f
        );
        translate.setDuration(TRANS_DURATION);
        heartAnimation.addAnimation(translate);

        Animation fadeOut = AnimationUtils.loadAnimation(cuddleCopingSkillActivity.getApplicationContext(),R.anim.heart_fade_out);
        heartAnimation.addAnimation(fadeOut);

        heartImageView.startAnimation(heartAnimation);

        new CountDownTimer(FADE_DURATION+TRANS_DURATION, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.e("Timer", "Done moving");
                //yPos = (float) (0.447 * (float) height) - 200;
                heartImageView.setX(xPos);
                heartImageView.setY(yPos-200);
                new CountDownTimer(FADE_DURATION, 500) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Log.e("Timer", "Done fading");
                        heartImageView.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        }.start();
    }

    public void startAnimation() throws InterruptedException {
        Log.e("Animation", "Starting animation");

        // Set side of the screen
        Random rnd = new Random();
        right = rnd.nextInt(2);

        // Set the position of the heart on screen
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (right == 0) {
            xPos = (float) (0.29 * (float) width);
        } else {
            xPos = (float) (0.599 * (float) width);
        }
        float yPos = (float) (0.447 * (float) height);
        heartImageView.setX(xPos);
        heartImageView.setY(yPos);

        // Set the size of the heart
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) heartImageView.getLayoutParams();
        params.width = rnd.nextInt(100) + 100;
        heartImageView.setLayoutParams(params);

        // Set the view to be visible
        heartImageView.setVisibility(View.VISIBLE);

        fadeIn = AnimationUtils.loadAnimation(cuddleCopingSkillActivity.getApplicationContext(), R.anim.heart_fade_in);
        heartImageView.startAnimation(fadeIn);

        new CountDownTimer(FADE_DURATION, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.e("Timer", "Done fading in");
                moveHeart();
            }
        }.start();
    }

    public void moveHeart(){
        stopAnimation(heartImageView);
        Log.e("Move", "Clear");

        AnimationSet heartMov = new AnimationSet(true);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) heartImageView.getLayoutParams();
        float xPivot = params.width/2;
        float yPivot = params.height/2;
        RotateAnimation rot = new RotateAnimation(0, -15,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rot.setDuration(TEMPO);
        rot.setRepeatCount(0);
        //heartMov.addAnimation(rot);


        TranslateAnimation animation = new TranslateAnimation(0.0f, 0, 0.0f, -200f);
        animation.setDuration(TRANS_DURATION);
        animation.setRepeatCount(0);
        heartMov.addAnimation(animation);
        heartMov.setStartOffset(0);
        heartImageView.startAnimation(heartMov);
        /*
        ObjectAnimator animation = ObjectAnimator.ofFloat(heartImageView, "translationY", -200f);
        animation.setDuration(1000);
        animation.start();

         */
        new CountDownTimer(TRANS_DURATION, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.e("Timer", "Done moving");
                fadeHeartOut();
            }
        }.start();

    }

    public void fadeHeartOut() {
        stopAnimation(heartImageView);

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float yPos = (float) (0.447 * (float) height) - 200;
        heartImageView.setX(xPos);
        heartImageView.setY(yPos);

        Animation fadeOut = AnimationUtils.loadAnimation(cuddleCopingSkillActivity.getApplicationContext(),R.anim.heart_fade_out);
        heartImageView.startAnimation(fadeOut);
        new CountDownTimer(FADE_DURATION, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.e("Timer", "Done moving");
                heartImageView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    public void stopAnimation(View v) {
        v.clearAnimation();
        if (canCancelAnimation()) {
            v.animate().cancel();
        }
    }

    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public void initAnimation() {
        display = cuddleCopingSkillActivity.getWindowManager().getDefaultDisplay();
        heartImageView = cuddleCopingSkillActivity.findViewById(R.id.heart1);
    }

    private void initSheep(){
        sheepImageView = cuddleCopingSkillActivity.findViewById(R.id.sheep);
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float xPos = (float) (0.394 * (float) width);
        float yPos = (float) (0.64 * (float) height);

        sheepImageView.setX(xPos);
        sheepImageView.setY(yPos);

        sheepImageView.setVisibility(View.VISIBLE);
    }

    private void initTouch(){
        gdt = new GestureDetector(new GestureListener(cuddleCopingSkillActivity, this));
        sheepImageView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            } });
    }

    public CuddleCopingSkillAnimation(final CuddleCopingSkillActivity cuddleCopingSkillActivity) {
        this.cuddleCopingSkillActivity = cuddleCopingSkillActivity;

        initAnimation();
        initSheep();
        initTouch();

        /*new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.e("Timer", "Done waiting on start");
                try {
                    startAnimation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
         */
    }
}


