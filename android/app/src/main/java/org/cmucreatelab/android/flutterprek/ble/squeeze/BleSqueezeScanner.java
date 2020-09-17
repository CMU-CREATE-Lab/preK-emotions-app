package org.cmucreatelab.android.flutterprek.ble.squeeze;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze.SqueezeCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleSqueezeScanner implements UARTConnection.ConnectionListener {

    public interface DiscoveryListener {
        void onDiscovered(BleSqueeze bleSqueeze);
    }

    private static final int REQUEST_ENABLE_BT = 1;

    private final BluetoothAdapter bluetoothAdapter;
    private final AbstractActivity activity;
    private final @NonNull
    BleSqueezeScanner.DiscoveryListener discoveryListener;
    private final @NonNull UARTConnection.ConnectionListener connectionListener;
    private boolean isScanning = false;
    private boolean isSqueezeDiscovered;
    private boolean isSqueezeConnected;



    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!isSqueezeDiscovered) {
                BluetoothDevice device = result.getDevice();
                if (device.getName() != null && GlobalHandler.getInstance(activity.getApplicationContext()).deviceConnectionHandler.checkIfValidBleDevice(BleSqueeze.class, device.getName())) {
                    Log.d(Constants.LOG_TAG, "onLeScan found Squeeze with name=" + device.getName());
                    isSqueezeDiscovered = true;
                    GlobalHandler.getInstance(activity.getApplicationContext()).startConnection(BleSqueeze.class, device, BleSqueezeScanner.this);
                    discoveryListener.onDiscovered(GlobalHandler.getInstance(activity.getApplicationContext()).bleSqueeze);
                    stopScan();
                } else {
                    Log.d(Constants.LOG_TAG, "onLeScan result: " + device.getName());
                }
            }
            super.onScanResult(callbackType, result);
        }
    };


    private void startScan() {
        // TODO ScanFilter https://developer.android.com/reference/android/bluetooth/le/ScanFilter
        BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(Constants.LOG_TAG, "Tried to call stopScan but scanner is null");
            return;
        }
        scanner.startScan(scanCallback);
        isScanning = true;
    }


    public BleSqueezeScanner(AbstractActivity activity, @NonNull BleSqueezeScanner.DiscoveryListener discoveryListener, @NonNull UARTConnection.ConnectionListener connectionListener) {
        this.activity = activity;
        this.discoveryListener = discoveryListener;
        this.connectionListener = connectionListener;

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        this.isSqueezeDiscovered = globalHandler.isSqueezeConnected();
        this.isSqueezeConnected = globalHandler.isSqueezeConnected();
    }


    @Override
    public void onConnected() {
        isSqueezeConnected = true;
        connectionListener.onConnected();
    }


    @Override
    public void onDisconnected() {
        isSqueezeConnected = false;
        isSqueezeDiscovered = false;
        connectionListener.onDisconnected();
    }


    public void stopScan() {
        isScanning = false;
        BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(Constants.LOG_TAG, "Tried to call stopScan but scanner is null");
            return;
        }
        scanner.stopScan(scanCallback);
    }


    public void requestScan() {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.d(Constants.LOG_TAG,"Starting LeScan...");
            startScan();
        }
    }


    public boolean isScanning() {
        return isScanning;
    }


    public boolean isSqueezeDiscovered() {
        return isSqueezeDiscovered;
    }


    public boolean isSqueezeConnected() {
        return isSqueezeConnected;
    }

}