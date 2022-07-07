package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;

public class SettingsBleActivity extends TeacherSectionActivityWithHeaderAndDrawer {

    private TextView textViewTitle;
    private RadioGroup blockRadioGroupPairingMode;
    private TextView textViewHeaderDeviceName;
    private TextView textViewPairingModePrompt;
    private RecyclerView recyclerViewScannedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.textViewTitle = findViewById(R.id.textViewTitle);
        this.blockRadioGroupPairingMode = findViewById(R.id.blockRadioGroupPairingMode);
        this.textViewHeaderDeviceName = findViewById(R.id.textViewHeaderDeviceName);
        this.textViewPairingModePrompt = findViewById(R.id.textViewPairingModePrompt);
        this.recyclerViewScannedDevices = findViewById(R.id.recyclerViewScannedDevices);

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
        findViewById(R.id.textViewButtonEnterDeviceName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO actions for manual entry of device name
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
