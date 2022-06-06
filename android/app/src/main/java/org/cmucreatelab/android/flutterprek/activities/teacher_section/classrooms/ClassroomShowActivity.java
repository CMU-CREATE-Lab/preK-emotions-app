package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

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
import org.cmucreatelab.android.flutterprek.activities.DebugCorner;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithHeader;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.LoginActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentEditActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ClassroomShowActivity extends TeacherSectionActivityWithHeaderAndDrawer {

    public static final String EXTRA_CLASSROOM_UUID = "classroom_uuid";
    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";

    private String classroomName;

    private final StudentWithCustomizationsIndexAdapter.ClickListener listener = new StudentWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(StudentWithCustomizations studentWithCustomizations) {
            final Student student = studentWithCustomizations.student;
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());

            Intent studentEditActivity = new Intent(ClassroomShowActivity.this, StudentEditActivity.class);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_STUDENT, studentWithCustomizations.student);
            studentEditActivity.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
            startActivity(studentEditActivity);

            // TODO actions
//            // track selection with GlobalHandler
//            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
//            globalHandler.studentSectionNavigationHandler.studentUuid = student.getUuid();
//            globalHandler.studentSectionNavigationHandler.imageUuid = student.getPictureFileUuid();
//
//            // start new session for student
//            globalHandler.startNewSession(studentWithCustomizations);
//
//            // send to next activity
//            //Intent chooseEmotionActivity = new Intent(ChooseStudentActivity.this, ChooseEmotionActivity.class);
//            Intent chooseEmotionActivity = globalHandler.getSessionTracker().getNextIntent(ClassroomShowActivity.this);
//            startActivity(chooseEmotionActivity);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO bundle classroomUuid
//        String classroomUuid = GlobalHandler.getInstance(this).studentSectionNavigationHandler.classroomUuid;

//        LiveData<List<StudentWithCustomizations>> liveData;
//        if (classroomUuid.isEmpty()) {
//            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizations();
//        } else {
//            liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
//        }

        String classroomUuid = getIntent().getStringExtra(EXTRA_CLASSROOM_UUID);
        this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);

        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                studentsGridView.setAdapter(new StudentWithCustomizationsIndexAdapter(ClassroomShowActivity.this, students, listener));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_classrooms_show;
    }


    @Override
    public void onClickImageStudent() {
        if (!activityShouldHandleOnClickEvents()) {
            Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
            return;
        }
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        // TODO create/implement sections for classroom show
        return null;
    }


//    @Override
//    public void updateImageStudent(AppCompatActivity activity) {
//        ((ImageView)findViewById(R.id.imageStudent)).setBackgroundResource(R.drawable.ic_mindfulnest_header_student_section);
//    }
//
//
//    @Override
//    public boolean isInfoIconVisible() {
//        return true;
//    }

}
