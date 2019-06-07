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
    private final WandSpeedTracker wandSpeedTracker;
    private BleWand bleWand;
    private String lastNotification = "";
    private int[] curVals = null;
    private long prevTime = 0;

    private long curTime = 0;
    // Number of readings for a period
    private int period = 70; //120 // 60 //100
    private WandStateHandler.State currentState = WandStateHandler.State.STOPPED;

    private int dataCount = 0;
    private int periodCount = 0;
    private int window = 3;


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
            color = new byte[] {0x00};
        } else if (currentState == WandStateHandler.State.SLOW) {
            // Turn lights rainbow colors matching the tempo of the music
            // TODO send color change command?
            color = new byte[] {0x02};
            activity.setVolumeHigh();
        } else if (currentState == WandStateHandler.State.FAST) {
            // Turn off lights on the tip of wand
            // TODO Send color red?
            color = new byte[] {0x01};
            activity.setVolumeLow();
        }
        if(bleWand != null) {
            bleWand.writeData(color);
        }
    }


    public WandStateHandler(WandCopingSkillActivity activity) {
        this.activity = activity;
        this.bleWandScanner = new BleWandScanner(activity, this, this);
        this.wandSpeedTracker = new WandSpeedTracker(activity, window);

        // debug window
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

        dataCount++;
        periodCount++;
        //Log.e(Constants.LOG_TAG, "total count: "+dataCount+", period count: "+periodCount);

        curVals = new int[] {Integer.parseInt(arg1), Integer.parseInt(arg2), Integer.parseInt(arg3)};

        wandSpeedTracker.writeValsToArray(curVals, curTime);

        if (dataCount >= window) {
            //Log.e(Constants.LOG_TAG, "Calling countSigns()");
            wandSpeedTracker.countSigns();
            //Log.e(Constants.LOG_TAG, "Returned from countSigns()");
        }
        //Log.e(Constants.LOG_TAG, "Done.");

        prevTime = curTime;
        curTime = System.currentTimeMillis();

        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3;
            lastNotification = reformedData + "\n" + (curTime-prevTime) + "\n" + curTime + "\n" + dataCount;
            updateDebugWindow();
        }
    }

    public void update() {
        long tempTime = System.currentTimeMillis();

        Log.e(Constants.LOG_TAG, "temp time - prev time: " + (tempTime-prevTime));
        if(tempTime - prevTime > 1000) {
            changeState(State.STOPPED);
        } else {
            if (periodCount >= period) {
                //Log.e(Constants.LOG_TAG, "Calling getSpeed()");
                int state = -1;
                state = wandSpeedTracker.getSpeed();

                switch (state) {
                    case 2:
                        //Fast
                        changeState(State.FAST);
                        break;
                    case 1:
                        //Slow
                        changeState(State.SLOW);
                        break;
                    case 0:
                        //Not moving
                        changeState(State.STOPPED);
                        break;
                    case -1:
                        break;
                    default:
                }
                periodCount = 0;
                //Log.e(Constants.LOG_TAG, "Returned from getSpeed()");
            }
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
