package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWand;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWandScanner;

public class WandStateHandler implements BleWand.NotificationCallback, UARTConnection.ConnectionListener, BleWandScanner.DiscoveryListener {
    enum State {
        STOPPED,
        FAST,
        SLOW
    }

    private static final boolean SHOW_DEBUG_WINDOW = Constants.WAND_SHOW_DEBUG_WINDOW;

    private final WandCopingSkillActivity activity;
    private final BleWandScanner bleWandScanner;
    private BleWand bleWand;
    private String lastNotification = "";
    private WandStateHandler.State currentState = WandStateHandler.State.STOPPED;


    private void updateDebugWindow() {
        if (SHOW_DEBUG_WINDOW) {
            String display;
            if (bleWandScanner.isWandDiscovered()) {
                display = bleWand.getDeviceName();
                display = display+"\n"+lastNotification;
            } else {
                display = bleWandScanner.isScanning() ? "Looking for Wand" : "Inactive";
            }
            final String displayOnUiThread = display;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO Check text view debug
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
                activity.findViewById(R.id.wandConnectionError).setVisibility( bleWandScanner.isWandConnected() ? View.GONE : View.VISIBLE);
            }
        });
    }


    private void updateWand(BleWand bleWand) {
        this.bleWand = bleWand;
        this.bleWand.notificationCallback = this;
    }


    private void changeState(WandStateHandler.State newState) {
        currentState = newState;
        if (currentState == WandStateHandler.State.STOPPED) {
            // Turn off light on tip of wand
            //TODO Send the off command
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.findViewById(R.id.activityBackground).setBackgroundResource(activity.getResourceForBackground());
                    TextView textView = activity.findViewById(R.id.textViewTitle);
                    textView.setText(activity.getTextTitleResource());
                    textView.setTextColor(activity.getResources().getColor(activity.getColorResourceForTitle()));
                }
            });
        } else if (currentState == WandStateHandler.State.SLOW) {
            // Turn lights rainbow colors matching the tempo of the music
            // TODO send commands
        } else if (currentState == WandStateHandler.State.FAST) {
            // Turn off lights on the tip of wand
            // TODO Send the off command
        }
    }


    public WandStateHandler(WandCopingSkillActivity activity) {
        this.activity = activity;
        this.bleWandScanner = new BleWandScanner(activity, this, this);

        // debug window
        //TODO
        TextView textView = activity.findViewById(R.id.textViewDebug);
        String display = "Initialized";
        final String displayOnUiThread = display;
        textView.setText(displayOnUiThread);
        if (!SHOW_DEBUG_WINDOW) {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onReceivedData(String arg1, String arg2, String arg3) {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "onReceivedData ignored while activity is paused.");
            return;
        }

        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3;
            // TODO add reformedData to queue
            lastNotification = reformedData;
            updateDebugWindow();
        }
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onConnected");
        updateConnectionErrorView();
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onDisconnected");
        updateConnectionErrorView();
        //TODO
        lookForWand();
    }


    @Override
    public void onDiscovered(BleWand bleWand) {
        updateWand(bleWand);
        updateDebugWindow();
    }


    public void lookForWand() {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "lookForWand ignored while activity is paused.");
            return;
        }
        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        if (bleWandScanner.isWandDiscovered()) {
            updateWand(globalHandler.bleWand);
        } else {
            bleWandScanner.requestScan();
        }

        updateConnectionErrorView();
        updateDebugWindow();
    }


    public void initializeState() {
        changeState(WandStateHandler.State.STOPPED);

    }


    public void pauseState() {
        bleWandScanner.stopScan();
        currentState = WandStateHandler.State.STOPPED;
    }

}
