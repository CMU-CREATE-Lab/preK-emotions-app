package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.Observer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.ClassroomIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.DebugCorner;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ChooseClassroomActivity extends StudentSectionActivityWithHeader {

    private DebugCorner debugCorner;

    private final ClassroomIndexAdapter.ClickListener listener = new ClassroomIndexAdapter.ClickListener() {
        @Override
        public void onClick(Classroom classroom) {
            Log.d(Constants.LOG_TAG, "onClick classroom = " + classroom.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.classroomUuid = classroom.getUuid();
            // send to next activity
            Intent chooseStudentActivity = new Intent(ChooseClassroomActivity.this, ChooseStudentActivity.class);
            startActivity(chooseStudentActivity);
        }
    };

    private static final int REQUEST_ENABLE_BT = 1;


    private void checkAndRequestBle() {
        // Initializes Bluetooth adapter.
        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable List<Classroom> classrooms) {
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
                classroomsGridView.setAdapter(new ClassroomIndexAdapter(classrooms, listener));
            }
        });

        this.debugCorner = new DebugCorner(this);
        checkAndRequestBle();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_classroom;
    }

}
