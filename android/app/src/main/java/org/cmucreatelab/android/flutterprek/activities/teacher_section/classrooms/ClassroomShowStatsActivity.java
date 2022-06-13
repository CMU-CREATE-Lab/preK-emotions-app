package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherClassroomFragment;

public class ClassroomShowStatsActivity extends ManageClassroomActivityWithHeaderAndDrawer {


//    @Override
//    protected void onResume() {
//        super.onResume();
//
////        LiveData<List<StudentWithCustomizations>> liveData;
////        liveData = AppDatabase.getInstance(this).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
////        liveData.observe(this, new Observer<List<StudentWithCustomizations>>() {
////            @Override
////            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
////                GridView studentsGridView = findViewById(R.id.studentsGridView);
////                studentsGridView.setAdapter(new StudentWithCustomizationsIndexAdapter(ClassroomShowStatsActivity.this, students, listener));
////            }
////        });
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        this.classroomUuid = getIntent().getStringExtra(EXTRA_CLASSROOM_UUID);
////        this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
////
////        findViewById(R.id.fabNewStudent).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent studentAddActivityIntent = new Intent(ClassroomShowStatsActivity.this, StudentAddActivity.class);
////                studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_STUDENT, new Student(templateNameForAddStudent, classroomUuid));
////                studentAddActivityIntent.putExtra(StudentEditActivity.EXTRA_CLASSROOM_NAME, classroomName);
////                startActivity(studentAddActivityIntent);
////            }
////        });
//    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_classrooms_show_stats;
    }


    @Override
    public DrawerTeacherClassroomFragment.Section getSectionForDrawer() {
        return DrawerTeacherClassroomFragment.Section.STATS;
    }

}
