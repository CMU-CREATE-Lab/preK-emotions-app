package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings.troubleshoot_device;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.DebugCorner;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;

public class SettingsTroubleshootDeviceShow extends TeacherSectionActivityWithHeaderAndDrawer implements UARTConnection.ConnectionListener, UARTConnection.RXDataListener, TroubleshootDeviceProcess.EventsCallback {

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_IS_CONNECTED = "isConnected";

    private UARTConnection uartConnection;
    private TextView textViewTitle;
    private Button buttonBluetooth;
    private TableLayout tableLayoutTroubleshootingBle;
    private TextView labelFirmwareVersion, valueFirmwareVersion;
    private TextView labelPowerLedState, valuePowerLedState;
    private TextView labelBatteryPercentage, valueBatteryPercentage;
    private TextView labelVoltageBattery, valueVoltageBattery;
    private TextView labelVoltageUsb, valueVoltageUsb;
    private TextView labelUsbDetected, valueUsbDetected;
    private TextView labelCurrentDataStream, valueCurrentDataStream;

    private TroubleshootDeviceProcess troubleshootDeviceProcess;
    private boolean isHandlingTroubleshootDeviceProcess = false;

    public BluetoothDevice device;
    public boolean isConnected;

    private DebugCorner debugCorner;


    private void onClickButtonBluetooth() {
        this.isHandlingTroubleshootDeviceProcess = true;
        troubleshootDeviceProcess.initialize();
        // TODO start troubleshootDeviceProcess
    }

    public void writeDataToDevice(byte[] bytes) {
        if (bytes != null) {
            boolean wrote = this.uartConnection.writeBytes(bytes);
            if (!wrote) {
                Log.w(Constants.LOG_TAG, String.format("Value: '%s' was not written", new String(bytes)));
            }
        }
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_troubleshoot_device_show_with_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.textViewTitle = findViewById(R.id.textViewTitle);
        this.buttonBluetooth = findViewById(R.id.buttonBluetooth);
        this.tableLayoutTroubleshootingBle = findViewById(R.id.tableLayoutTroubleshootingBle);
        this.labelFirmwareVersion = findViewById(R.id.labelFirmwareVersion);
        this.valueFirmwareVersion = findViewById(R.id.valueFirmwareVersion);
        this.labelPowerLedState = findViewById(R.id.labelPowerLedState);
        this.valuePowerLedState = findViewById(R.id.valuePowerLedState);
        this.labelBatteryPercentage = findViewById(R.id.labelBatteryPercentage);
        this.valueBatteryPercentage = findViewById(R.id.valueBatteryPercentage);
        this.labelVoltageBattery = findViewById(R.id.labelVoltageBattery);
        this.valueVoltageBattery = findViewById(R.id.valueVoltageBattery);
        this.labelVoltageUsb = findViewById(R.id.labelVoltageUsb);
        this.valueVoltageUsb = findViewById(R.id.valueVoltageUsb);
        this.labelUsbDetected = findViewById(R.id.labelUsbDetected);
        this.valueUsbDetected = findViewById(R.id.valueUsbDetected);
        this.labelCurrentDataStream = findViewById(R.id.labelCurrentDataStream);
        this.valueCurrentDataStream = findViewById(R.id.valueCurrentDataStream);

        this.device =  getIntent().getParcelableExtra(EXTRA_DEVICE);
        this.isConnected = getIntent().getBooleanExtra(EXTRA_IS_CONNECTED, false);
        this.troubleshootDeviceProcess = new TroubleshootDeviceProcess(this);

        findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonBluetooth();
            }
        });
        // NOTE: only display the button when we can confirm that it is connected
        buttonBluetooth.setVisibility(View.GONE);

        // NOTE: UART settings are the same across all devices
        this.uartConnection = new UARTConnection(getApplicationContext(), device, Constants.DEFAULT_UART_SETTINGS, this);
        uartConnection.addRxDataListener(this);

        this.debugCorner = new DebugCorner(this);
    }


    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return null;
    }


    @Override
    public void onConnected() {
        // TODO note when we need to wait for this? (isConnected will be false)
        Log.i(Constants.LOG_TAG,"SettingsTroubleshootDeviceShow.onConnected");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTitle.setText(String.format("Troubleshooting: %s", device.getName()));
                buttonBluetooth.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onDisconnected() {
        Log.w(Constants.LOG_TAG,"SettingsTroubleshootDeviceShow.onDisconnected detected; exiting activity");
        finish();
    }


    @Override
    public void onRXData(byte[] newData) {
        String msg = new String(newData).trim();
        Log.i(Constants.LOG_TAG,"SettingsTroubleshootDeviceShow.onRXData: " + msg);

        // only send relevant messages to the process (i.e. begins with dollar sign)
        if (isHandlingTroubleshootDeviceProcess && msg.charAt(0) == '$') {
            troubleshootDeviceProcess.updateWithBleResponse(msg);
        }
        // always display the current data stream
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                valueCurrentDataStream.setText(msg);
            }
        });
    }

    @Override
    public void onTroubleshootDeviceProcessSendCommand(String cmd) {
        byte[] msg = cmd.getBytes();
        writeDataToDevice(msg);
    }

    @Override
    public void onTroubleshootDeviceProcessValueReceived(TroubleshootDeviceProcess.ProcessState state, String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case firmwareVersion:
                        valueFirmwareVersion.setText(value);
                        break;
                    case powerLedState:
                        valuePowerLedState.setText(value);
                        break;
                    case batteryPercentage:
                        valueBatteryPercentage.setText(value);
                        break;
                    case voltageBattery:
                        valueVoltageBattery.setText(value);
                        break;
                    case voltageUsb:
                        valueVoltageUsb.setText(value);
                        break;
                    case usbDetected:
                        valueUsbDetected.setText(value);
                        break;
                    default:
                        Log.e(Constants.LOG_TAG, "onTroubleshootDeviceProcessValueReceived with unknown state " + state);
                }
            }
        });
    }

    @Override
    public void onTroubleshootDeviceProcessTimeout() {
        this.isHandlingTroubleshootDeviceProcess = false;
        Log.i(Constants.LOG_TAG, "onTroubleshootDeviceProcessTimeout");
    }

    @Override
    public void onTroubleshootDeviceProcessCompleted() {
        this.isHandlingTroubleshootDeviceProcess = false;
        Log.i(Constants.LOG_TAG, "onTroubleshootDeviceProcessCompleted");
    }

}
