package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentUpdateAbstractActivity;
import org.cmucreatelab.android.flutterprek.ble.scan.ScanViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class SettingsBleActivity extends TeacherSectionActivityWithHeaderAndDrawer implements ScanViewAdapter.OnListInteractionListener {

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
    private ScanViewAdapter scanViewAdapter;

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(Constants.LOG_TAG,"onLeScan result: " + device.getName());
            scanViewAdapter.addToList(device);
            super.onScanResult(callbackType, result);
        }
    };


    private void setPairingMode(PairingMode pairingMode) {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());

        // false implies automatic pairing mode
        boolean value = (pairingMode == PairingMode.MANUAL);

        switch (device.getDeviceType()) {
            case FLOWER:
                sharedPreferences.edit().putBoolean(Constants.PreferencesKeys.flowerPairingModeManual, value).apply();
                break;
            case WAND:
                sharedPreferences.edit().putBoolean(Constants.PreferencesKeys.wandPairingModeManual, value).apply();
                break;
            case SQUEEZE:
                sharedPreferences.edit().putBoolean(Constants.PreferencesKeys.squeezePairingModeManual, value).apply();
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not setPairingMode; unknown device type.");
                return;
        }

        updateViews();
    }


    private void setSsidForDevice(String ssid) {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());

        switch (device.getDeviceType()) {
            case FLOWER:
                sharedPreferences.edit().putString(Constants.PreferencesKeys.flowerSsid, ssid).apply();
                break;
            case WAND:
                sharedPreferences.edit().putString(Constants.PreferencesKeys.wandSsid, ssid).apply();
                break;
            case SQUEEZE:
                sharedPreferences.edit().putString(Constants.PreferencesKeys.squeezeSsid, ssid).apply();
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not setSsidForDevice; unknown device type.");
                return;
        }

        updateViews();
    }


    private PairingMode getPairingMode() {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());
        boolean value;

        switch (device.getDeviceType()) {
            case FLOWER:
                value = sharedPreferences.getBoolean(Constants.PreferencesKeys.flowerPairingModeManual, false);
                break;
            case WAND:
                value = sharedPreferences.getBoolean(Constants.PreferencesKeys.wandPairingModeManual, false);
                break;
            case SQUEEZE:
                value = sharedPreferences.getBoolean(Constants.PreferencesKeys.squeezePairingModeManual, false);
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not getPairingMode; unknown device type.");
                return null;
        }

        return value ? PairingMode.MANUAL : PairingMode.AUTOMATIC;
    }


    private String getSsidForDevice() {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());
        String ssid;

        switch (device.getDeviceType()) {
            case FLOWER:
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.flowerSsid, "");
                break;
            case WAND:
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.wandSsid, "");
                break;
            case SQUEEZE:
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.squeezeSsid, "");
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not getSsidForDevice; unknown device type.");
                return null;
        }

        return ssid;
    }


    private synchronized void prepareLeScanning(boolean startScan) {
        Log.d(Constants.LOG_TAG,"SettingsBleActivity.prepareLeScanning()");
        scanViewAdapter.clearList();

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


    public void updateViews() {
        String ssid = getSsidForDevice();
        PairingMode pairingMode = getPairingMode();

        // update views based on pairing mode
        int promptResourceId;
        int visibility;

        switch (pairingMode) {
            case MANUAL:
                promptResourceId = R.string.ble_settings_pairing_mode_manual_description;
                visibility = View.VISIBLE;
                textViewHeaderDeviceName.setText(String.format("Device SSID: %s", ssid));
                blockRadioButtonManual.setChecked(true);
                prepareLeScanning(true);
                break;
            case AUTOMATIC:
            default:
                promptResourceId = R.string.ble_settings_pairing_mode_automatic_description;
                visibility = View.INVISIBLE;
                blockRadioButtonAutomatic.setChecked(true);
                prepareLeScanning(false);
                break;
        }

        textViewPairingModePrompt.setText(promptResourceId);
        textViewHeaderDeviceName.setVisibility(visibility);
        textViewHeaderScannedDevices.setVisibility(visibility);
        recyclerViewScannedDevices.setVisibility(visibility);
        textViewButtonEnterDeviceName.setVisibility(visibility);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.textViewTitle = findViewById(R.id.textViewTitle);
        this.blockRadioGroupPairingMode = findViewById(R.id.blockRadioGroupPairingMode);
        this.blockRadioButtonAutomatic = findViewById(R.id.blockRadioButtonAutomatic);
        this.blockRadioButtonManual = findViewById(R.id.blockRadioButtonManual);
        this.textViewHeaderDeviceName = findViewById(R.id.textViewHeaderDeviceName);
        this.textViewPairingModePrompt = findViewById(R.id.textViewPairingModePrompt);
        this.textViewHeaderScannedDevices = findViewById(R.id.textViewHeaderScannedDevices);
        this.recyclerViewScannedDevices = findViewById(R.id.recyclerViewScannedDevices);
        this.textViewButtonEnterDeviceName = findViewById(R.id.textViewButtonEnterDeviceName);

        this.device = (Device) getIntent().getSerializableExtra(EXTRA_DEVICE);
        if (device != null) {
            this.textViewTitle.setText(device.titleForTextViewTitle);
        }

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        this.scanViewAdapter = new ScanViewAdapter(new ArrayList<BluetoothDevice>(), this);
        recyclerViewScannedDevices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewScannedDevices.setAdapter(scanViewAdapter);

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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SettingsBleActivity.this);
                final View alertView = getLayoutInflater().inflate(R.layout.dialog_device_ssid, null);
                builder.setView(alertView)
                        .setPositiveButton(R.string.alert_option_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String deviceName = ((EditText) alertView.findViewById(R.id.editTextDeviceName)).getText().toString();
                                Log.d(Constants.LOG_TAG, String.format("dialog onClick positive; ssid='%s'", deviceName));
                                setSsidForDevice(deviceName);
                            }
                        })
                        .setNegativeButton(R.string.alert_option_cancel, null)
                        .setTitle(R.string.alert_title_ble_manual)
                        .setMessage(R.string.alert_message_ble_manual);
                builder.create().show();
            }
        });

        blockRadioGroupPairingMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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


    @Override
    public void onItemSelected(BluetoothDevice item) {
        final String deviceName = item.getName();
        Log.d(Constants.LOG_TAG, "onItemSelected: " + deviceName);
        String alertMessage = String.format(getString(R.string.alert_message_ble_select), deviceName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertMessage);
        builder.setTitle(R.string.alert_title_ble_select);
        builder.setPositiveButton(R.string.alert_option_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setSsidForDevice(deviceName);
            }
        });
        builder.setNegativeButton(R.string.alert_option_cancel, null);
        builder.create().show();
    }

}
