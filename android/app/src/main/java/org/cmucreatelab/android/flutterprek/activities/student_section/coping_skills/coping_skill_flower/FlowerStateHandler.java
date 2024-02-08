package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlowerScanner;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class FlowerStateHandler implements BleFlower.NotificationCallback, FlowerBreathTracker.Listener, UARTConnection.ConnectionListener, BleFlowerScanner.DiscoveryListener {

    enum State {
        WAIT_FOR_BUTTON,
        BREATHING,
        FINISHED
    }

    private static final boolean SHOW_DEBUG_WINDOW = Constants.FLOWER_SHOW_DEBUG_WINDOW;

    private final FlowerCopingSkillActivity activity;
    private final FlowerBreathTracker breathTracker;
    private final BleFlowerScanner bleFlowerScanner;
    private final FlowerWriteTimer flowerWriteTimer;
    private boolean isPressingButton = false;
    private BleFlower bleFlower;


    private void updateDebugWindow(String message) {
        if (SHOW_DEBUG_WINDOW) {
            String display;
            if (bleFlowerScanner.isFlowerDiscovered()) {
                display = bleFlower.getDeviceName();
                display = display + "\n" + ((message == null) ? "" : message);
            } else {
                display = bleFlowerScanner.isScanning() ? "Looking for Flower..." : "Inactive";
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
        updateConnectionErrorView(bleFlower == null || !bleFlower.isConnected());
    }

    private void updateConnectionErrorView(final boolean isVisible) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.buttonConnectionError).setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    private void updateFlower(BleFlower bleFlower) {
        this.bleFlower = bleFlower;
        this.bleFlower.notificationCallback = this;
    }


    private void changeState(State newState) {
        if (newState == State.WAIT_FOR_BUTTON) {
            activity.displayHoldFlowerInstructions();
            breathTracker.resetTracker();
            // clear this flag (in case button was held down before entering this state)
            isPressingButton = false;
        } else if (newState == State.BREATHING) {
            activity.displayBreatheIn();
            breathTracker.startTracker();
        } else if (newState == State.FINISHED) {
            breathTracker.resetTracker();
            activity.displayOverlay();
        }
    }


    public FlowerStateHandler(FlowerCopingSkillActivity activity) {
        this.activity = activity;
        this.breathTracker = new FlowerBreathTracker(activity, this);
        this.bleFlowerScanner = new BleFlowerScanner(activity, this, this);
        this.flowerWriteTimer = new FlowerWriteTimer(this);

        // debug window
        TextView textView = activity.findViewById(R.id.textViewDebug);
        if (!SHOW_DEBUG_WINDOW) {
            textView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onFinishedBreathing() {
        changeState(State.FINISHED);
    }


    @Override
    public void onReceivedData(String arg1, String arg2, String arg3, String arg4, String arg5) {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "onReceivedData ignored while activity is paused.");
            return;
        }

        boolean newValue = arg2.equals("1");

        // only listen for when the button is first pressed
        if (!isPressingButton && newValue) {
            isPressingButton = true;
            changeState(State.BREATHING);
        }

        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3+","+arg4;
            updateDebugWindow(reformedData);
        }
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "FlowerStateHandler: onConnected");
        flowerWriteTimer.startTimer();
        updateConnectionErrorView(false);
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "FlowerStateHandler: onDisconnected");
        // override (because we know it disconnected)
        updateConnectionErrorView(true);
        lookForFlower();
    }


    @Override
    public void onDiscovered(BleFlower bleFlower) {
        updateFlower(bleFlower);
        updateDebugWindow("");
    }


    public void lookForFlower() {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "lookForFlower ignored while activity is paused.");
            return;
        }
        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        if (bleFlowerScanner.isFlowerDiscovered()) {
            updateFlower(globalHandler.bleFlower);
        } else {
            bleFlowerScanner.requestScan();
        }

        updateConnectionErrorView();
        updateDebugWindow("");
    }


    public void initializeState() {
        changeState(State.WAIT_FOR_BUTTON);
    }


    public void pauseState() {
        bleFlowerScanner.stopScan();
        // NOTE: we do not call changeState() method since it plays the audio prompt while in the background.
        //changeState(State.WAIT_FOR_BUTTON);
        breathTracker.resetTracker();
        // clear this flag (in case button was held down before entering this state)
        isPressingButton = false;
    }


    public BleFlower getBleFlower() {
        return bleFlower;
    }

}
