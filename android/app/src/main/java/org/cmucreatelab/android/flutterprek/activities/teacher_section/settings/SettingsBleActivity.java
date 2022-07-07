package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

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


    private void setPairingMode(PairingMode pairingMode) {
        int promptResourceId;
        int visibility;

        switch (pairingMode) {
            case MANUAL:
                promptResourceId = R.string.ble_settings_pairing_mode_manual_description;
                visibility = View.VISIBLE;
                // TODO textViewHeaderDeviceName set string "Device SSID: <name>"
                break;
            case AUTOMATIC:
            default:
                promptResourceId = R.string.ble_settings_pairing_mode_automatic_description;
                visibility = View.INVISIBLE;
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

        // TODO views and setPairingMode based on device
        if (device != null) {
            this.textViewTitle.setText(device.titleForTextViewTitle);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.device = (Device) getIntent().getSerializableExtra(EXTRA_DEVICE);

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
