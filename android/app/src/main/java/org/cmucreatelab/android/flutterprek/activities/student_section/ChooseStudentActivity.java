package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ChooseStudentActivity extends StudentSectionActivityWithHeader {

    public static final String CLASSROOM_KEY = "classroom";

    private final StudentIndexAdapter.ClickListener listener = new StudentIndexAdapter.ClickListener() {
        @Override
        public void onClick(Student student) {
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());
            // send to next activity
            // TODO track selection with GlobalHandler?
            Intent chooseEmotionActivity = new Intent(ChooseStudentActivity.this, ChooseEmotionActivity.class);
            //chooseStudentActivity.putExtra(ChooseStudentActivity.CLASSROOM_KEY, classroom);
            startActivity(chooseEmotionActivity);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Classroom classroom = (Classroom) getIntent().getSerializableExtra(CLASSROOM_KEY);
        LiveData<List<Student>> liveData;
        if (classroom == null) {
            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudents();
        } else {
            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsFromClassroom(classroom.getUuid());
        }

        liveData.observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentIndexAdapter(getApplicationContext(), students, listener));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_student;
    }

}
