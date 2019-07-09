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

    private UARTConnection uartConnection2;
    public BleWand.NotificationCallback notificationCallback2 = null;


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
                    notificationCallback.onReceivedData(params[0], params[1], params[2], "accel");
                }
            }
        });

        /*
        this.uartConnection2 = new UARTConnection(appContext, device, Constants.WAND2_UART_SETTINGS, connectionListener);
        uartConnection2.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                Log.d(Constants.LOG_TAG, "newData='" + new String(newData).trim() + "'");
                if (notificationCallback2 != null) {
                    String[] params = new String(newData).trim().split(",");
                    if (params.length < 3) {
                        Log.e(Constants.LOG_TAG, "parsed less than three params from notification='"+new String(newData).trim()+"'; unable to call NotificationCallback.");
                        return;
                    }
                    notificationCallback2.onReceivedData(params[0], params[1], params[2], "gyro");
                } else {
                    Log.e(Constants.LOG_TAG, "Null notifications callback");
                }
            }
        });
        */
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
        void onReceivedData(@NonNull String arg1, @NonNull String arg2, @NonNull String arg3, String type);
    }

}
