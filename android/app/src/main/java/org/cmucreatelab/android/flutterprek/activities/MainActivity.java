package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseClassroomActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseEmotionActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.ClassroomIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.CopingSkillIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.EmotionIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.StudentIndexActivity;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomViewModel;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.database.models.student.StudentViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ClassroomViewModel classroomViewModel;
    private StudentViewModel studentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classroomViewModel = ViewModelProviders.of(this).get(ClassroomViewModel.class);
        classroomViewModel.getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable final List<Classroom> classrooms) {
                Log.i(Constants.LOG_TAG,"onChanged classrooms");
            }
        });

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable final List<Student> students) {
                Log.i(Constants.LOG_TAG,"onChanged students");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_classroom_index:
                Intent classroomsIndexActivity = new Intent(MainActivity.this, ClassroomIndexActivity.class);
                startActivity(classroomsIndexActivity);
                return true;
            case R.id.menu_student_index:
                Intent studentsIndexActivity = new Intent(MainActivity.this, StudentIndexActivity.class);
                startActivity(studentsIndexActivity);
                return true;
            case R.id.menu_coping_skill_index:
                Intent copingSkillsIndexActivity = new Intent(MainActivity.this, CopingSkillIndexActivity.class);
                startActivity(copingSkillsIndexActivity);
                return true;
            case R.id.menu_emotion_index:
                Intent emotionsIndexActivity = new Intent(MainActivity.this, EmotionIndexActivity.class);
                startActivity(emotionsIndexActivity);
                return true;
            case R.id.menu_choose_classroom:
                Intent chooseClassroomActivity = new Intent(MainActivity.this, ChooseClassroomActivity.class);
                startActivity(chooseClassroomActivity);
                return true;
            case R.id.menu_choose_student:
                Intent chooseStudentActivity = new Intent(MainActivity.this, ChooseStudentActivity.class);
                startActivity(chooseStudentActivity);
                return true;
            case R.id.menu_choose_emotion:
                Intent chooseEmotionActivity = new Intent(MainActivity.this, ChooseEmotionActivity.class);
                startActivity(chooseEmotionActivity);
                return true;
            case R.id.menu_choose_coping_skill:
                Intent chooseCopingSkillActivity = new Intent(MainActivity.this, ChooseCopingSkillActivity.class);
                startActivity(chooseCopingSkillActivity);
                return true;
            // extras
            case R.id.menu_web:
                Intent webIndexActivity = new Intent(MainActivity.this, WebIndexActivity.class);
                startActivity(webIndexActivity);
                return true;
        }
        return false;
    }

}
