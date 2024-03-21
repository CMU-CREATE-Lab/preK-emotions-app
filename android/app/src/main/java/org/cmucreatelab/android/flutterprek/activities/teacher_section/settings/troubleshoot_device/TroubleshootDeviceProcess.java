package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings.troubleshoot_device;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;

public class TroubleshootDeviceProcess implements BackgroundTimer.TimeExpireListener {

    private static final int millisecondsToWaitForBackgroundTimer = 2500;

    public interface EventsCallback {
        void onTroubleshootDeviceProcessSendCommand(String cmd);
        void onTroubleshootDeviceProcessValueReceived(ProcessState state, String value);
        void onTroubleshootDeviceProcessTimeout();
        void onTroubleshootDeviceProcessCompleted();
    }

    enum ProcessState {
        firmwareVersion,
        powerLedState,
        batteryPercentage,
        voltageBattery,
        voltageUsb,
        usbDetected
    }

    public static class FirmwareValue {
        public ProcessState state;
        public String bleCommand;

        public FirmwareValue(ProcessState state, String bleCommand) {
            this.state = state;
            this.bleCommand = bleCommand;
        }

    }

    private static final FirmwareValue[] stateMachine = {
            new FirmwareValue(ProcessState.firmwareVersion, "$f"),
            new FirmwareValue(ProcessState.powerLedState, "$s"),
            new FirmwareValue(ProcessState.batteryPercentage, "$p"),
            new FirmwareValue(ProcessState.voltageBattery, "$b"),
            new FirmwareValue(ProcessState.voltageUsb, "$u"),
            new FirmwareValue(ProcessState.usbDetected, "$c"),
    };

    private BackgroundTimer backgroundTimer;
    private EventsCallback callback;
    private int currentIndexOfStateMachine = 0;


    private void continueProcess() {
        if (currentIndexOfStateMachine >= stateMachine.length) {
            callback.onTroubleshootDeviceProcessCompleted();
            return;
        }
        String bleCommand = stateMachine[currentIndexOfStateMachine].bleCommand;
        backgroundTimer.startTimer();
        callback.onTroubleshootDeviceProcessSendCommand(bleCommand);
    }


    public TroubleshootDeviceProcess(EventsCallback callback) {
        this.backgroundTimer = new BackgroundTimer(millisecondsToWaitForBackgroundTimer, this);
        this.callback = callback;
    }


    public void initialize() {
        // fresh state for instance
        backgroundTimer.stopTimer();
        currentIndexOfStateMachine = 0;
        // start/restart
        continueProcess();
        Log.i(Constants.LOG_TAG, "TroubleshootDeviceProcess.initialize and start background timer");
    }


    // handles the received BLE Message based on current state of process
    public synchronized void updateWithBleResponse(String message) {
        Log.i(Constants.LOG_TAG, "updateWithBleResponse: " + message);
        backgroundTimer.stopTimer();
        ProcessState currentState = stateMachine[currentIndexOfStateMachine].state;
        // TODO remove '$'?
        String value = message;
        callback.onTroubleshootDeviceProcessValueReceived(currentState, value);

        currentIndexOfStateMachine++;
        continueProcess();
    }


    @Override
    public void timerExpired() {
        Log.i(Constants.LOG_TAG, "TroubleshootDeviceProcess.timerExpired");
        callback.onTroubleshootDeviceProcessTimeout();
    }

}
