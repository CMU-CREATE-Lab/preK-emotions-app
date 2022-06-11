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
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.StudentEditFragment;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;

public abstract class StudentUpdateModelAbstractActivity extends AbstractActivity {

    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";

    private boolean isHandlingPicture = false;

    // models/objects
    private String classroomName;
    private Student student;
    private File newStudentPicture;

    // views
    private StudentEditFragment headerFragment;
    private ImageButton imageButtonStudentPhoto;
    private TextView textButtonRetakePhoto;
    private EditText editTextStudentName;
    private EditText editTextStudentNotes;


    private void populateViews() {
        // update header text
        headerFragment.textViewBack.setText((classroomName != null && !classroomName.isEmpty()) ? String.format("Back to %s", classroomName) : "Back");
        headerFragment.textViewTitle.setText(getHeaderTitle());

        // update views with student info
        ((EditText)findViewById(R.id.editTextStudentName)).setText(student.getName());
        ((EditText)findViewById(R.id.editTextStudentNotes)).setText(student.getNotes());

        // get image
        if (student.getPictureFileUuid() != null) {
            final Context appContext = getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(this, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    Util.setImageViewWithDbFile(appContext, (ImageButton) findViewById(R.id.imageButtonStudentPhoto), dbFile);
                }
            });
        } else {
            ((ImageButton) findViewById(R.id.imageButtonStudentPhoto)).setImageResource(R.drawable.ic_placeholder);
        }
    }


    private void updatePicture(File picture) {
        if (newStudentPicture != null) {
            picture.delete();
        }
        this.newStudentPicture = picture;
        imageButtonStudentPhoto.setImageBitmap(BitmapFactory.decodeFile(newStudentPicture.getPath()));
    }


    private void startCameraActivityForResult() {
        Intent cameraIntent = new Intent(StudentUpdateModelAbstractActivity.this, CameraActivity.class);
        String filename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
        cameraIntent.putExtra(CameraActivity.EXTRA_PICTURE_FILENAME, filename);
        startActivityForResult(cameraIntent, CameraActivity.REQUEST_CODE);
    }


    public void finishEditActivity(boolean isSaving) {
        if (isHandlingPicture) {
            return;
        }
        this.isHandlingPicture = true;
        if (isSaving) {
            String newName = editTextStudentName.getText().toString();
            String newNotes = editTextStudentNotes.getText().toString();
            if (!newName.equals(student.getName()) || !newNotes.equals(student.getNotes()) || newStudentPicture != null) {
                student.setName(newName);
                student.setNotes(newNotes);
                // TODO delete old image? (requires another DB query so I'm guessing not, for now)
                updateModel(student, newStudentPicture);
            } else {
                // nothing to update/save
                finish();
            }
        } else {
            if (newStudentPicture != null) {
                newStudentPicture.delete();
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // construct models/objects
        this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
        this.student = (Student) getIntent().getSerializableExtra(EXTRA_STUDENT);
        if (student != null) {
            Log.d(Constants.LOG_TAG, "StudentEditActivity.onCreate got object Student: " + String.format("Name=%s, uuid=%s, notes=%s", student.getName(), student.getUuid(), student.getNotes()));
        }

        // construct views
        this.headerFragment = (StudentEditFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
        this.headerFragment.setHeaderTransparency(true);
        this.imageButtonStudentPhoto = findViewById(R.id.imageButtonStudentPhoto);
        this.textButtonRetakePhoto = findViewById(R.id.textButtonRetakePhoto);
        this.editTextStudentName = findViewById(R.id.editTextStudentName);
        this.editTextStudentNotes = findViewById(R.id.editTextStudentNotes);

        populateViews();

        // avoid EditText focus when activity starts: https://stackoverflow.com/questions/4149415/onscreen-keyboard-opens-automatically-when-activity-starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // demo click listener on textview
        headerFragment.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditActivity(false);
            }
        });
        headerFragment.imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditActivity(false);
            }
        });
        headerFragment.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditActivity(true);
            }
        });
        imageButtonStudentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.LOG_TAG, "onClick imageButtonStudentPhoto");
                startCameraActivityForResult();
            }
        });
        textButtonRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraActivityForResult();
            }
        });
    }


    @Override
    protected void onDestroy() {
        // make sure to handle extra actions performed in finishEditActivity()
        if (!isHandlingPicture && newStudentPicture != null) {
            newStudentPicture.delete();
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CameraActivity.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(Constants.LOG_TAG, "got RESULT_OK from CameraActivity, updating picture");
                    File picture = (File) data.getExtras().getSerializable(CameraActivity.EXTRA_RESULT_PICTURE);
                    updatePicture(picture);
                } else if (resultCode == CameraActivity.RESULT_START_OVER) {
                    startCameraActivityForResult();
                }
                break;
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_students_edit;
    }


    public abstract void updateModel(final Student student, final File newStudentPicture);


    public abstract String getHeaderTitle();

}