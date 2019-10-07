package org.cmucreatelab.android.flutterprek.ble.wand;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

import java.lang.reflect.Array;
import java.util.Arrays;

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
                    if (params.length < 5) {
                        Log.d(Constants.LOG_TAG, "parsed less than five params from notification='"+new String(newData).trim()+"'; unable to call NotificationCallback.");
                        return;
                    } else {
                        // Trim the spaces out of the data
                        params[1] = params[1].replaceAll("\\s", "");
                        params[2] = params[2].replaceAll("\\s", "");
                        params[3] = params[3].replaceAll("\\s", "");
                        params[4] = params[4].replaceAll("\\s", "");
                        // TODO Remove after debugging
                        //Log.e(Constants.LOG_TAG, "Counter: " + params[0]);
                        //Log.e(Constants.LOG_TAG, "Button: " + params[1]);
                        //Log.e(Constants.LOG_TAG, "X: " + params[2]);
                        //Log.e(Constants.LOG_TAG, "Y: " + params[3]);
                        //Log.e(Constants.LOG_TAG, "Z: " + params[4]);
                        // Send the data, without the counter
                        notificationCallback.onReceivedData(params[1], params[2], params[3], params[4]);
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
        } else {
            return bluetoothDevice.getName();
        }
    }


    public void disconnect() {
        this.uartConnection.disconnect();
    }

    public void writeData(byte[] bytes){
        if(bytes != null) {
            boolean wrote = this.uartConnection.writeBytes(bytes);
            if (!wrote) {
                Log.w(Constants.LOG_TAG, "Value: " + bytes[0] + " was not written");
            }
        }
    }


    public interface NotificationCallback {
        void onReceivedData(@NonNull String button, @NonNull String x, @NonNull String y, @NonNull String z);
    }

}
