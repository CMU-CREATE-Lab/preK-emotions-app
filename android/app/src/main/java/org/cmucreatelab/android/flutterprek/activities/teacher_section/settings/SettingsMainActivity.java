package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.settings.troubleshoot_device.SettingsTroubleshootDeviceIndex;

public class SettingsMainActivity extends TeacherSectionActivityWithHeaderAndDrawer {

    private static class BluetoothSettings {

        public final TextView header, text, button;

        public BluetoothSettings(TextView header, TextView text, TextView button) {
            this.header = header;
            this.text = text;
            this.button = button;
        }

    }

    private BluetoothSettings flowerBleSettings, squeezeBleSettings, wandBleSettings;


    private void startSettingsBleActivity(SettingsBleActivity.DeviceType deviceType) {
        Intent settingsBleActivity = new Intent(getApplicationContext(), SettingsBleActivity.class);
        settingsBleActivity.putExtra(SettingsBleActivity.EXTRA_DEVICE, new SettingsBleActivity.Device(deviceType));
        startActivity(settingsBleActivity);
    }


    private void updateViewsForPairingMode() {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());
        String ssid = "";
        TextView textViewBleSettingsFlower = findViewById(R.id.textViewBleSettingsFlower);
        TextView textViewBleSettingsSqueeze = findViewById(R.id.textViewBleSettingsSqueeze);
        TextView textViewBleSettingsWand = findViewById(R.id.textViewBleSettingsWand);

        // flower
        if (sharedPreferences.getBoolean(Constants.PreferencesKeys.flowerPairingModeManual, false)) {
            ssid = sharedPreferences.getString(Constants.PreferencesKeys.flowerSsid, "");
            textViewBleSettingsFlower.setText(String.format(getString(R.string.teacher_section_settings_pairing_mode_manual), ssid));
        } else {
            textViewBleSettingsFlower.setText(R.string.teacher_section_settings_pairing_mode_automatic);
        }

        // wand
        if (sharedPreferences.getBoolean(Constants.PreferencesKeys.wandPairingModeManual, false)) {
            ssid = sharedPreferences.getString(Constants.PreferencesKeys.wandSsid, "");
            textViewBleSettingsWand.setText(String.format(getString(R.string.teacher_section_settings_pairing_mode_manual), ssid));
        } else {
            textViewBleSettingsWand.setText(R.string.teacher_section_settings_pairing_mode_automatic);
        }

        // squeeze
        if (sharedPreferences.getBoolean(Constants.PreferencesKeys.squeezePairingModeManual, false)) {
            ssid = sharedPreferences.getString(Constants.PreferencesKeys.squeezeSsid, "");
            textViewBleSettingsSqueeze.setText(String.format(getString(R.string.teacher_section_settings_pairing_mode_manual), ssid));
        } else {
            textViewBleSettingsSqueeze.setText(R.string.teacher_section_settings_pairing_mode_automatic);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateViewsForPairingMode();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowerBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsFlower), (TextView) findViewById(R.id.textViewBleSettingsFlower), (TextView) findViewById(R.id.textButtonBleSettingsFlower));
        squeezeBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsSqueeze), (TextView) findViewById(R.id.textViewBleSettingsSqueeze), (TextView) findViewById(R.id.textButtonBleSettingsSqueeze));
        wandBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsWand), (TextView) findViewById(R.id.textViewBleSettingsWand), (TextView) findViewById(R.id.textButtonBleSettingsWand));

        flowerBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsBleActivity(SettingsBleActivity.DeviceType.FLOWER);
            }
        });
        squeezeBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsBleActivity(SettingsBleActivity.DeviceType.SQUEEZE);
            }
        });
        wandBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsBleActivity(SettingsBleActivity.DeviceType.WAND);
            }
        });

        findViewById(R.id.textButtonChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsPasswordActivity = new Intent(getApplicationContext(), SettingsPasswordActivity.class);
                startActivity(settingsPasswordActivity);
            }
        });
        findViewById(R.id.textButtonTroubleshootDevices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsTroubleshootDeviceIndex = new Intent(getApplicationContext(), SettingsTroubleshootDeviceIndex.class);
                startActivity(settingsTroubleshootDeviceIndex);
            }
        });
    }


    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return DrawerTeacherMainFragment.Section.SETTINGS;
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_main;
    }

}
