package org.cmucreatelab.android.flutterprek.activities.student_section;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;

public abstract class ConnectBleDeviceActivity extends StudentSectionActivityWithHeader {

    private int itineraryIndex;
    
    public abstract boolean bleDeviceIsConnected();
    public abstract Class getClassForCopingSkillActivity();
    public abstract void startBleScanner();
    public abstract void stopBleScanner();
    public abstract String getTextForTitle();


    public void updateTextTitle() {
        TextView tv = findViewById(R.id.textTitle);
        tv.setText(getTextForTitle());
    }


    @Override
    protected void onResume() {
        super.onResume();

        updateTextTitle();

        if (bleDeviceIsConnected()) {
            finish();
        } else {
            startBleScanner();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
    }


    @Override
    protected void onPause() {
        stopBleScanner();
        super.onPause();
    }


    @Override
    public void finish() {
        Intent intent = new Intent(this, getClassForCopingSkillActivity());
        intent.putExtra(ITINERARY_INDEX, itineraryIndex);
        startActivity(intent);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_connect_ble_device;
    }

}
