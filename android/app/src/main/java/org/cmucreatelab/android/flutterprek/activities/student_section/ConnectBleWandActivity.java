package org.cmucreatelab.android.flutterprek.activities.student_section;

import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand.WandCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWand;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWandScanner;

public class ConnectBleWandActivity extends ConnectBleDeviceActivity {

    private BleWandScanner bleScanner;


    @Override
    public boolean bleDeviceIsConnected() {
        return GlobalHandler.getInstance(getApplicationContext()).isWandConnected();
    }


    @Override
    public Class getClassForCopingSkillActivity() {
        return WandCopingSkillActivity.class;
    }


    @Override
    public void startBleScanner() {
        if (bleScanner != null) bleScanner.stopScan();
        this.bleScanner = new BleWandScanner(this, new BleWandScanner.DiscoveryListener() {
            @Override
            public void onDiscovered(BleWand bleWand) {
                // does nothing
            }
        }, new UARTConnection.ConnectionListener() {
            @Override
            public void onConnected() {
                finish();
            }

            @Override
            public void onDisconnected() {
                // does nothing
            }
        });
        bleScanner.requestScan();
    }

    public void stopBleScanner() {
        bleScanner.stopScan();
    }


    @Override
    public String getTextForTitle() {
        return "Trying to Connect to Wand...";
    }

}
