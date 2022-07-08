package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.ClassroomIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ClassroomIndexActivity extends TeacherSectionActivityWithHeaderAndDrawer implements ClassroomIndexAdapter.ClickListener {


    private void displayClassroomNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_classroom_name, null);
        builder.setView(view)
                .setPositiveButton(R.string.alert_option_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String classroomName = ((EditText) view.findViewById(R.id.editTextClassroomName)).getText().toString();
                        Log.d(Constants.LOG_TAG, String.format("dialog_classroom_name onClick positive; name='%s'", classroomName));
                        createNewClassroom(classroomName);
                    }
                })
                .setNegativeButton(R.string.alert_option_cancel, null)
                .setTitle(R.string.alert_title_new_classroom)
                .setMessage(R.string.alert_message_new_classroom);
        builder.create().show();
    }


    private void createNewClassroom(String classroomName) {
        if (!classroomName.isEmpty()) {
            Classroom classroom = new Classroom(classroomName);
            new UpdateClassroomModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateClassroomModelAsyncTask.ActionType.INSERT, classroom, new UpdateClassroomModelAsyncTask.PostExecute() {
                @Override
                public void onPostExecute(Boolean modelSaved, Classroom classroom) {
                    startClassroomShowStatsActivity(classroom);
                }
            }).execute();
        } else {
            Log.w(Constants.LOG_TAG, "Classroom model not created when classroomName is empty");
        }
    }


    private void startClassroomShowStatsActivity(Classroom classroom) {
        // TODO #112 hide unused for now (navigate to students instead for now)
//        Intent classroomShowStatsActivity = new Intent(ClassroomIndexActivity.this, ClassroomShowStatsActivity.class);
//        classroomShowStatsActivity.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
//        startActivity(classroomShowStatsActivity);

        Intent classroomStudentsActivity = new Intent(ClassroomIndexActivity.this, ClassroomShowStudentsActivity.class);
        classroomStudentsActivity.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
        startActivity(classroomStudentsActivity);
    }


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
                displayClassroomNameDialog();
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
        startClassroomShowStatsActivity(classroom);
    }

}
