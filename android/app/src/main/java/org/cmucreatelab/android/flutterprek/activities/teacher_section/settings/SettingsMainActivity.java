package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
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
    private Switch switchPostCopingSkills;
    private SeekBar seekBarPromptRepeatDelay;
    private TextView textViewHeader2PromptRepeat;


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


    private void updateViewsForGeneralAppSettings() {
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());

        // switch
        switchPostCopingSkills.setChecked(sharedPreferences.getBoolean(Constants.PreferencesKeys.settingsPostCopingSkills, Constants.DEFAULT_USE_POST_COPING_SKILLS));

        // seekbar
        long value = sharedPreferences.getLong(Constants.PreferencesKeys.settingsRepromptInMilliseconds, Constants.DEFAULT_REPROMPT_IN_MILLISECONDS);;
        int seekBarValue = (int)(value/1000);
        seekBarPromptRepeatDelay.setProgress(seekBarValue);
        updateViewForTextViewHeader2PromptRepeat(seekBarValue);
    }


    private void updateViewForTextViewHeader2PromptRepeat(int seekBarValue) {
        final String text = String.format("Repeat Audio Prompts after %d seconds", seekBarValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewHeader2PromptRepeat.setText(text);
            }
        });
    }


    public void updateViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateViewsForPairingMode();
                updateViewsForGeneralAppSettings();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowerBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsFlower), (TextView) findViewById(R.id.textViewBleSettingsFlower), (TextView) findViewById(R.id.textButtonBleSettingsFlower));
        squeezeBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsSqueeze), (TextView) findViewById(R.id.textViewBleSettingsSqueeze), (TextView) findViewById(R.id.textButtonBleSettingsSqueeze));
        wandBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsWand), (TextView) findViewById(R.id.textViewBleSettingsWand), (TextView) findViewById(R.id.textButtonBleSettingsWand));

        switchPostCopingSkills = findViewById(R.id.switchPostCopingSkills);
        seekBarPromptRepeatDelay = findViewById(R.id.seekBarPromptRepeatDelay);
        textViewHeader2PromptRepeat = findViewById(R.id.textViewHeader2PromptRepeat);

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
        switchPostCopingSkills.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                GlobalHandler.getSharedPreferences(getApplicationContext()).edit().putBoolean(Constants.PreferencesKeys.settingsPostCopingSkills, b).apply();
            }
        });
        seekBarPromptRepeatDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // TODO change value (text)
                Log.v(Constants.LOG_TAG, "SeekBar.onProgressChanged: value = "+i);
                updateViewForTextViewHeader2PromptRepeat(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                Log.v(Constants.LOG_TAG, "SeekBar.onStopTrackingTouch: value = "+i);
                // get value in milliseconds
                long value = i*1000;
                GlobalHandler.getSharedPreferences(getApplicationContext()).edit().putLong(Constants.PreferencesKeys.settingsRepromptInMilliseconds, value).apply();
                updateViewForTextViewHeader2PromptRepeat(i);
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
