package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.ClassroomIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseClassroomActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ClassroomIndexActivity extends TeacherSectionActivityWithHeaderAndDrawer implements ClassroomIndexAdapter.ClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable List<Classroom> classrooms) {
                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
                classroomsGridView.setAdapter(new ClassroomIndexAdapter(ClassroomIndexActivity.this, classrooms, ClassroomIndexActivity.this));
            }
        });

        FloatingActionButton fabNewClassroom = findViewById(R.id.fabNewClassroom);
        fabNewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("activity", "fabNewClassroom.onClick");
                if (!activityShouldHandleOnClickEvents()) {
                    Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                    return;
                }
            }
        });
    }


    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return DrawerTeacherMainFragment.Section.CLASSES;
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_classrooms_index;
    }


    @Override
    public void onClick(Classroom classroom) {
        Log.i(Constants.LOG_TAG, String.format("onClick classroom name=%s with uuid=%s", classroom.getName(), classroom.getUuid()));

        // send to next activity
        Intent classroomShowActivity = new Intent(ClassroomIndexActivity.this, ClassroomShowActivity.class);
        classroomShowActivity.putExtra(ClassroomShowActivity.EXTRA_CLASSROOM_UUID, classroom.getUuid());
        classroomShowActivity.putExtra(ClassroomShowActivity.EXTRA_CLASSROOM_NAME, classroom.getName());
        startActivity(classroomShowActivity);
    }

}
