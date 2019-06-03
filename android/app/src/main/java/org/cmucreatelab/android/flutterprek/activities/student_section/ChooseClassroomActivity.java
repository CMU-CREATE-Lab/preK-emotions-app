package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.Observer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ChooseClassroomActivity extends StudentSectionActivityWithHeader {

    private DebugCorner debugCorner;

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

        AppDatabase.getInstance(this).classroomDAO().getAllClassroomsWithCustomizations().observe(this, new Observer<List<ClassroomWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<ClassroomWithCustomizations> classrooms) {
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
                classroomsGridView.setAdapter(new ClassroomWithCustomizationsIndexAdapter(classrooms, listener));
            }
        });

        this.debugCorner = new DebugCorner(this);
        checkAndRequestBle();
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

}
