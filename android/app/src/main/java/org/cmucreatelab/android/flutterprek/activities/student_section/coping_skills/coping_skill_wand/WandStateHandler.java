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
    private int [] prev2 = null;
    private int[] prevVals = null;
    private int[] curVals = null;
    private long prev2Time = 0;
    private long prevTime = 0;
    private long curTime = 0;
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
        byte[] color = null;
        if (currentState == WandStateHandler.State.STOPPED) {
            // Turn off light on tip of wand
            //TODO Send the off command
            color = new byte[] {0x00};
        } else if (currentState == WandStateHandler.State.SLOW) {
            // Turn lights rainbow colors matching the tempo of the music
            // TODO send commands
            color = new byte[] {0x02};
        } else if (currentState == WandStateHandler.State.FAST) {
            // Turn off lights on the tip of wand
            // TODO Send the off command
            color = new byte[] {0x01};
        }
        if(bleWand != null) {
            bleWand.writeData(color);
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

        // TEST
        // Get times
        prev2Time = prevTime;
        prevTime = curTime;
        curTime = System.currentTimeMillis();

        prev2 = prevVals;
        prevVals = curVals;
        curVals = new int[] {Integer.parseInt(arg1), Integer.parseInt(arg2), Integer.parseInt(arg3)};
        double vel = 0.0;
        double vel1 = 0.0;
        double accel = 0.0;
        double dt = 0.0;
        if(prevVals != null) {
            dt = curTime - prevTime;
            double dif1 = ((double)(curVals[0] - prevVals[0]))/dt;
            double dif2 = ((double)(curVals[1] - prevVals[1]))/dt;
            double dif3 = ((double)(curVals[2] - prevVals[2]))/dt;
            vel = Math.sqrt((dif1*dif1) + (dif2*dif2) + (dif3*dif3));
            if(prev2 != null) {
                dt = prevTime - prev2Time;
                double dif11 = ((double)(prevVals[0] - prev2[0]))/dt;
                double dif12 = ((double)(prevVals[1] - prev2[1]))/dt;
                double dif13 = ((double)(prevVals[2] - prev2[2]))/dt;
                vel1 = Math.sqrt((dif11*dif11) + (dif12*dif12) + (dif13*dif13));
                dt = curTime - prev2Time;
                accel = (vel - vel1)/dt;
            }
        }


        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3;
            // TODO add reformedData to queue
            lastNotification = reformedData + "\n" + vel + "\n" + accel;
            updateDebugWindow();
        }

        // TODO update the position/motion of the wand
        if (vel == 0.0) {
            changeState(State.STOPPED);
        } else if (vel < 5.0) {
            changeState(State.SLOW);
        } else {
            changeState(State.FAST);
        }
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onConnected");
        updateConnectionErrorView();
        updateDebugWindow();
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onDisconnected");
        updateConnectionErrorView();
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
        activity.setScreen();
        changeState(WandStateHandler.State.STOPPED);
    }


    public void pauseState() {
        bleWandScanner.stopScan();
        //currentState = WandStateHandler.State.STOPPED;
        changeState(WandStateHandler.State.STOPPED);
    }

}
