package org.cmucreatelab.android.flutterprek.ble.wand;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleWand {

    private static final String MAGNITUDE_PROTOCOL_VERSION = "-1";

    private UARTConnection uartConnection;
    public BleWand.NotificationCallback notificationCallback = null;


    public BleWand(Context appContext, BluetoothDevice device, UARTConnection.ConnectionListener connectionListener) {

        this.uartConnection = new UARTConnection(appContext, device, Constants.WAND_UART_SETTINGS, connectionListener);
        uartConnection.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                String notification = new String(newData).trim();
                Log.d(Constants.LOG_TAG, "newData='" + notification + "'");
                if (notificationCallback != null) {
                    String[] params = notification.split(",");
                    for (int i = 0; i < params.length; i++) {
                        params[i] = params[i].trim();
                    }

                    if (params.length >= 5) {
                        // Legacy protocol: counter,button,x,y,z. The existing
                        // speed tracker calculates magnitude from x, y, and z.
                        notificationCallback.onReceivedData(params[1], params[2], params[3], params[4]);
                    } else if (params.length == 4 && MAGNITUDE_PROTOCOL_VERSION.equals(params[0])) {
                        // Magnitude protocol: version,counter,button,magnitude.
                        try {
                            notificationCallback.onReceivedMagnitude(params[2], Double.parseDouble(params[3]));
                        } catch (NumberFormatException e) {
                            Log.e(Constants.LOG_TAG, "invalid wand magnitude in notification='" + notification + "'", e);
                        }
                    } else {
                        Log.d(Constants.LOG_TAG, "unsupported wand notification='" + notification + "'; unable to call NotificationCallback.");
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


    public void writeData(byte[] bytes) {
        if(bytes != null) {
            boolean wrote = this.uartConnection.writeBytes(bytes);
            if (!wrote) {
                Log.w(Constants.LOG_TAG, "Value: " + bytes[0] + " was not written");
            }
        }
    }


    public interface NotificationCallback {
        void onReceivedData(@NonNull String button, @NonNull String x, @NonNull String y, @NonNull String z);
        void onReceivedMagnitude(@NonNull String button, double magnitude);
    }

}

