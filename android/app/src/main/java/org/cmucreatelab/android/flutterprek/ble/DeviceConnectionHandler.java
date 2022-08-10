package org.cmucreatelab.android.flutterprek.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueeze;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWand;

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

    private boolean validateSqueezeOnPrefix(@NonNull String deviceName) {
        return deviceName.startsWith("SHEEP-");
    }

    private boolean validateWandOnPrefix(@NonNull String deviceName) {
        return deviceName.startsWith("WAND-");
    }

    private boolean validateFlowerHardcoded(@NonNull String deviceName) {
        if (hardcodedBleFlower == null) {
            Log.w(Constants.LOG_TAG, "Hardcoded value was null; returning false");
            return false;
        }
        return deviceName.equals(hardcodedBleFlower);
    }

    private boolean validateSqueezeHardcoded(@NonNull String deviceName) {
        if (hardcodedBleSqueeze == null) {
            Log.w(Constants.LOG_TAG, "Hardcoded value was null; returning false");
            return false;
        }
        return deviceName.equals(hardcodedBleSqueeze);
    }

    private boolean validateWandHardcoded(@NonNull String deviceName) {
        if (hardcodedBleWand == null) {
            Log.w(Constants.LOG_TAG, "Hardcoded value was null; returning false");
            return false;
        }
        return deviceName.equals(hardcodedBleWand);
    }


    public DeviceConnectionHandler(@Nullable HardcodedValues hardcodedValues) {
        if (hardcodedValues == null) {
            this.usesHardcodedBleDevices = false;
        } else {
            this.usesHardcodedBleDevices = true;
            this.hardcodedBleFlower = hardcodedValues.flower;
            this.hardcodedBleSqueeze = hardcodedValues.squeeze;
            this.hardcodedBleWand = hardcodedValues.wand;
            this.hardcodedBleYoga = hardcodedValues.yoga;
        }
    }


    /**
     * Determine if a BLE device is valid given its device name.
     * @param classToValidate specifies what type of device we are expecting (example: {@link BleFlower}).
     * @param deviceName the name of the device, likely obtained from {@link BluetoothDevice#getName()}.
     * @return true if the device is valid, false otherwise.
     */
    public boolean checkIfValidBleDevice(Class classToValidate, @NonNull String deviceName, Context appContext) {
        if (usesHardcodedBleDevices) {
            if (classToValidate == BleFlower.class) {
                return validateFlowerHardcoded(deviceName);
            } else if (classToValidate == BleSqueeze.class) {
                Log.v(Constants.LOG_TAG, "found valid squeeze class");
                return validateSqueezeHardcoded(deviceName);
            } else if(classToValidate == BleWand.class) {
                return validateWandHardcoded(deviceName);
            } else {
                Log.e(Constants.LOG_TAG, "checkIfValidBleDevice Could not determine class; returning false");
            }
            return false;
        } else {
            SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(appContext);
            boolean pairingModeIsManual;
            String ssid;

            if (classToValidate == BleFlower.class) {
                pairingModeIsManual = sharedPreferences.getBoolean(Constants.PreferencesKeys.flowerPairingModeManual, false);
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.flowerSsid, "");

                if (pairingModeIsManual && !ssid.isEmpty()) {
                    return deviceName.equals(ssid);
                }
                return validateFlowerOnPrefix(deviceName);
            } else if (classToValidate == BleSqueeze.class) {
                pairingModeIsManual = sharedPreferences.getBoolean(Constants.PreferencesKeys.squeezePairingModeManual, false);
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.squeezeSsid, "");

                if (pairingModeIsManual && !ssid.isEmpty()) {
                    return deviceName.equals(ssid);
                }
                return validateSqueezeOnPrefix(deviceName);
            } else if(classToValidate == BleWand.class) {
                pairingModeIsManual = sharedPreferences.getBoolean(Constants.PreferencesKeys.wandPairingModeManual, false);
                ssid = sharedPreferences.getString(Constants.PreferencesKeys.wandSsid, "");

                if (pairingModeIsManual && !ssid.isEmpty()) {
                    return deviceName.equals(ssid);
                }
                return validateWandOnPrefix(deviceName);
            } else {
                Log.e(Constants.LOG_TAG, "checkIfValidBleDevice Could not determine class; returning false");
            }
            //return false;
        }
        return false;
    }

}
