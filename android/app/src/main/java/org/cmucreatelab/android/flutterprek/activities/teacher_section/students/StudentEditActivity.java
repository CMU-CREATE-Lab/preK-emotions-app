package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.StudentEditFragment;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;

public class StudentEditActivity extends AbstractActivity {

    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";

    private static final String headerTitleAddStudent = "Add Student";
    private static final String headerTitleEditStudent = "Edit Student";

    // models/objects
    private String classroomName;
    private Student student;
    private File newStudentPicture;

    // views
    private StudentEditFragment headerFragment;
    private ImageButton imageButtonStudentPhoto;


    private void populateViews() {
        // update header text
        headerFragment.textViewBack.setText((classroomName != null && !classroomName.isEmpty()) ? String.format("Back to %s", classroomName) : "Back");
        headerFragment.textViewTitle.setText(headerTitleEditStudent);

        // update views with student info
        ((EditText)findViewById(R.id.editTextStudentName)).setText(student.getName());
        ((EditText)findViewById(R.id.editTextStudentNotes)).setText(student.getNotes());

        // get image
        if (student.getPictureFileUuid() != null) {
            final Context appContext = getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(this, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    // TODO check if file type is asset
                    Util.setImageViewWithAsset(appContext, (ImageButton) findViewById(R.id.imageButtonStudentPhoto), dbFile.getFilePath());
                }
            });
        } else {
            ((ImageButton) findViewById(R.id.imageButtonStudentPhoto)).setImageResource(R.drawable.ic_placeholder);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // construct models/objects
        this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
        this.student = (Student) getIntent().getSerializableExtra(EXTRA_STUDENT);
        Log.d(Constants.LOG_TAG, "StudentEditActivity.onCreate got object Student: " + String.format("Name=%s, uuid=%s, notes=%s", student.getName(), student.getUuid(), student.getNotes()));

        // construct views
        this.headerFragment = (StudentEditFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
        this.headerFragment.setHeaderTransparency(true);
        this.imageButtonStudentPhoto = findViewById(R.id.imageButtonStudentPhoto);

        populateViews();

        // avoid EditText focus when activity starts: https://stackoverflow.com/questions/4149415/onscreen-keyboard-opens-automatically-when-activity-starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // demo click listener on textview
        headerFragment.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO method for handling return back (save vs. cancel)
                finish();
            }
        });
        headerFragment.imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO method for handling return back (save vs. cancel)
                finish();
            }
        });
        headerFragment.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO method for handling return back (save vs. cancel)
                finish();
            }
        });
        imageButtonStudentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.LOG_TAG, "onClick imageButtonStudentPhoto");

                Intent cameraIntent = new Intent(StudentEditActivity.this, CameraActivity.class);
                String filename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
                cameraIntent.putExtra(CameraActivity.EXTRA_PICTURE_FILENAME, filename);
                startActivityForResult(cameraIntent, CameraActivity.REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CameraActivity.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(Constants.LOG_TAG, "got RESULT_OK from CameraActivity, updating picture");
                    // TODO handle delete file if left unused (or overwrites)
                    this.newStudentPicture = (File) data.getExtras().getSerializable(CameraActivity.EXTRA_RESULT_PICTURE);
                    imageButtonStudentPhoto.setImageBitmap(BitmapFactory.decodeFile(newStudentPicture.getPath()));
                }
                break;
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_students_edit;
    }

}
