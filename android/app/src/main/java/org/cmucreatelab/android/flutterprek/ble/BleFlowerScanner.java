package org.cmucreatelab.android.flutterprek.ble;

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
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class BleFlowerScanner implements UARTConnection.ConnectionListener {

    private static final int REQUEST_ENABLE_BT = 1;

    private final BluetoothAdapter bluetoothAdapter;
    private final AbstractActivity activity;
    private final @NonNull DiscoveryListener discoveryListener;
    private final @NonNull UARTConnection.ConnectionListener connectionListener;
    private boolean isScanning = false;
    private boolean isFlowerDiscovered;
    private boolean isFlowerConnected;

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!isFlowerDiscovered) {
                BluetoothDevice device = result.getDevice();
                if (device.getName() != null && GlobalHandler.getInstance(activity.getApplicationContext()).deviceConnectionHandler.checkIfValidBleDevice(BleFlower.class, device.getName())) {
                    Log.d(Constants.LOG_TAG, "onLeScan found Flower with name=" + device.getName());
                    isFlowerDiscovered = true;
                    GlobalHandler.getInstance(activity.getApplicationContext()).startConnection(BleFlower.class, device, BleFlowerScanner.this);
                    discoveryListener.onDiscovered(GlobalHandler.getInstance(activity.getApplicationContext()).bleFlower);
                    stopScan();
                } else {
                    Log.d(Constants.LOG_TAG, "onLeScan result: " + device.getName());
                }
            }
            super.onScanResult(callbackType, result);
        }
    };

    public interface DiscoveryListener {
        void onDiscovered(BleFlower bleFlower);
    }


    private void startScan() {
        isScanning = true;
        // TODO ScanFilter https://developer.android.com/reference/android/bluetooth/le/ScanFilter
        BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(Constants.LOG_TAG, "Tried to call stopScan but scanner is null");
            return;
        }
        scanner.startScan(scanCallback);
        // TODO timeout?
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
//                    Log.d(Constants.LOG_TAG,"Stopped LeScan");
//                }
//            }, 2000);
    }


    public BleFlowerScanner(FlowerCopingSkillActivity activity, @NonNull DiscoveryListener discoveryListener, @NonNull UARTConnection.ConnectionListener connectionListener) {
        this.activity = activity;
        this.discoveryListener = discoveryListener;
        this.connectionListener = connectionListener;

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        this.isFlowerDiscovered = globalHandler.isFlowerConnected();
        this.isFlowerConnected = globalHandler.isFlowerConnected();
    }


    @Override
    public void onConnected() {
        isFlowerConnected = true;
        connectionListener.onConnected();
    }


    @Override
    public void onDisconnected() {
        isFlowerConnected = false;
        isFlowerDiscovered = false;
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


    public boolean isFlowerDiscovered() {
        return isFlowerDiscovered;
    }


    public boolean isFlowerConnected() {
        return isFlowerConnected;
    }

}
