package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherClassroomFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentAddActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentEditActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ClassroomShowStudentsActivity extends ManageClassroomActivityWithHeaderAndDrawer {

    private static final String templateNameForAddStudent = "";

    private String classroomUuid;
    private String classroomName;

    private final StudentWithCustomizationsIndexAdapter.ClickListener listener = new StudentWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(StudentWithCustomizations studentWithCustomizations) {
            final Student student = studentWithCustomizations.student;
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());

            Intent studentEditActivity = new Intent(ClassroomShowStudentsActivity.this, StudentEditActivity.class);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_STUDENT, studentWithCustomizations.student);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
            startActivity(studentEditActivity);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentWithCustomizationsIndexAdapter(ClassroomShowStudentsActivity.this, students, listener));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.classroomUuid = getClassroom().getUuid();
        this.classroomName = getClassroom().getName();

        findViewById(R.id.fabNewStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentAddActivityIntent = new Intent(ClassroomShowStudentsActivity.this, StudentAddActivity.class);
                studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_STUDENT, new Student(templateNameForAddStudent, classroomUuid));
                studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
                startActivity(studentAddActivityIntent);
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_classrooms_show_students;
    }


    @Override
    public DrawerTeacherClassroomFragment.Section getSectionForDrawer() {
        return DrawerTeacherClassroomFragment.Section.STUDENTS;
    }

}
