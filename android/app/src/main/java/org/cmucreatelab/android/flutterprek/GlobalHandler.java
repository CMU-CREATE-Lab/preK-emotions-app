package org.cmucreatelab.android.flutterprek;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.ble.DeviceConnectionHandler;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueeze;

/**
 *
 * A class that provides access across activities.
 *
 */
public class GlobalHandler {

    public final Context appContext;
    public BleFlower bleFlower;
    public BleSqueeze bleSqueeze;
    public final StudentSectionNavigationHandler studentSectionNavigationHandler;
    public final DeviceConnectionHandler deviceConnectionHandler;
    private SessionTracker sessionTracker;


    private GlobalHandler(Context context) {
        this.appContext = context;
        this.studentSectionNavigationHandler = new StudentSectionNavigationHandler();
        this.deviceConnectionHandler = new DeviceConnectionHandler(Constants.HARDCODED_VALUES);
    }


    // Singleton Implementation


    private static GlobalHandler classInstance;


    public static synchronized GlobalHandler getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(context);
        }
        return classInstance;
    }


    // public methods

    public boolean isSqueezeConnected() {
        return (bleSqueeze != null && bleSqueeze.isConnected());
    }

    /**
     * Start a new session for a student.
     *
     * @param student Every session has a Student associated with it.
     */
    public void startNewSession(StudentWithCustomizations student) {
        if (currentSessionIsActive()) {
            Log.w(Constants.LOG_TAG, "call to startNewSession while another session is active.");
        }
        String sessionMode = studentSectionNavigationHandler.classroomSessionMode;
        sessionTracker = (sessionMode != null && sessionMode.equals("checkIn")) ? new SessionTracker(student, SessionTracker.SessionMode.CHECK_IN) : new SessionTracker(student);
    }


    /**
     * Check if the current session is active.
     *
     * @return false if the session is finished or if it is not set (null).
     */
    public boolean currentSessionIsActive() {
        return (sessionTracker != null && !sessionTracker.isFinished());
    }


    /**
     * @return the current session.
     */
    public SessionTracker getSessionTracker() {
        return sessionTracker;
    }


    /**
     * Ends the current session.
     *
     * @param currentActivity the current activity (this is called to start a new activity).
     * @return true if the session was successfully ended (and was not already ended), false otherwise.
     */
    public boolean endCurrentSession(AbstractActivity currentActivity) {
        boolean result = false;
        if (currentSessionIsActive()) {
            result = sessionTracker.endSession();
        } else {
            Log.w(Constants.LOG_TAG, "called endCurrentSession but no current session active");
        }

        // go back to student screen (regardless of value of result)
        Intent intent = SessionTracker.getIntentForEndSession(currentActivity);
        currentActivity.startActivity(intent);

        return result;
    }


    // BLE methods


    public boolean isFlowerConnected() {
        return (bleFlower != null && bleFlower.isConnected());
    }


    /**
     * start a new BLE connection with a BLE device
     * @param classToValidate The type of device we are connecting to (example: {@link BleFlower}).
     * @param bluetoothDevice should be one of MindfulNest BLE devices.
     * @param connectionListener Listen for connection state changes.
     */
    public synchronized void startConnection(Class classToValidate, BluetoothDevice bluetoothDevice, UARTConnection.ConnectionListener connectionListener) {
        if(classToValidate == BleFlower.class) {
            if (bleFlower != null) {
                Log.w(Constants.LOG_TAG, "current bleFlower in GlobalHandler is not null; attempting to close.");
                try {
                    bleFlower.disconnect();
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "Exception caught in GlobalHandler.startConnection (likely null reference in UARTConnection); Exception message was: ``" + e.getMessage() + "''");
                }
            }
            this.bleFlower = new BleFlower(appContext, bluetoothDevice, connectionListener);
        }

        if(classToValidate == BleSqueeze.class){
            if (bleSqueeze != null) {
                Log.w(Constants.LOG_TAG, "current bleSqueeze in GlobalHandler is not null; attempting to close.");
                try {
                    bleSqueeze.disconnect();
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "Exception caught in GlobalHandler.startConnection (likely null reference in UARTConnection); Exception message was: ``" + e.getMessage() + "''");
                }
            }
            Log.w(Constants.LOG_TAG, "Trying to create new bleSqueeze");
            this.bleSqueeze = new BleSqueeze(appContext, bluetoothDevice, connectionListener);
            Log.w(Constants.LOG_TAG, "Created new bleSqueeze");
        }
    }

}
