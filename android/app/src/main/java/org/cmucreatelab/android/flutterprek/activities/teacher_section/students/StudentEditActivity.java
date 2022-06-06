package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.StudentEditFragment;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

public class StudentEditActivity extends AbstractActivity {

    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";

    // models/objects
    private String classroomName;
    private Student student;

    // views
    private StudentEditFragment headerFragment;
    private ImageButton imageButtonStudentPhoto;


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

        // update header text
        headerFragment.textViewBack.setText((classroomName != null && !classroomName.isEmpty()) ? String.format("Back to %s", classroomName) : "Back");

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
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_students_edit;
    }

}
