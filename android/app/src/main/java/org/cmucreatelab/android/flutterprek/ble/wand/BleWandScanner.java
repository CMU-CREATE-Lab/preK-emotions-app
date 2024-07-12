package org.cmucreatelab.android.flutterprek.ble.wand;

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
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand.WandCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleWandScanner implements UARTConnection.ConnectionListener {

    public interface DiscoveryListener {
        void onDiscovered(BleWand bleWand);
    }

    private static final int REQUEST_ENABLE_BT = 1;

    private final BluetoothAdapter bluetoothAdapter;
    private final AbstractActivity activity;
    private final @NonNull
    BleWandScanner.DiscoveryListener discoveryListener;
    private final @NonNull UARTConnection.ConnectionListener connectionListener;
    private boolean isScanning = false;
    private boolean isWandDiscovered;
    private boolean isWandConnected;


    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!isWandDiscovered) {
                BluetoothDevice device = result.getDevice();
                if (device.getName() != null && GlobalHandler.getInstance(activity.getApplicationContext()).deviceConnectionHandler.checkIfValidBleDevice(BleWand.class, device.getName(), activity.getApplicationContext())) {
                    Log.d(Constants.LOG_TAG, "onLeScan found Wand with name=" + device.getName());
                    isWandDiscovered = true;
                    GlobalHandler.getInstance(activity.getApplicationContext()).startConnection(BleWand.class, device, BleWandScanner.this);
                    discoveryListener.onDiscovered(GlobalHandler.getInstance(activity.getApplicationContext()).bleWand);
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


    public BleWandScanner(AbstractActivity activity, @NonNull BleWandScanner.DiscoveryListener discoveryListener, @NonNull UARTConnection.ConnectionListener connectionListener) {
        this.activity = activity;
        this.discoveryListener = discoveryListener;
        this.connectionListener = connectionListener;

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        this.isWandDiscovered = globalHandler.isWandConnected();
        this.isWandConnected = globalHandler.isWandConnected();
    }


    @Override
    public void onConnected() {
        isWandConnected = true;
        connectionListener.onConnected();
    }


    @Override
    public void onDisconnected() {
        isWandConnected = false;
        isWandDiscovered = false;
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


    public boolean isWandDiscovered() {
        return isWandDiscovered;
    }


    public boolean isWandConnected() {
        return isWandConnected;
    }

}
