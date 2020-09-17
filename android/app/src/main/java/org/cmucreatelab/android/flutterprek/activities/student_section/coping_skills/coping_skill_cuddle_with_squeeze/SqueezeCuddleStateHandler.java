package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze;

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
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze.SqueezeCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze.SqueezeWriteTimer;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueeze;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueezeScanner;

import java.util.Random;

public class SqueezeCuddleStateHandler implements BleSqueeze.NotificationCallback, UARTConnection.ConnectionListener, BleSqueezeScanner.DiscoveryListener {

    enum State {
        DISCOVERED,
        STOPPED,
        SQUEEZING,
        ACTIVITY_END,
    }

    private static final boolean SHOW_DEBUG_WINDOW = Constants.SQUEEZE_SHOW_DEBUG_WINDOW;
    private State currentState = State.STOPPED;
//    private static final int numBackgroundImages = 20;
//    private static final int squeezeThreshold = 5;
//    private static final long defaultAnimSpeed = 55000L;

    private final SqueezeCuddleCopingSkillActivity activity;
    private final BleSqueezeScanner bleSqueezeScanner;
    private final SqueezeCuddleWriteTimer squeezeWriteTimer;
    private BleSqueeze bleSqueeze;
    private String lastNotification = "";
//    private boolean foundRestState = false;
//    private int restStateValue;
//    private int lastSqueezeVal;
//    private String balloonAnimateDirection = "left";
//    private boolean changedBalloonAnimateDirection = false;

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
        updateConnectionErrorView( bleSqueeze == null || !bleSqueeze.isConnected());
    }


    private void updateConnectionErrorView(final boolean isVisible) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.buttonConnectionError).setVisibility( isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    private void updateSqueeze(BleSqueeze bleSqueeze) {
        this.bleSqueeze = bleSqueeze;
        this.bleSqueeze.notificationCallback = this;
    }


    private void changeState(SqueezeCuddleStateHandler.State newState) {
        currentState = newState;
    }


    private void handleActivityEndState() {
        changeState(State.ACTIVITY_END);
        // TODO overlays
    }


    public State getCurrentState() {
        return currentState;
    }


    public SqueezeCuddleStateHandler(SqueezeCuddleCopingSkillActivity activity) {
        this.activity = activity;
        this.bleSqueezeScanner = new BleSqueezeScanner(activity, this, this);
        this.squeezeWriteTimer = new SqueezeCuddleWriteTimer(this);

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

        if (currentSqueezeValue > 0) {
            // TODO actions for squeeze
            Log.v(Constants.LOG_TAG, "S Q U E E Z E");
        }
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "SqueezeStateHandler: onConnected");
        squeezeWriteTimer.startTimer();
        updateConnectionErrorView(false);
        updateDebugWindow();
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "SqueezeStateHandler: onDisconnected");
        updateConnectionErrorView(true);
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


    public BleSqueeze getBleSqueeze() {
        return bleSqueeze;
    }

}