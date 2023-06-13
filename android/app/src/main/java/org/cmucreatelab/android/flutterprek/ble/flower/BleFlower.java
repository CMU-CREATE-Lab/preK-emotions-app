package org.cmucreatelab.android.flutterprek.ble.flower;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleFlower {

    private UARTConnection uartConnection;
    public NotificationCallback notificationCallback = null;


    public BleFlower(Context appContext, BluetoothDevice device, UARTConnection.ConnectionListener connectionListener) {
        this.uartConnection = new UARTConnection(appContext, device, Constants.FLOWER_UART_SETTINGS, connectionListener);
        uartConnection.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                Log.d(Constants.LOG_TAG, "newData='" + new String(newData).trim() + "'");
                if (notificationCallback != null) {
                    String[] params = new String(newData).trim().split(",");
                    if (Constants.SUPPORT_LEGACY_FLOWER_PROTOCOL) {
                        if (params.length == 4) {
                            notificationCallback.onReceivedData(params[0], params[1], params[2], params[3]);
                        } else if (params.length == 3) {
                            // NOTE: this value increments on the new flower but is unused by the onReceivedData method, so a constant here is okay.
                            String str = "0";
                            notificationCallback.onReceivedData(str, params[0], params[1], params[2]);
                        } else {
                            Log.e(Constants.LOG_TAG, "parsed invalid number of params from notification='" + new String(newData).trim() + "'; unable to call NotificationCallback.");
                            return;
                        }
                    } else {
                        if (params.length < 4) {
                            Log.e(Constants.LOG_TAG, "parsed less than four params from notification='" + new String(newData).trim() + "'; unable to call NotificationCallback.");
                            return;
                        }
                        notificationCallback.onReceivedData(params[0], params[1], params[2], params[3]);
                    }
                }
            }
        });
    }


    public boolean isConnected() {
        return uartConnection.isConnected();
    }


    public String getDeviceName() {
        BluetoothDevice bluetoothDevice = uartConnection.getBLEDevice();
        if (bluetoothDevice == null) {
            Log.w(Constants.LOG_TAG, "getDeviceName with null bluetooth device");
            return null;
        }
        return bluetoothDevice.getName();
    }


    public void writeData(byte[] bytes) {
        if (bytes != null) {
            boolean wrote = this.uartConnection.writeBytes(bytes);
            if (!wrote) {
                Log.w(Constants.LOG_TAG, "Value: " + bytes[0] + " was not written");
            }
        }
    }


    public void disconnect() {
        this.uartConnection.disconnect();
    }


    public interface NotificationCallback {
        void onReceivedData(@NonNull String arg1, @NonNull String arg2, @NonNull String arg3, @NonNull String arg4);
    }

}
