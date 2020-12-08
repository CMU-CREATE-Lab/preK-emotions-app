package org.cmucreatelab.android.flutterprek;

import org.cmucreatelab.android.flutterprek.ble.DeviceConnectionHandler;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTSettings;

import java.util.UUID;

public class Constants {

    public static final String LOG_TAG = "flutterprek";
    public static final String BLE_LOG_TAG = "ble_flutterprek";

    /** Determines if the flower activity should display the debug window (shows BLE name and last message received). */
    public static final boolean FLOWER_SHOW_DEBUG_WINDOW = false;
    public static final boolean SQUEEZE_SHOW_DEBUG_WINDOW = false;
    public static final boolean WAND_SHOW_DEBUG_WINDOW = false;

    /** Determines what type of activity will run for the "choose emotion" step of a student's session. Setting to false will use the legacy interface (displays emotions only). */
    public static final boolean CHOOSE_EMOTION_WITH_TALK_ABOUT_IT_OPTION = false;

    public static final UARTSettings FLOWER_UART_SETTINGS;
    public static final UARTSettings SQUEEZE_UART_SETTINGS;
    public static final UARTSettings WAND_UART_SETTINGS;

    static {
        FLOWER_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxCharacteristicUUID(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setTxCharacteristicUUID(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                .build();
        SQUEEZE_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxCharacteristicUUID(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setTxCharacteristicUUID(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                .build();

        WAND_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxCharacteristicUUID(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setTxCharacteristicUUID(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"))
                .build();
    }

    public static final String MOON_FLOWER = "FLOWER-7F257";
    public static final String MOON_SQUEEZE = "SQWZ-D91E9";
    public static final String MOON_WAND = "WAND-1F216";

    public static final String HEART_FLOWER = "FLOWER-ED7AE";
    public static final String HEART_SQUEEZE = "SQWZ-4EA63";
    public static final String HEART_WAND = "WAND-A01B9";

    public static final String SMILEY_FLOWER = "FLOWER-22B26";
    public static final String SMILEY_SQUEEZE = "SQWZ-D20C5";
    public static final String SMILEY_WAND = "WAND-333FA";

    public static final String TREE_FLOWER = "FLOWER-6A57F";
    public static final String TREE_SQUEEZE = "SQWZ-B8FE2";
    public static final String TREE_WAND = "WAND-57BBB";

    public static final DeviceConnectionHandler.HardcodedValues
            // Stations with Tangibles
            STATION_MOON__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF19 2020Fall", MOON_FLOWER, MOON_SQUEEZE, MOON_WAND,null),
            STATION_HEART__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83D\uDC96 2020Fall", HEART_FLOWER, HEART_SQUEEZE, HEART_WAND,null),
            STATION_SMILEY__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("🙂 2020Fall", SMILEY_FLOWER, SMILEY_SQUEEZE, SMILEY_WAND,null),
            STATION_TREE__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF32 2020Fall", TREE_FLOWER, TREE_SQUEEZE, TREE_WAND,null),

            // Standalone Version
            STATION_STANDALONE__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("S 2020Fall", TREE_FLOWER, TREE_SQUEEZE, TREE_WAND,null),

            // avoids need to update semi-colon placement; do not actually use this
            NULL_STATION = null;

    /**
     * Set this to use hardcoded values for the MindfulNest BLE devices.
     * This is helpful when there are multiple stations in the same room.
     * Otherwise set to null.
     */
    public static final DeviceConnectionHandler.HardcodedValues HARDCODED_VALUES = STATION_STANDALONE__FALL_2020;


    /** This file is defined in the assets/ directory. */
    public static final String DATABASE_SEED;
    static {
        if (HARDCODED_VALUES == STATION_STANDALONE__FALL_2020) {
            DATABASE_SEED = "DbSeedNoTangiblesPD1.json";
        } else {
            DATABASE_SEED = "DbSeedWithTangiblesPD2.json";
        }
    }

}
