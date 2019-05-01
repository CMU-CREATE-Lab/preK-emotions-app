package org.cmucreatelab.android.flutterprek.ble.wand;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleWand {

    private UARTConnection uartConnection;
    public BleWand.NotificationCallback notificationCallback = null;


    public BleWand(Context appContext, BluetoothDevice device, UARTConnection.ConnectionListener connectionListener) {
        this.uartConnection = new UARTConnection(appContext, device, Constants.WAND_UART_SETTINGS, connectionListener);
        uartConnection.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                Log.d(Constants.LOG_TAG, "newData='" + new String(newData).trim() + "'");
                if (notificationCallback != null) {
                    String[] params = new String(newData).trim().split(",");
                    if (params.length < 3) {
                        Log.e(Constants.LOG_TAG, "parsed less than three params from notification='"+new String(newData).trim()+"'; unable to call NotificationCallback.");
                        return;
                    }
                    notificationCallback.onReceivedData(params[0], params[1], params[2]);
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


    public void disconnect() {
        this.uartConnection.disconnect();
    }


    public interface NotificationCallback {
        void onReceivedData(@NonNull String arg1, @NonNull String arg2, @NonNull String arg3);
    }

}
