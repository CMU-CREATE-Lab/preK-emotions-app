package org.cmucreatelab.android.flutterprek.activities.coping_skill_flower;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BleFlower;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerStateHandler implements BleFlower.NotificationCallback, FlowerBreathTracker.Listener {

    enum State {
        WAIT_FOR_BUTTON,
        BREATHING,
        FINISHED
    }

    private static final int REQUEST_ENABLE_BT = 1;
    private static final boolean SHOW_DEBUG_WINDOW = false;

    private final BluetoothAdapter bluetoothAdapter;
    private final FlowerCopingSkillActivity activity;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!flowerDiscovered) {
                BluetoothDevice device = result.getDevice();
                if (device.getName() != null && device.getName().startsWith("FLOWER-")) {
                    Log.d(Constants.LOG_TAG, "onLeScan found Flower with name=" + device.getName());
                    flowerDiscovered = true;
                    GlobalHandler.getInstance(activity.getApplicationContext()).startConnection(device);
                    updateFlower(GlobalHandler.getInstance(activity.getApplicationContext()).bleFlower);
                    stopScan();
                    updateDebugWindow();
//                    // TODO simulates receiving a notification (HW flower broken for me at the moment, sadface)
//                    onReceivedData("1","","");
                } else {
                    Log.d(Constants.LOG_TAG, "onLeScan result: " + device.getName());
                }
            }
            super.onScanResult(callbackType, result);
        }
    };
    private final FlowerBreathTracker breathTracker;
    private boolean isScanning = false;
    private boolean flowerDiscovered = false;
    private boolean isPressingButton = false;
    private BleFlower bleFlower;
    // TODO make this save more than one
    private String lastNotification = "";
//    // TODO this will track progress within a state
//    private final Handler handler = new Handler();
//    private Runnable currentCallback;
    private State currentState = State.WAIT_FOR_BUTTON;


    private void updateDebugWindow() {
        if (SHOW_DEBUG_WINDOW) {
            String display;
            if (flowerDiscovered) {
                display = bleFlower.getDeviceName();
                display = display+"\n"+lastNotification;
            } else {
                display = isScanning ? "Looking for Flower..." : "Inactive";
            }
            TextView textView = activity.findViewById(R.id.textViewDebug);
            textView.setText(display);
        }
    }


    public FlowerStateHandler(FlowerCopingSkillActivity activity) {
        this.activity = activity;
        this.breathTracker = new FlowerBreathTracker(activity, this);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // debug window
        TextView textView = activity.findViewById(R.id.textViewDebug);
        if (!SHOW_DEBUG_WINDOW) {
            textView.setVisibility(View.INVISIBLE);
        }
//        } else {
//            textView.setText("Flower\nCoping\rSkill\n1\n2\n3\n4\n5\n6");
//        }
    }


    private void updateFlower(BleFlower bleFlower) {
        this.bleFlower = bleFlower;
        this.bleFlower.notificationCallback = this;
    }


    private void changeState(State newState) {
        currentState = newState;
        if (newState == State.WAIT_FOR_BUTTON) {
            activity.displayHoldFlowerInstructions();
            breathTracker.resetTracker();
        } else if (newState == State.BREATHING) {
            activity.displayBreatheIn();
            breathTracker.startTracker();
        } else if (newState == State.FINISHED) {
            breathTracker.resetTracker();
            activity.displayOverlay();
        }
    }


    public void stopScan() {
        isScanning = false;
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }


    public void startScan() {
        isScanning = true;
        // TODO ScanFilter https://developer.android.com/reference/android/bluetooth/le/ScanFilter
        bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        // TODO timeout?
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
//                    Log.d(Constants.LOG_TAG,"Stopped LeScan");
//                }
//            }, 2000);
    }


    public void lookForFlower() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
        this.flowerDiscovered = globalHandler.isFlowerConnected();

        if (flowerDiscovered) {
            updateFlower(globalHandler.bleFlower);
        } else {
            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                Log.d(Constants.LOG_TAG,"Starting LeScan...");
                startScan();
            }
        }
        updateDebugWindow();
    }


    public void initializeState() {
        changeState(State.WAIT_FOR_BUTTON);
    }


    @Override
    public void onFinishedBreathing() {
        changeState(State.FINISHED);
    }


    @Override
    public void onReceivedData(String arg1, String arg2, String arg3) {
        boolean newValue = arg1.equals("1");

        // only perform actions on a changed state
        if (isPressingButton != newValue) {
            isPressingButton = newValue;
            if (currentState != State.FINISHED) {
                if (isPressingButton) {
                    changeState(State.BREATHING);
                } else {
                    changeState(State.WAIT_FOR_BUTTON);
                }
            }
        }

        if (SHOW_DEBUG_WINDOW) {
            String reformedData = arg1+","+arg2+","+arg3;
            // TODO add reformedData to queue
            lastNotification = reformedData;
            updateDebugWindow();
        }
    }

}
