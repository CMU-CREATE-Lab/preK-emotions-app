package org.cmucreatelab.android.flutterprek;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

/**
 *
 * A class that provides access across activities.
 *
 */
public class GlobalHandler {

    public final Context appContext;
    public BleFlower bleFlower;
    public final StudentSectionNavigationHandler studentSectionNavigationHandler;
    public final DeviceConnectionHandler deviceConnectionHandler;


    private GlobalHandler(Context context) {
        this.appContext = context;
        this.studentSectionNavigationHandler = new StudentSectionNavigationHandler();
        if (Constants.HARDCODED_VALUES == null) {
            this.deviceConnectionHandler = new DeviceConnectionHandler();
        } else {
            this.deviceConnectionHandler = new DeviceConnectionHandler(Constants.HARDCODED_VALUES);
        }
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


    public boolean isFlowerConnected() {
        return (bleFlower != null && bleFlower.isConnected());
    }


    /**
     * start a new BLE connection with a BLE device
     * @param bluetoothDevice should be one of MindfulNest BLE devices.
     */
    public synchronized void startConnection(BluetoothDevice bluetoothDevice) {
        // TODO check for device type (assumes flower for now)
        if (bleFlower != null) {
            Log.w(Constants.LOG_TAG, "current bleFlower in GlobalHandler is not null; attempting to close.");
            try {
                bleFlower.disconnect();
            } catch (Exception e) {
                Log.e(Constants.LOG_TAG, "Exception caught in GlobalHandler.startConnection (likely null reference in UARTConnection); Exception message was: ``" + e.getMessage() + "''");
            }
        }
        this.bleFlower = new BleFlower(appContext, bluetoothDevice);
    }

}
