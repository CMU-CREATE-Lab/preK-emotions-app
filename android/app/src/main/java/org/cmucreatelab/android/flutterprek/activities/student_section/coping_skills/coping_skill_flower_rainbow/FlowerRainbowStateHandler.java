package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlowerScanner;

public class FlowerRainbowStateHandler implements BleFlower.NotificationCallback, FlowerRainbowBreathTracker.Listener, UARTConnection.ConnectionListener, BleFlowerScanner.DiscoveryListener {

    enum State {
        WAIT_FOR_BUTTON,
        BREATHING,
        FINISHED
    }

    public interface BleBreathListener {
        void onReceivedData(boolean flowerDetectsBreathing);
    }

    private static final boolean SHOW_DEBUG_WINDOW = Constants.FLOWER_SHOW_DEBUG_WINDOW;

    private final FlowerRainbowCopingSkillActivity activity;
    private final FlowerRainbowBreathTracker breathTracker;
    private final BleFlowerScanner bleFlowerScanner;
    private final FlowerRainbowWriteTimer flowerWriteTimer;
    private boolean isPressingButton = false;
    private BleFlower bleFlower;
    private BleBreathListener bleBreathListener;


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
            // start breathing out instead
            activity.displayBreatheOut();
            //activity.displayBreatheIn();
            breathTracker.startTracker();
        } else if (newState == State.FINISHED) {
            breathTracker.resetTracker();
            activity.displayOverlay();
        }
    }


    public FlowerRainbowStateHandler(FlowerRainbowCopingSkillActivity activity) {
        this(activity, null);
    }


    public FlowerRainbowStateHandler(FlowerRainbowCopingSkillActivity activity, BleBreathListener bleBreathListener) {
        this.activity = activity;
        this.bleBreathListener = bleBreathListener;
        this.breathTracker = new FlowerRainbowBreathTracker(activity, this);
        this.bleFlowerScanner = new BleFlowerScanner(activity, this, this);
        this.flowerWriteTimer = new FlowerRainbowWriteTimer(this);

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
        boolean flowerDetectsBreathing = arg4.equals("1");

        // update led count for activity
        try {
            int ledCount = Integer.parseInt(arg5);
            // forces interpret value between 0 and 12
            activity.ledCountOnFlower = Math.max(Math.min(ledCount, 12), 0);
        } catch (NumberFormatException e) {
            Log.e(Constants.LOG_TAG, String.format("Failed to parge ledCount '%s'; will default to 0", arg5));
            activity.ledCountOnFlower = 0;
            e.printStackTrace();
        }

        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3+","+arg4+","+arg5+"\n\n BREATH: "+activity.breathCount;
            updateDebugWindow(reformedData);
        }

        if (bleBreathListener != null) {
            bleBreathListener.onReceivedData(flowerDetectsBreathing);
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
        // NOTE: avoid using UI thread for ble scans/connection
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
                if (bleFlowerScanner.isFlowerDiscovered()) {
                    updateFlower(globalHandler.bleFlower);
                } else {
                    bleFlowerScanner.requestScan();
                }

                updateConnectionErrorView();
                updateDebugWindow("");
            }
        });
    }


    public void initializeState() {
        changeState(State.BREATHING);
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
