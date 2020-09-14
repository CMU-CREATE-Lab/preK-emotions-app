package org.cmucreatelab.android.flutterprek;

import org.cmucreatelab.android.flutterprek.ble.DeviceConnectionHandler;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTSettings;

import java.util.UUID;

public class Constants {

    public static final String LOG_TAG = "flutterprek";
    public static final String BLE_LOG_TAG = "ble_flutterprek";

    /** This file is defined in the assets/ directory. */
    public static final String DATABASE_SEED = "DbSeed.json";

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

//        SQUEEZE_UART_SETTINGS = new UARTSettings.Builder()
//                // first on main screen
//                .setUARTServiceUUID(UUID.fromString("BEA5760D-503D-4920-B000-101E7306B000"))
//                // last on main screen
//                .setRxCharacteristicUUID(UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FD"))
//                .setTxCharacteristicUUID(UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FD"))
//                // page with the value
//                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
//                .build();
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

    // New Squeeze BLE Broadcast Names (help keep track of type)
    public static final String SQUEEZE_1_TURTLE = "SQWZ-BC94B";
    public static final String SQUEEZE_2_FISH = "SQWZ-11E23";
    public static final String SQUEEZE_3_TURTLE = "SQWZ-4B3A8";
    public static final String SQUEEZE_4_FISH = "SQWZ-64BED";
    public static final String SQUEEZE_5_TURTLE = "SQWZ-18770";
    public static final String SQUEEZE_6_FISH = "SQWZ-3E567";

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
            STATION_HEART = new DeviceConnectionHandler.HardcodedValues("\uD83D\uDC96","FLOWER-E5F6C","MNSQ1","WAND-8A224",null),
            STATION_STAR = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1F","FLOWER-E5E22","MNSQ2","WAND-E1A98",null),
            STATION_SMILEY = new DeviceConnectionHandler.HardcodedValues("🙂","FLOWER-4D605","MNSQ5","WAND-7C185",null),
            STATION_SUN = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1E","FLOWER-AC43D","MNSQ4","WAND-7426D",null),
            STATION_MOON = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF19","FLOWER-E167F","MNSQ6","WAND-A8D12",null),
            STATION_TREE = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF32","FLOWER-595A4","MNSQ3","WAND-E2A59",null),
            STATION_SUN_WITH_TURTLE = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1E + \uD83D\uDC22","FLOWER-AC43D", SQUEEZE_1_TURTLE,"WAND-7426D",null),
            STATION_MOON_WITH_FISH = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF19 + \uD83D\uDC20","FLOWER-E167F", SQUEEZE_2_FISH,"WAND-A8D12",null),
            STATION_HEART_WITH_TURTLE = new DeviceConnectionHandler.HardcodedValues("\uD83D\uDC96 + \uD83D\uDC22","FLOWER-E5F6C", SQUEEZE_3_TURTLE,"WAND-8A224",null),
            STATION_STAR_WITH_FISH = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1F + \uD83D\uDC20","FLOWER-E5E22", SQUEEZE_4_FISH,"WAND-E1A98",null),
            STATION_TREE_WITH_TURTLE = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF32 + \uD83D\uDC22","FLOWER-595A4",SQUEEZE_5_TURTLE,"WAND-E2A59",null),
            STATION_SMILEY_WITH_FISH = new DeviceConnectionHandler.HardcodedValues("🙂 + \uD83D\uDC20","FLOWER-4D605", SQUEEZE_6_FISH,"WAND-7C185",null),

            STATION_MOON__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF19 2020", MOON_FLOWER, MOON_SQUEEZE, MOON_WAND,null),
            STATION_HEART__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83D\uDC96 2020", HEART_FLOWER, HEART_FLOWER, HEART_FLOWER,null),
            STATION_SMILEY__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("🙂 2020", SMILEY_FLOWER, SMILEY_FLOWER, SMILEY_FLOWER,null),
            STATION_TREE__FALL_2020 = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF32 2020", TREE_FLOWER, TREE_FLOWER, TREE_FLOWER,null),
            // avoids need to update semi-colon placement; do not actually use this
            NULL_STATION = null;

    /**
     * Set this to use hardcoded values for the MindfulNest BLE devices.
     * This is helpful when there are multiple stations in the same room.
     * Otherwise set to null.
     */
    public static final DeviceConnectionHandler.HardcodedValues HARDCODED_VALUES = STATION_MOON__FALL_2020;

}
