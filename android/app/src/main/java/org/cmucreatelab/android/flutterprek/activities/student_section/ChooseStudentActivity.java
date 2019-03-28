package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.DebugCorner;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ChooseStudentActivity extends StudentSectionActivityWithHeader {

    private DebugCorner debugCorner;

    private final StudentIndexAdapter.ClickListener listener = new StudentIndexAdapter.ClickListener() {
        @Override
        public void onClick(Student student) {
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());
            // track selection with GlobalHandler
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.studentSectionNavigationHandler.studentUuid = student.getUuid();
            globalHandler.studentSectionNavigationHandler.imageUuid = student.getPictureFileUuid();

            // start new session for student
            globalHandler.startNewSession(student);

            // send to next activity
            //Intent chooseEmotionActivity = new Intent(ChooseStudentActivity.this, ChooseEmotionActivity.class);
            Intent chooseEmotionActivity = globalHandler.getSessionTracker().getNextIntent(ChooseStudentActivity.this);
            startActivity(chooseEmotionActivity);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String classroomUuid = GlobalHandler.getInstance(this).studentSectionNavigationHandler.classroomUuid;
        LiveData<List<Student>> liveData;
        if (classroomUuid.isEmpty()) {
            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudents();
        } else {
            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsFromClassroom(classroomUuid);
        }

        liveData.observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentIndexAdapter(ChooseStudentActivity.this, students, listener));
            }
        });

        this.debugCorner = new DebugCorner(this);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_student;
    }


    @Override
    public void onClickImageStudent() {
        // TODO go to teacher section (does nothing for now)

    }


    @Override
    public void updateImageStudent(AppCompatActivity activity) {
        ((ImageView)findViewById(R.id.imageStudent)).setBackgroundResource(R.drawable.ic_mindfulnest_header_student_section);
    }

}
