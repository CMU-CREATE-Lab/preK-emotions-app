package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings.troubleshoot_device;

import static android.bluetooth.BluetoothProfile.GATT;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.ble.scan.ConnectedAndScannedDevicesViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsTroubleshootDeviceIndex extends TeacherSectionActivityWithHeaderAndDrawer implements ConnectedAndScannedDevicesViewAdapter.OnListInteractionListener {

    private RecyclerView recyclerViewAvailableDevices;
    private BluetoothAdapter bluetoothAdapter;
    private ConnectedAndScannedDevicesViewAdapter connectedAndScannedDevicesViewAdapter;

    private static final int REQUEST_ENABLE_BT = 1;

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(Constants.LOG_TAG,"onLeScan result: " + device.getName());
            connectedAndScannedDevicesViewAdapter.addToList(device, false);
            super.onScanResult(callbackType, result);
        }
    };


    // TODO copied method from SettingsBleActivity (see notes there)
    private synchronized void prepareLeScanning(boolean startScan) {
        Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning()");
        connectedAndScannedDevicesViewAdapter.clearList();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (startScan) {
                Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning START");
                bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
            } else {
                Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning STOP");
                bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            }
        }
    }


    private void updateViews() {
        // scan for advertising devices
        prepareLeScanning(true);

        // get connected devices
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        List<BluetoothDevice> list = bluetoothManager.getConnectedDevices(GATT);
        if (list.size() > 0) {
            for (BluetoothDevice device: list) {
                String displayName = String.format("%s (connected)", device.getName());
                Log.d(Constants.LOG_TAG,"ConnectedDevices: " + displayName);
                connectedAndScannedDevicesViewAdapter.addToList(device, true);
            }
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_troubleshoot_device_index_with_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.recyclerViewAvailableDevices = findViewById(R.id.recyclerViewAvailableDevices);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        this.connectedAndScannedDevicesViewAdapter = new ConnectedAndScannedDevicesViewAdapter(new ArrayList<BluetoothDevice>(), this);
        recyclerViewAvailableDevices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewAvailableDevices.setAdapter(connectedAndScannedDevicesViewAdapter);

        findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        prepareLeScanning(false);
    }

    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return null;
    }

    @Override
    public void onItemSelected(BluetoothDevice item, boolean isConnected) {
        final String deviceName = item.getName();
        Log.d(Constants.LOG_TAG, String.format("onItemSelected: %s (connected=%b)", deviceName, isConnected));

        Intent settingsTroubleshootDeviceShow = new Intent(getApplicationContext(), SettingsTroubleshootDeviceShow.class);
        settingsTroubleshootDeviceShow.putExtra(SettingsTroubleshootDeviceShow.EXTRA_DEVICE, item);
        settingsTroubleshootDeviceShow.putExtra(SettingsTroubleshootDeviceShow.EXTRA_IS_CONNECTED, isConnected);
        startActivity(settingsTroubleshootDeviceShow);
    }
}
