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
    public static final UARTSettings WAND2_UART_SETTINGS;

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
                .setUARTServiceUUID(UUID.fromString("BEA5760D-503D-4920-B000-101E7306B000"))
                //Gyroscope
                //.setRxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"))
                //Accelerometer
                .setRxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fc"))
                .setTxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fe"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"))
                .build();
        WAND2_UART_SETTINGS = new UARTSettings.Builder()
                .setUARTServiceUUID(UUID.fromString("BEA5760D-503D-4920-B000-101E7306B000"))
                .setRxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"))
                //.setRxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fc"))
                .setTxCharacteristicUUID(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fe"))
                .setRxConfigUUID(UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"))
                .build();
    }

    public static final DeviceConnectionHandler.HardcodedValues
            STATION_TEST = new DeviceConnectionHandler.HardcodedValues("test","FLOWER-AEF3D",null, null, null),
            STATION_TEST_V2 = new DeviceConnectionHandler.HardcodedValues("test-v2","FLOWER-30384",null, null, null),
            STATION_1 = new DeviceConnectionHandler.HardcodedValues("heart","FLOWER-9B4BC","MNSQ1","MNWD2","MNYG1"),
            STATION_2 = new DeviceConnectionHandler.HardcodedValues("star","FLOWER-9C915","MNSQ3","MNWD1","MNYG2");

    /**
     * Set this to use hardcoded values for the MindfulNest BLE devices.
     * This is helpful when there are multiple stations in the same room.
     * Otherwise set to null.
     */
    //public static final DeviceConnectionHandler.HardcodedValues HARDCODED_VALUES = STATION_2;
    public static final DeviceConnectionHandler.HardcodedValues HARDCODED_VALUES = new DeviceConnectionHandler.HardcodedValues("heart + new flower","FLOWER-4D605","MNSQ1","MNWD1",null);

}
