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
    String[] full_params = new String[4];
    boolean partialLogged = false;
    byte[] full_bytes = new byte[28];
    boolean startLogged = false;


    public BleWand(Context appContext, BluetoothDevice device, UARTConnection.ConnectionListener connectionListener) {

        this.uartConnection = new UARTConnection(appContext, device, Constants.WAND_UART_SETTINGS, connectionListener);
        uartConnection.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                boolean end = false;
                boolean send = false;
                Log.e(Constants.LOG_TAG, "Second to last byte='" + String.format("%02X ", newData[newData.length-2]) + "'");
                Log.e(Constants.LOG_TAG, "Last byte='" + String.format("%02X ", newData[newData.length-1]) + "'");
                Log.d(Constants.LOG_TAG, "newData='" + new String(newData).trim() + "'");
                if (newData[newData.length-2] == 0x0D & newData[newData.length-1] == 0x0A) {
                    end = true;
                    //send = collectData()
                    Log.e(Constants.LOG_TAG, "End is now true");
                } else {
                    end = false;
                }
                if (notificationCallback != null) {
                    String[] params = new String(newData).trim().split(",");
                    Double[] vals = new Double[params.length];
                    for (int i = 0; i < vals.length; i++) {
                        vals[i] = Double.parseDouble(params[i]);
                    }
                    Log.e(Constants.LOG_TAG, "TEST" + String.valueOf(Double.parseDouble(".8")));
                    //params[0] = params[0].substring(1);
                    if (params.length < 5) {
                        Log.e(Constants.LOG_TAG, "parsed less than five params from notification='"+new String(newData).trim()+"'; unable to call NotificationCallback.");
                        send = collectData(params, end);
                        //return;
                    } else {
                        //params[0] = params[0].replaceAll("\\s", "");
                        //params[1] = params[1].replaceAll("\\s", "");
                        //params[2] = params[2].replaceAll("\\s", "");
                        Log.e(Constants.LOG_TAG, "Counter: " + params[0]);
                        Log.e(Constants.LOG_TAG, "Button: " + params[1]);
                        Log.e(Constants.LOG_TAG, "X: " + params[2]);
                        Log.e(Constants.LOG_TAG, "Y: " + params[3]);
                        Log.e(Constants.LOG_TAG, "Z: " + params[4]);
                        Log.e(Constants.LOG_TAG, "End is " + end);
                        send = collectData(Arrays.copyOfRange(params, 1, 5), end);
                        //notificationCallback.onReceivedData("2", "3", "4");
                    }
                    if (send) {
                        notificationCallback.onReceivedData("2", "3", "4");
                    }
                }
            }
        });
    }

    public boolean collectBytes (byte[] part, boolean end){
        if (end) {
            if (startLogged) {
                for (int i = 20; i < full_bytes.length; i++) {
                    full_bytes[i] = part[i-20];
                }
                startLogged = false;
            }
        } else {
            if (!startLogged) {
                for (int i = 0; i < part.length; i++) {
                    full_bytes[i] = part[i];
                }
                startLogged = true;
            }
        }
        // TODO put this in the right spots
        return false;
    }

    public boolean collectData(String[] params, boolean end) {
        Log.e(Constants.LOG_TAG, "Made it to collect data");
        if (end) {
            Log.e(Constants.LOG_TAG, "Partial Logged is "+ partialLogged);
            if (partialLogged) {
                full_params[4] = full_params[4] + params[0];
                //full_params[4].concat(params[0]);
                Log.e(Constants.LOG_TAG, "HERE");
                partialLogged = false;
                Log.e(Constants.LOG_TAG, "full params: " + Arrays.toString(full_params));
                return true;
            }
            return false;
        } else {
            full_params[0] = params[0]; // Button
            full_params[1] = params[1]; // X
            full_params[2] = params[2]; // Y
            full_params[3] = params[3]; // Partial(?) Z
            partialLogged = true;
            Log.e(Constants.LOG_TAG, "Logged partial data");
            return false;
        }
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
        void onReceivedData(@NonNull String arg1, @NonNull String arg2, @NonNull String arg3);
    }

}
