package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;

import java.io.Serializable;

public class SettingsBleActivity extends TeacherSectionActivityWithHeaderAndDrawer {

    public static final String EXTRA_DEVICE = "device";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456; //Arbitrary, but needs to be unique
    private static final int REQUEST_ENABLE_BT = 1;

    public enum PairingMode {
        AUTOMATIC,
        MANUAL
    }

    public enum DeviceType {
        FLOWER,
        WAND,
        SQUEEZE
    }

    public static class Device implements Serializable {

        public final String titleForTextViewTitle;
        private final DeviceType deviceType;

        public Device(DeviceType deviceType) {
            this.deviceType = deviceType;
            switch (deviceType) {
                case FLOWER:
                    this.titleForTextViewTitle = "Flower Bluetooth Settings";
                    break;
                case WAND:
                    this.titleForTextViewTitle = "Wand Bluetooth Settings";
                    break;
                case SQUEEZE:
                    this.titleForTextViewTitle = "Squeeze Bluetooth Settings";
                    break;
                default:
                    this.titleForTextViewTitle = "";
                    Log.w(Constants.LOG_TAG, "Invalid DeviceType in Device constructor");
                    break;
            }
        }

        public DeviceType getDeviceType() {
            return deviceType;
        }

    }

    private TextView textViewTitle;
    private RadioGroup blockRadioGroupPairingMode;
    private RadioButton blockRadioButtonAutomatic;
    private RadioButton blockRadioButtonManual;
    private TextView textViewHeaderDeviceName;
    private TextView textViewPairingModePrompt;
    private TextView textViewHeaderScannedDevices;
    private RecyclerView recyclerViewScannedDevices;
    private TextView textViewButtonEnterDeviceName;

    private Device device;
    private BluetoothAdapter bluetoothAdapter;

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(Constants.LOG_TAG,"onLeScan result: " + device.getName());
            // TODO scanListAdapter.addToList(device);
            super.onScanResult(callbackType, result);
        }
    };


    private void setPairingMode(PairingMode pairingMode) {
        int promptResourceId;
        int visibility;

        switch (pairingMode) {
            case MANUAL:
                promptResourceId = R.string.ble_settings_pairing_mode_manual_description;
                visibility = View.VISIBLE;
                // TODO textViewHeaderDeviceName set string "Device SSID: <name>"
                prepareLeScanning(true);
                break;
            case AUTOMATIC:
            default:
                promptResourceId = R.string.ble_settings_pairing_mode_automatic_description;
                visibility = View.INVISIBLE;
                prepareLeScanning(false);
                break;
        }

        textViewPairingModePrompt.setText(promptResourceId);
        textViewHeaderDeviceName.setVisibility(visibility);
        textViewHeaderScannedDevices.setVisibility(visibility);
        recyclerViewScannedDevices.setVisibility(visibility);
        textViewButtonEnterDeviceName.setVisibility(visibility);
    }


    private synchronized void prepareLeScanning(boolean startScan) {
        Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning()");
        // TODO not sure if this code is still relevant with current SDK and location permissions (scanning code was taken from owlet)
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Log.i(Constants.LOG_TAG, "Don't have permission to do bluetooth scan.");
//
//                new AlertDialog.Builder(this)
//                        .setMessage("This app needs location permissions to scan for nearby Bluetooth devices.")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            // The previous permission check should only be able to fail on M or higher
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//                            }
//                        })
//                        .show();
//            } else {
//                runLeScan();
//            }
            if (startScan) {
                Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning START");
                bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
            } else {
                Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning STOP");
                bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // TODO views and setPairingMode based on device
        if (device != null) {
            this.textViewTitle.setText(device.titleForTextViewTitle);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        prepareLeScanning(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.device = (Device) getIntent().getSerializableExtra(EXTRA_DEVICE);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        this.textViewTitle = findViewById(R.id.textViewTitle);
        this.blockRadioGroupPairingMode = findViewById(R.id.blockRadioGroupPairingMode);
        this.blockRadioButtonAutomatic = findViewById(R.id.blockRadioButtonAutomatic);
        this.blockRadioButtonManual = findViewById(R.id.blockRadioButtonManual);
        this.textViewHeaderDeviceName = findViewById(R.id.textViewHeaderDeviceName);
        this.textViewPairingModePrompt = findViewById(R.id.textViewPairingModePrompt);
        this.textViewHeaderScannedDevices = findViewById(R.id.textViewHeaderScannedDevices);
        this.recyclerViewScannedDevices = findViewById(R.id.recyclerViewScannedDevices);
        this.textViewButtonEnterDeviceName = findViewById(R.id.textViewButtonEnterDeviceName);

        // hide the keyboard when the activity starts (selects input box by default)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViewById(R.id.imageButtonBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textViewButtonEnterDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO actions for manual entry of device name
            }
        });

        blockRadioGroupPairingMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //Log.i(Constants.LOG_TAG, String.format("radio group onCheckedChanged: i=%d", i));
                //Log.i(Constants.LOG_TAG, String.format("radio group onCheckedChanged: blockRadioButtonAutomatic.getId()=%d / blockRadioButtonManual.getId()=%d", blockRadioButtonAutomatic.getId(), blockRadioButtonManual.getId()));

                // TODO update SharedPreferences
                if (i == blockRadioButtonAutomatic.getId()) {
                    setPairingMode(PairingMode.AUTOMATIC);
                } else if (i == blockRadioButtonManual.getId()) {
                    setPairingMode(PairingMode.MANUAL);
                }
            }
        });
    }


    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return null;
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_ble_device_with_drawer;
    }

}
