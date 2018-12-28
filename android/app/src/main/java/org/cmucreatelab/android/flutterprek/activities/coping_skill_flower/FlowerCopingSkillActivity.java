package org.cmucreatelab.android.flutterprek.activities.coping_skill_flower;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerCopingSkillActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    // NOTE: construct instance of callback or else stopScan() does nothing
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!flowerDiscovered) {
                BluetoothDevice device = result.getDevice();
                if (device.getName() != null && device.getName().startsWith("FLOWER-")) {
                    Log.d(Constants.LOG_TAG, "onLeScan found Flower with name=" + device.getName());
                    flowerDiscovered = true;
                    GlobalHandler.getInstance(getApplicationContext()).startConnection(device);
                    TextView textView = findViewById(R.id.textViewDebug);
                    textView.setText(device.getName());
                    // stop scanning
                    bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                } else {
                    Log.d(Constants.LOG_TAG, "onLeScan result: " + device.getName());
                }
            }
            super.onScanResult(callbackType, result);
        }
    };
    private boolean flowerDiscovered = false;
    private FlowerCopingSkillProcess flowerCopingSkillProcess;
    private FlowerCopingSkillStep1Timer step1Timer;
    private static final boolean SHOW_DEBUG_WINDOW = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_coping_skill);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // close
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.LOG_TAG, "clicked buttonClose; now finishing activity");
                finish();
            }
        });

        // TODO need to detect when nav bar is forced to display, so it can hide again after a few seconds
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        flowerCopingSkillProcess = new FlowerCopingSkillProcess(this);
        step1Timer = new FlowerCopingSkillStep1Timer(flowerCopingSkillProcess);

        // debug window
        TextView textView = findViewById(R.id.textViewDebug);
        if (!SHOW_DEBUG_WINDOW) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setText("Flower\nCoping\rSkill\n1\n2\n3\n4\n5\n6");
        }

        // test set step
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
//        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_2_SMELL);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_3_BLOW);
//            }
//        }, 2000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (bluetoothAdapter != null) {
            Log.d(Constants.LOG_TAG,"Stopping LeScan...");
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        this.flowerDiscovered = globalHandler.isFlowerConnected();

        if (flowerDiscovered) {
            // show device name in debug
            TextView textView = findViewById(R.id.textViewDebug);
            textView.setText(globalHandler.bleFlower.getDeviceName());
        } else {
            lookForFlower();
        }
    }


    public void lookForFlower() {
        TextView textView = findViewById(R.id.textViewDebug);
        textView.setText("Looking for Flower...");

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.d(Constants.LOG_TAG,"Starting LeScan...");
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
    }
}
