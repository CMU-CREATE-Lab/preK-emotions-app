package org.cmucreatelab.android.flutterprek.ble;

import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;

/**
 * Determines if a broadcasted bluetooth device is valid for the app to try connecting to.
 */
public class DeviceConnectionHandler {

    private boolean usesHardcodedBleDevices;

    private String hardcodedBleFlower;
    private String hardcodedBleSqueeze;
    private String hardcodedBleWand;
    private String hardcodedBleYoga;

    public static class HardcodedValues {
        private @NonNull String stationName;
        public String flower, squeeze, wand, yoga;

        public HardcodedValues(@NonNull String stationName, String flower, String squeeze, String wand, String yoga) {
            this.stationName = stationName;
            this.flower = flower;
            this.squeeze = squeeze;
            this.wand = wand;
            this.yoga = yoga;
        }

        public String getStationName() {
            return stationName;
        }
    }


    private boolean validateFlowerOnPrefix(@NonNull String deviceName) {
        return deviceName.startsWith("FLOWER-");
    }


    private boolean validateFlowerHardcoded(@NonNull String deviceName) {
        if (hardcodedBleFlower == null) {
            Log.w(Constants.LOG_TAG, "Hardcoded value was null; returning false");
            return false;
        }
        return deviceName.equals(hardcodedBleFlower);
    }


    public DeviceConnectionHandler() {
        this.usesHardcodedBleDevices = false;
    }


    public DeviceConnectionHandler(HardcodedValues hardcodedValues) {
        this.usesHardcodedBleDevices = true;
        this.hardcodedBleFlower = hardcodedValues.flower;
        this.hardcodedBleSqueeze = hardcodedValues.squeeze;
        this.hardcodedBleWand = hardcodedValues.wand;
        this.hardcodedBleYoga = hardcodedValues.yoga;
    }


    public boolean checkIfValidBleDevice(Class classToValidate, String deviceName) {
        // TODO add other hardware device classes
        if (classToValidate == BleFlower.class) {
            if (usesHardcodedBleDevices) {
                return validateFlowerHardcoded(deviceName);
            }
            return validateFlowerOnPrefix(deviceName);
        } else {
            Log.e(Constants.LOG_TAG, "checkIfValidBleDevice Could not determine class; returning false");
        }
        return false;
    }

}
