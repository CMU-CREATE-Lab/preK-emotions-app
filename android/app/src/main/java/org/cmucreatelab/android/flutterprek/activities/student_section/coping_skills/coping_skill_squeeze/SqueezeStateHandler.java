package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueeze;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueezeScanner;

import java.util.Random;

public class SqueezeStateHandler implements BleSqueeze.NotificationCallback, UARTConnection.ConnectionListener, BleSqueezeScanner.DiscoveryListener {
    enum State {
        DISCOVERED,
        STOPPED,
        SQUEEZING,
        ACTIVITY_END,
    }

    private static final boolean SHOW_DEBUG_WINDOW = Constants.SQUEEZE_SHOW_DEBUG_WINDOW;

    private final SqueezeCopingSkillActivity activity;
    private final BleSqueezeScanner bleSqueezeScanner;
    private BleSqueeze bleSqueeze;
    private String lastNotification = "";
    private static State currentState = State.STOPPED;
    private static final int numBackgroundImages = 20;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, (float)numBackgroundImages);
    private static final int squeezeThreshold = 5;
    private boolean foundRestState = false;
    private int restStateValue;
    private int lastSqueezeVal;
    private static final long defaultAnimSpeed = 65000L;
    private String balloonAnimateDirection = "left";
    private boolean changedBalloonAnimateDirection = false;

    private void updateDebugWindow() {
        if (SHOW_DEBUG_WINDOW) {
            String display;
            if (bleSqueezeScanner.isSqueezeDiscovered()) {
                display = bleSqueeze.getDeviceName();
                display = display+"\n"+lastNotification;
            } else {
                display = bleSqueezeScanner.isScanning() ? "Looking for Squeeze" : "Inactive";
            }
            final String displayOnUiThread = display;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = activity.findViewById(R.id.textViewDebug);
                    textView.setText(displayOnUiThread);
                }
            });
        }
    }


    private void updateConnectionErrorView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Change this and set error views
                activity.findViewById(R.id.squeezeConnectionError).setVisibility( bleSqueezeScanner.isSqueezeConnected() ? View.GONE : View.VISIBLE);
            }
        });
    }


    private void updateSqueeze(BleSqueeze bleSqueeze) {
        this.bleSqueeze = bleSqueeze;
        this.bleSqueeze.notificationCallback = this;
    }


    private void changeState(SqueezeStateHandler.State newState) {
        currentState = newState;
    }


    private void recalculateRestState(int squeezeVal) {
        // Add some wiggle room for sensor jitter
        if (squeezeVal >= lastSqueezeVal - 2 && squeezeVal <= lastSqueezeVal + 2 && lastSqueezeVal < 998) {
            restStateValue = squeezeVal;
        }
        lastSqueezeVal = squeezeVal;
    }


    private void handleActivityEndState() {
        changeState(State.ACTIVITY_END);
        animator.end();
        SqueezeCopingSkillProcess.releaseTimers();
        activity.findViewById(R.id.gradient).setVisibility(View.GONE);
        activity.findViewById(R.id.gradient_pointer).setVisibility(View.GONE);
        activity.findViewById(R.id.balloon).setVisibility(View.GONE);
        activity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        ((TextView)activity.findViewById(R.id.textViewOverlayTitle)).setText(R.string.coping_skill_squeeze_overlay);
        ((TextView)activity.findViewById(R.id.textViewTitle)).setText("");
    }


    public static State getCurrentState() {
        return currentState;
    }


    public SqueezeStateHandler(SqueezeCopingSkillActivity activity) {
        this.activity = activity;
        this.bleSqueezeScanner = new BleSqueezeScanner(activity, this, this);

        // debug window
        TextView textView = activity.findViewById(R.id.textViewDebug);
        final String displayOnUiThread = "Initialized";
        textView.setText(displayOnUiThread);
        if (!SHOW_DEBUG_WINDOW) {
            textView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onReceivedData(@NonNull String arg1) {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "onReceivedData ignored while activity is paused.");
            return;
        }

        if (SHOW_DEBUG_WINDOW) {
            lastNotification = arg1;
            updateDebugWindow();
        }

        if (currentState == State.ACTIVITY_END) {
            return;
        }

        final int currentSqueezeValue = Integer.parseInt(arg1);

        // What squeeze state the squeezer begins at.
        if (!foundRestState) {
            restStateValue = Integer.parseInt(arg1);
            foundRestState = true;
        } else {
            recalculateRestState(currentSqueezeValue);
            if (activity.squeezeDetectionIsBinary) {
                // NOTE: new squeeze uses buttons so it is "1" when pressed (and 0 otherwise).
                if (currentSqueezeValue > 0) {
                    SqueezeCopingSkillProcess.resetTimers();
                    if (currentState == State.STOPPED) {
                        changeState(State.SQUEEZING);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (animator.isPaused()) {
                                    animator.resume();
                                } else {
                                    animator.start();
                                }
                            }
                        });
                    }
                } else {
                    if (currentState != State.ACTIVITY_END) {
                        changeState(State.STOPPED);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentState != State.ACTIVITY_END) {
                                animator.pause();
                                activity.findViewById(R.id.balloon).setRotation(0);
                            }
                            activity.findViewById(R.id.gradient_pointer).setTranslationY(0);
                        }
                    });
                }
            } else {
                // TODO delete old squeeze detection code below
                if (currentSqueezeValue >= restStateValue + squeezeThreshold) {
                    SqueezeCopingSkillProcess.resetTimers();
                    if (currentState == State.STOPPED) {
                        changeState(State.SQUEEZING);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (animator.isPaused()) {
                                    animator.resume();
                                } else {
                                    animator.start();
                                }
                            }
                        });
                    }

                    // 1040 is a relatively random chosen "max" value observed from the pressure sensor
                    float relativePressureAmount = ((currentSqueezeValue - restStateValue) / (1040f - restStateValue));
                    // change pressure gauge
                    int gradientImageHeight = activity.findViewById(R.id.gradient).getHeight();
                    final float pressureGaugeIndicatorTranslation = Math.min(gradientImageHeight, relativePressureAmount * gradientImageHeight);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.findViewById(R.id.gradient_pointer).setTranslationY(pressureGaugeIndicatorTranslation * -1.0f);
                        }
                    });
                } else {
                    if (currentState != State.ACTIVITY_END) {
                        changeState(State.STOPPED);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentState != State.ACTIVITY_END) {
                                animator.pause();
                                activity.findViewById(R.id.balloon).setRotation(0);
                            }
                            activity.findViewById(R.id.gradient_pointer).setTranslationY(0);
                        }
                    });
                }
            }

        }
    }


    public void initializeSqueezeAnimation() {
        final ImageView[] backgroundImages = new ImageView[numBackgroundImages];
        for(int i = 0; i < numBackgroundImages; i++) {
            backgroundImages[i] = activity.findViewById(activity.getResources().getIdentifier("squeeze_background_" + (i+1), "id", activity.getPackageName()));
        }
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(defaultAnimSpeed);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (currentState == State.ACTIVITY_END) return;
                final float progress = (float) animation.getAnimatedValue();
                // Animate background
                final float height = backgroundImages[0].getHeight();
                final float translationY = height * progress;
                for (int i = 0; i < numBackgroundImages; i++) {
                    // Subtract one pixel so that the split background layers slightly overlap, otherwise a flicker sometimes occurs at the seams
                    backgroundImages[i].setTranslationY(translationY - ((height - 1) * i));
                }
                // Stop animating background roughly 10px before the last image starts going off the screen
                if (progress > 0 && backgroundImages[backgroundImages.length - 1].getTranslationY() >= -10) {
                    handleActivityEndState();
                }
                // Animate balloon
                int firstRandom = new Random().nextInt(2);
                final ImageView balloon = activity.findViewById(R.id.balloon);
                final float balloonRotation = balloon.getRotation();
                if (firstRandom == 1) {
                    if (balloonAnimateDirection.equals("left")) {
                        balloon.setRotation(balloonRotation - 1.0f);
                    } else if (balloonAnimateDirection.equals("right")) {
                        balloon.setRotation(balloonRotation + 1.0f);
                    }
                    if (balloonRotation == -10) {
                        balloonAnimateDirection = "right";
                        changedBalloonAnimateDirection = false;
                    } else if (balloonRotation == 10) {
                        balloonAnimateDirection = "left";
                        changedBalloonAnimateDirection = false;
                    } else if (!changedBalloonAnimateDirection){
                        int secondRandom = new Random().nextInt(15);
                        if (secondRandom == 1) {
                            if (balloonAnimateDirection.equals("right")) {
                                balloonAnimateDirection = "left";
                            } else {
                                balloonAnimateDirection = "right";
                            }
                            changedBalloonAnimateDirection = true;
                        }
                    }
                }
            }
        });
        changeState(State.STOPPED);
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "SqueezeStateHandler: onConnected");
        updateConnectionErrorView();
        updateDebugWindow();
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "SqueezeStateHandler: onDisconnected");
        updateConnectionErrorView();
        lookForSqueeze();
    }


    @Override
    public void onDiscovered(BleSqueeze bleSqueeze) {
        updateSqueeze(bleSqueeze);
        updateDebugWindow();
        changeState(State.DISCOVERED);
    }


    public void lookForSqueeze() {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "lookForSqueeze ignored while activity is paused.");
            return;
        }
        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        if (bleSqueezeScanner.isSqueezeDiscovered()) {
            updateSqueeze(globalHandler.bleSqueeze);
        } else {
            bleSqueezeScanner.requestScan();
        }

        updateConnectionErrorView();
        updateDebugWindow();
    }


    public void initializeState() {
        activity.setScreen();
        changeState(State.STOPPED);
    }


    public void pauseState() {
        bleSqueezeScanner.stopScan();
        changeState(State.STOPPED);
    }

}