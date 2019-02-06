package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

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
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.studentUuid = student.getUuid();
            // send to next activity
            Intent chooseEmotionActivity = new Intent(ChooseStudentActivity.this, ChooseEmotionActivity.class);
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

}
