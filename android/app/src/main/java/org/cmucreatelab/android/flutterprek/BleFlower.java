package org.cmucreatelab.android.flutterprek;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.bluetooth_birdbrain.UARTConnection;

public class BleFlower {

    private UARTConnection uartConnection;


    public BleFlower(Context appContext, BluetoothDevice device) {
        this.uartConnection = new UARTConnection(appContext, device, Constants.FLOWER_UART_SETTINGS);
        uartConnection.addRxDataListener(new UARTConnection.RXDataListener() {
            @Override
            public void onRXData(byte[] newData) {
                Log.d(Constants.LOG_TAG, "newData='" + new String(newData).trim() + "'");
            }
        });
    }


    public boolean isConnected() {
        return uartConnection.isConnected();
    }


    public String getDeviceName() {
        return uartConnection.getBLEDevice().getName();
    }


    public void disconnect() {
        this.uartConnection.disconnect();
    }

}
