package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.ModelUpdateHeaderFragment;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;

public abstract class StudentUpdateAbstractActivity extends AbstractActivity {

    public enum ModelAction {
        UPDATE,
        DELETE,
        CANCEL
    }

    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";

    private static final int requestCodeCameraPermission = 15;

    private boolean isHandlingPicture = false;

    // models/objects
    private String classroomName;
    private Student student;
    private File newStudentPicture;

    // views
    private ModelUpdateHeaderFragment headerFragment;
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
            newStudentPicture.delete();
        }
        Util.defaultReformatImageFile(picture);
        this.newStudentPicture = picture;
        imageButtonStudentPhoto.setImageBitmap(BitmapFactory.decodeFile(newStudentPicture.getPath()));
    }


    private void startCameraActivityForResult() {
        GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;

        Intent cameraIntent = new Intent(StudentUpdateAbstractActivity.this, CameraActivity.class);
        String filename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
        cameraIntent.putExtra(CameraActivity.EXTRA_PICTURE_FILENAME, filename);
        startActivityForResult(cameraIntent, CameraActivity.REQUEST_CODE);
    }


    private void requestCameraActivityForResult() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, requestCodeCameraPermission);
        } else {
            startCameraActivityForResult();
        }
    }


    public void finishEditActivity(ModelAction modelAction) {
        if (isHandlingPicture) {
            return;
        }
        this.isHandlingPicture = true;

        switch (modelAction) {
            case UPDATE:
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
                break;

            case DELETE:
                if (newStudentPicture != null) {
                    newStudentPicture.delete();
                }
                Log.d(Constants.LOG_TAG, "performing DB delete");
                new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.DELETE, student, newStudentPicture, new UpdateStudentModelAsyncTask.PostExecute() {
                    @Override
                    public void onPostExecute(Boolean modelSaved) {
                        if (!modelSaved) {
                            Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }).execute();
                break;

            case CANCEL:
            default:
                if (newStudentPicture != null) {
                    newStudentPicture.delete();
                }
                finish();
                break;
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
        this.headerFragment = (ModelUpdateHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
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
                finishEditActivity(ModelAction.CANCEL);
            }
        });
        headerFragment.imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditActivity(ModelAction.CANCEL);
            }
        });
        headerFragment.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditActivity(ModelAction.UPDATE);
            }
        });
        if (isDisplayDeleteButton()) {
            headerFragment.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentUpdateAbstractActivity.this);
                    builder.setMessage(R.string.alert_message_delete_student);
                    builder.setTitle(R.string.alert_title_delete_student);
                    builder.setPositiveButton(R.string.alert_option_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishEditActivity(ModelAction.DELETE);
                        }
                    });
                    builder.setNegativeButton(R.string.alert_option_cancel, null);
                    builder.create().show();
                }
            });
        } else {
            headerFragment.imageButtonDelete.setVisibility(View.GONE);
        }
        imageButtonStudentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.LOG_TAG, "onClick imageButtonStudentPhoto");
                requestCameraActivityForResult();
            }
        });
        textButtonRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraActivityForResult();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case requestCodeCameraPermission:
                for (int i=0; i<permissions.length; i++) {
                    String permission = permissions[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (i < grantResults.length) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                startCameraActivityForResult();
                            } else {
                                String alertMessage = getString(R.string.alert_message_permissions_camera);
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                                builder.setMessage(alertMessage);
                                builder.setTitle(R.string.alert_title_permissions_camera);
                                builder.create().show();
                            }
                            break;
                        } else {
                            Log.e(Constants.LOG_TAG, "onRequestPermissionsResult out of index for camera permission; ignoring result.");
                        }
                    }
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CameraActivity.REQUEST_CODE:
                GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = false;

                if (resultCode == Activity.RESULT_OK) {
                    Log.d(Constants.LOG_TAG, "got RESULT_OK from CameraActivity, updating picture");
                    File picture = (File) data.getExtras().getSerializable(CameraActivity.EXTRA_RESULT_PICTURE);
                    updatePicture(picture);
                } else if (resultCode == CameraActivity.RESULT_START_OVER) {
                    requestCameraActivityForResult();
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


    public abstract boolean isDisplayDeleteButton();

}