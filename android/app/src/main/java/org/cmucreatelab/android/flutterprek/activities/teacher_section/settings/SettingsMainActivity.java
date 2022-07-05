package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowerBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsFlower), (TextView) findViewById(R.id.textViewBleSettingsFlower), (TextView) findViewById(R.id.textButtonBleSettingsFlower));
        squeezeBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsSqueeze), (TextView) findViewById(R.id.textViewBleSettingsSqueeze), (TextView) findViewById(R.id.textButtonBleSettingsSqueeze));
        wandBleSettings = new BluetoothSettings((TextView) findViewById(R.id.textViewHeaderBleSettingsWand), (TextView) findViewById(R.id.textViewBleSettingsWand), (TextView) findViewById(R.id.textButtonBleSettingsWand));

        // TODO testing click listeners for now
        flowerBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Flower", Toast.LENGTH_SHORT).show();
            }
        });
        squeezeBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Squeeze", Toast.LENGTH_SHORT).show();
            }
        });
        wandBleSettings.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Wand", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.textButtonChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Change Password", Toast.LENGTH_SHORT).show();
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
