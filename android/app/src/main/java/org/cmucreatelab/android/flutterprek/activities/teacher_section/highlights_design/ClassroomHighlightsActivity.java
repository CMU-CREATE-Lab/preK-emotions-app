package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStudentsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentEditActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ClassroomHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    private String classroomUuid;
    private String classroomName;
    private Classroom classroom;
    public static final String EXTRA_CLASSROOM = "classroom";

    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        Button buttonPlaceholder = findViewById(R.id.buttonPlaceholder);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPlaceholder.setText("ClassroomHighlightsActivity");
                buttonPlaceholder.setText("GoTo Student");
            }
        });

        buttonPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO next activity
                Intent intent = new Intent(ClassroomHighlightsActivity.this, StudentHighlightsActivity.class);
                startActivity(intent);
            }
        });

        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                GridView studentsGridView = findViewById(R.id.studentsGridView);
                 studentsGridView.setAdapter(new StudentHighlightWithCustomizationsIndexAdapter(ClassroomHighlightsActivity.this, students, listener));

                 Log.v("penguin", String.valueOf(students.size()));
            }
        });
    }

    private final StudentHighlightWithCustomizationsIndexAdapter.ClickListener listener = new StudentHighlightWithCustomizationsIndexAdapter.ClickListener() {
        @Override
        public void onClick(StudentWithCustomizations studentWithCustomizations) {
            final Student student = studentWithCustomizations.student;
            Log.d(Constants.LOG_TAG, "onClick student = " + student.getName());

//            Intent studentEditActivity = new Intent(ClassroomShowStudentsActivity.this, StudentEditActivity.class);
//            studentEditActivity.putExtra(StudentEditActivity.EXTRA_STUDENT, studentWithCustomizations.student);
//            studentEditActivity.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
//            startActivity(studentEditActivity);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Classroom from Intent?
        this.classroom = (Classroom) getIntent().getSerializableExtra(EXTRA_CLASSROOM);

        this.classroomUuid = getClassroom().getUuid();
        this.classroomName = getClassroom().getName();


        // TODO STUDENTS adapter
        //...
//        AppDatabase.getInstance(this).classroomDAO().getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
//            @Override
//            public void onChanged(@Nullable List<Classroom> classrooms) {
//                GridView classroomsGridView = findViewById(R.id.classroomsGridView);
//                classroomsGridView.setAdapter(new ClassroomIndexAdapter(ClassroomHighlightsActivity.this, classrooms, ClassroomHighlightsActivity.this));
//            }
//        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_classroom_highlights;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}



