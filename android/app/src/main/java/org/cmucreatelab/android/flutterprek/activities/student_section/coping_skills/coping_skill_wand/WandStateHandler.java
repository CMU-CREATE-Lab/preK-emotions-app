package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.os.AsyncTask;
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
    private final WandCopingSkillAudioHandler wandCopingSkillAudioHandler;
    private final WandWriteTimer wandWriteTimer;
    private BleWand bleWand;
    private String lastNotification = "";
    private int[] curVals = null;
    private long prevTime = 0;

    private long curTime = 0;
    // Number of readings for a period
    private int period = 14;
    private WandStateHandler.State currentState = WandStateHandler.State.STOPPED;

    private int dataCount = 0;
    private int periodCount = 0;
    private int window = 5;

    private String[] data;
    private Double dataMagnitude = null;
    private long dataSequence = 0;
    private Long lastProcessedSequence = null;
    private Long pendingTriggerSequence = null;
    private boolean triggerArmed = true;
    private boolean log = false;

    private String slow_color = "0,255,0";


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
        updateConnectionErrorView( bleWand == null || !bleWand.isConnected());
    }


    private void updateConnectionErrorView(final boolean isVisible) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.wandConnectionError).setVisibility( isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }


    private void updateWand(BleWand bleWand) {
        this.bleWand = bleWand;
        this.bleWand.notificationCallback = this;
    }


    private void changeState(WandStateHandler.State newState) {
        changeState(newState, false);
    }


    private void changeState(WandStateHandler.State newState, boolean includePendingTrigger) {
        currentState = newState;
        String rgb = "";
        byte[] color = null;
        if (currentState == WandStateHandler.State.STOPPED) {
            // Turn on the white light
            rgb = "255,255,255";
            wandCopingSkillAudioHandler.pauseAudio();
        } else if (currentState == WandStateHandler.State.SLOW) {
            // Turn lights rainbow colors matching the tempo of the music
            rgb = slow_color;
            // TODO replace with call to audio handler
            wandCopingSkillAudioHandler.setAudio(false);
            //activity.setVolumeHigh();
        } else if (currentState == WandStateHandler.State.FAST) {
            // Turn light red
            rgb = "255,0,0";
            // TODO replace with call to audio handler
            wandCopingSkillAudioHandler.setAudio(true);
            //activity.setVolumeLow();
        }
        if (bleWand != null) {
            if (lastProcessedSequence != null) {
                rgb = rgb + "," + lastProcessedSequence;
                if (includePendingTrigger && pendingTriggerSequence != null) {
                    rgb = rgb + "," + pendingTriggerSequence;
                    pendingTriggerSequence = null;
                }
            }
            color = rgb.getBytes();
            bleWand.writeData(color);
            bleWand.writeData(new byte[] {0x0D});
        }
    }


    public WandStateHandler(WandCopingSkillActivity activity, WandCopingSkillAudioHandler wandCopingSkillAudioHandler) {
        this.activity = activity;
        this.bleWandScanner = new BleWandScanner(activity, this, this);
        this.wandSpeedTracker = new WandSpeedTracker(activity, window);
        this.wandWriteTimer = new WandWriteTimer(this);
        this.wandCopingSkillAudioHandler = wandCopingSkillAudioHandler;

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
    public void onReceivedData(long sequence, String button, String x, String y, String z) {
        handleReceivedData(sequence, button, new String[] {x, y, z}, null);
    }


    @Override
    public void onReceivedMagnitude(long sequence, String button, double magnitude) {
        handleReceivedData(sequence, button, new String[] {Long.toString(Math.round(magnitude)), "0", "0"}, magnitude);
    }


    private void handleReceivedData(long sequence, String button, String[] receivedData, Double receivedMagnitude) {
        if (activity.isPaused()) {
            Log.v(Constants.LOG_TAG, "onReceivedData ignored while activity is paused.");
            return;
        }
        //TODO BUTTON
        if (Integer.parseInt(button) == 1) {
            int r = (int) (Math.random()*255);
            int g = (int) (Math.random()*255);
            int b = (int) (Math.random()*255);
            // Check if red
            if (r >= 200 && g <= 50 && b <=50) {
                g += 60;
                b += 60;
            }
            // Check if white
            if (r >= 200 && b >= 200 && b >= 200) {
                r -= 60;
                b -= 60;
            }

            slow_color = Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b);
            Log.e(Constants.LOG_TAG, "Slow color: "+slow_color);
        }

        prevTime = curTime;
        curTime = System.currentTimeMillis();

        data = receivedData;
        dataMagnitude = receivedMagnitude;
        dataSequence = sequence;
        log = true;

        if (SHOW_DEBUG_WINDOW) {
            String reformedData;
            if (receivedMagnitude == null) {
                reformedData = button+", "+receivedData[0]+","+receivedData[1]+","+receivedData[2];
            } else {
                reformedData = button+", magnitude="+receivedMagnitude;
            }
            lastNotification = reformedData + "\n" + (curTime-prevTime) + "\n" + curTime + "\n" + dataCount;
            //updateDebugWindow();
        }
    }


    public void update() {
        long tempTime = System.currentTimeMillis();

        //Log.e(Constants.LOG_TAG, "temp time - prev time: " + (tempTime-prevTime));
        if(tempTime - prevTime > 200) {
            changeState(State.STOPPED);
        } else {
            if (periodCount >= period) {
                Log.e(Constants.LOG_TAG, "Period Count = " + Integer.toString(periodCount));
                int state = -1;
                state = wandSpeedTracker.getSpeed();

                switch (state) {
                    case 2:
                        //Fast
                        changeState(State.FAST, true);
                        break;
                    case 1:
                        //Slow
                        changeState(State.SLOW, true);
                        break;
                    case 0:
                        //Not moving
                        changeState(State.STOPPED, true);
                        break;
                    case -1:
                        break;
                    default:
                }
                periodCount = 0;
            }
        }

    }


    public void logData() {
        // If the log flag is set, take the string and parse the info
        if (log) {
            curVals = new int[] {Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])};
            wandSpeedTracker.writeValsToArray(curVals, curTime);

            dataCount++;
            periodCount++;

            if (dataCount >= window) {
                double processedMagnitude;
                if (dataMagnitude == null) {
                    double x = (double) curVals[0];
                    double y = (double) curVals[1];
                    double z = (double) curVals[2];
                    processedMagnitude = Math.sqrt(x*x + y*y + z*z);
                } else {
                    processedMagnitude = Math.abs(dataMagnitude);
                }
                wandSpeedTracker.updateMaxMag(processedMagnitude);

                if (processedMagnitude < 15.0 && pendingTriggerSequence == null) {
                    triggerArmed = true;
                } else if (triggerArmed && pendingTriggerSequence == null) {
                    pendingTriggerSequence = dataSequence;
                    triggerArmed = false;
                }
            }
            lastProcessedSequence = dataSequence;
            log = false;
        }
    }


    @Override
    public void onConnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onConnected");
        wandWriteTimer.startTimer();
        updateConnectionErrorView(false);
        updateDebugWindow();
    }


    @Override
    public void onDisconnected() {
        Log.d(Constants.LOG_TAG, "WandStateHandler: onDisconnected");
        pendingTriggerSequence = null;
        triggerArmed = true;
        updateConnectionErrorView(true);
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
        // NOTE: avoid using UI thread for ble scans/connection
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
                if (bleWandScanner.isWandDiscovered()) {
                    updateWand(globalHandler.bleWand);
                } else {
                    bleWandScanner.requestScan();
                }

                updateConnectionErrorView();
                updateDebugWindow();
            }
        });
    }


    public void initializeState() {
        activity.displayTextTitle();
        changeState(WandStateHandler.State.STOPPED);
    }


    public void pauseState() {
        bleWandScanner.stopScan();
        //currentState = WandStateHandler.State.STOPPED;
        changeState(WandStateHandler.State.STOPPED);
    }


    public BleWand getBleWand() {
        return bleWand;
    }

}
