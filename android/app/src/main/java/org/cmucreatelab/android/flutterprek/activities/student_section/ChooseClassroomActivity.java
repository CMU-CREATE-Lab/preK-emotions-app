package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.DebugCorner;
import org.cmucreatelab.android.flutterprek.activities.adapters.ClassroomWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.LoginActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.ClassroomWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class ChooseClassroomActivity extends StudentSectionActivityWithHeader {

    private static final int REQUEST_ENABLE_BT = 1;

    private DebugCorner debugCorner;

    private final int requestCodeLocation = 1;
    private final int requestCodeBluetoothConnect = 2;
    private final int requestCodeBluetoothScan = 4;

    private final int[] requestPermissions = {requestCodeLocation, requestCodeBluetoothConnect, requestCodeBluetoothScan};

    private int requestPermissionsPtr = 0;

    private final ClassroomWithCustomizationsIndexAdapter.ClickListener listener = new ClassroomWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(ClassroomWithCustomizations classroomWithCustomizations) {
            final Classroom classroom = classroomWithCustomizations.classroom;
            Log.d(Constants.LOG_TAG, "onClick classroom = " + classroom.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.classroomUuid = classroom.getUuid();
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.classroomSessionMode = classroomWithCustomizations.getSessionMode();
            // send to next activity
            Intent chooseStudentActivity = new Intent(ChooseClassroomActivity.this, ChooseStudentActivity.class);
            startActivity(chooseStudentActivity);
        }
    };


    private void checkAndRequestBle() {
        // Initializes Bluetooth adapter.
        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, requestCodeBluetoothConnect);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, requestCodeBluetoothScan);
            }
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    // Android 11+ requires Location permissions at runtime
    private void checkAndRequestLocationForBluetooth() {
//        // not needed for Android 12 or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            return;
//        }
//        //String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
//        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeLocation);
        }

//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), locationPermission);
//        if (result == PERMISSION_GRANTED) {
//            Log.d(Constants.LOG_TAG, "location permission granted");
//        } else {
//            Log.d(Constants.LOG_TAG, "location permission NOT granted");
//            // https://developer.android.com/training/permissions/requesting#manage-request-code-yourself
//            requestPermissions(new String[] {locationPermission}, requestCodeLocation);
//        }
    }


    private void displayLocationPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Location required for Bluetooth")
                .setMessage("Location permissions must be enabled in order for Bluetooth devices to work with this app.")
                .setNeutralButton("OK", null)
                .show();
    }


    private synchronized void handleRequestPermissions() {
        boolean requestingPermission = false;
        int length = this.requestPermissions.length;
        if (requestPermissionsPtr < length) {
            int requestCode = requestPermissions[requestPermissionsPtr++];
            switch (requestCode) {
                case requestCodeLocation:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        break;
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        requestingPermission = true;
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeLocation);
                    }
                    break;
                case requestCodeBluetoothConnect:
                case requestCodeBluetoothScan:
                    BluetoothAdapter bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                            requestingPermission = true;
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, requestCodeBluetoothConnect);
                        }
                        if (!requestingPermission && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                            requestingPermission = true;
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, requestCodeBluetoothScan);
                        }
                    }

                    // Ensures Bluetooth is available on the device and it is enabled. If not,
                    // displays a dialog requesting user permission to enable Bluetooth.
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "failed to identify request code.");
            }
            // request next permission (if not already requesting)
            if (!requestingPermission) handleRequestPermissions();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase.getInstance(this).classroomDAO().getAllClassroomsWithCustomizations().observe(this, new Observer<List<ClassroomWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<ClassroomWithCustomizations> classrooms) {
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
                classroomsGridView.setAdapter(new ClassroomWithCustomizationsIndexAdapter(classrooms, listener));
            }
        });

        this.debugCorner = new DebugCorner(this);
//        checkAndRequestBle();
//        checkAndRequestLocationForBluetooth();
        handleRequestPermissions();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestCodeLocation:
            case requestCodeBluetoothConnect:
            case requestCodeBluetoothScan:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(Constants.LOG_TAG, "permissions result: PERMISSION_GRANTED");
                    break;
                }
            default:
                Log.d(Constants.LOG_TAG, "permissions result: NO RESULT");
                // dialog explaining you need Location permissions for BLE
                displayLocationPermissionDialog();
        }
        handleRequestPermissions();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_classroom;
    }


    @Override
    public void onClickImageStudent() {
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void updateImageStudent(AppCompatActivity activity) {
        ((ImageView)findViewById(R.id.imageStudent)).setBackgroundResource(R.drawable.ic_mindfulnest_header_student_section);
    }


    @Override
    public boolean isInfoIconVisible() {
        // TODO #112 hide unused for now
        //return true;
        return false;
    }


    @Override
    public boolean activityUsesDelayedOnClickHandler() {
        return false;
    }

}
