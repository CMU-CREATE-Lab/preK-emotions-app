package org.cmucreatelab.android.flutterprek.activities.student_section;

import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow.FlowerRainbowCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlowerScanner;

public class ConnectBleFlowerActivity extends ConnectBleDeviceActivity {

    private BleFlowerScanner bleScanner;


    @Override
    public boolean bleDeviceIsConnected() {
        return GlobalHandler.getInstance(getApplicationContext()).isFlowerConnected();
    }


    @Override
    public Class getClassForCopingSkillActivity() {
        return FlowerRainbowCopingSkillActivity.class;
    }


    @Override
    public void startBleScanner() {
        if (bleScanner != null) bleScanner.stopScan();
        this.bleScanner = new BleFlowerScanner(this, new BleFlowerScanner.DiscoveryListener() {
            @Override
            public void onDiscovered(BleFlower bleFlower) {
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
        return "Trying to Connect to Flower...";
    }

}
