package org.cmucreatelab.android.flutterprek;

import org.cmucreatelab.android.flutterprek.ble.DeviceConnectionHandler;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTSettings;

import java.util.UUID;

public class Constants {

    public static final String LOG_TAG = "flutterprek";

    /** This file is defined in the assets/ directory. */
    public static final String DATABASE_SEED = "DbSeedPD3.json";

    /** Determines if the flower activity should display the debug window (shows BLE name and last message received). */
    public static final boolean FLOWER_SHOW_DEBUG_WINDOW = false;
    public static final boolean SQUEEZE_SHOW_DEBUG_WINDOW = false;
    public static final boolean WAND_SHOW_DEBUG_WINDOW = false;

    /** Determines what type of activity will run for the "choose emotion" step of a student's session. Setting to false will use the legacy interface (displays emotions only). */
    public static final boolean CHOOSE_EMOTION_WITH_TALK_ABOUT_IT_OPTION = true;

    public static final UARTSettings FLOWER_UART_SETTINGS;
    public static final UARTSettings SQUEEZE_UART_SETTINGS;
    public static final UARTSettings WAND_UART_SETTINGS;

    //TODO Delete
    public static int fileStart = 0;

    static {
        FLOWER_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxCharacteristicUUID(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setTxCharacteristicUUID(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                .build();

        SQUEEZE_UART_SETTINGS = new UARTSettings.Builder()
                // first on main screen
                .setUARTServiceUUID(UUID.fromString("BEA5760D-503D-4920-B000-101E7306B000"))
                // last on main screen
                .setRxCharacteristicUUID(UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FD"))
                .setTxCharacteristicUUID(UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FD"))
                // page with the value
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                .build();

        WAND_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxCharacteristicUUID(UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setTxCharacteristicUUID(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"))
                .build();
    }

    public static final DeviceConnectionHandler.HardcodedValues
            STATION_HEART = new DeviceConnectionHandler.HardcodedValues("\uD83D\uDC96","FLOWER-E5F6C","MNSQ1","WAND-8A224",null),
            STATION_STAR = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1F","FLOWER-E5E22","MNSQ2","WAND-E1A98",null),
            STATION_SMILEY = new DeviceConnectionHandler.HardcodedValues("🙂","FLOWER-4D605","MNSQ5","WAND-7C185",null),
            STATION_SUN = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF1E","FLOWER-AC43D","MNSQ4","WAND-7426D",null),
            STATION_MOON = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF19","FLOWER-E167F","MNSQ6","WAND-A8D12",null),
            STATION_TREE = new DeviceConnectionHandler.HardcodedValues("\uD83C\uDF32","FLOWER-595A4","MNSQ3","WAND-E2A59",null);

    /**
     * Set this to use hardcoded values for the MindfulNest BLE devices.
     * This is helpful when there are multiple stations in the same room.
     * Otherwise set to null.
     */
    public static final DeviceConnectionHandler.HardcodedValues HARDCODED_VALUES = STATION_HEART;

}
