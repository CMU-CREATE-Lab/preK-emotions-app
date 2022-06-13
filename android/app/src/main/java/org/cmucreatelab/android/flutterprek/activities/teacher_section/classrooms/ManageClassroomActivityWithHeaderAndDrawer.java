package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherClassroomFragment;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeader;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

public abstract class ManageClassroomActivityWithHeaderAndDrawer extends TeacherSectionActivityWithHeader {

    public static final String EXTRA_CLASSROOM = "classroom";

    private DrawerTeacherClassroomFragment drawerTeacherClassroomFragment;
    private Classroom classroom;


    public Classroom getClassroom() {
        return classroom;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.drawerTeacherClassroomFragment = (DrawerTeacherClassroomFragment) (getSupportFragmentManager().findFragmentById(R.id.drawerTeacherClassroom));
        this.drawerTeacherClassroomFragment.setCurrentSection(getSectionForDrawer());

        this.classroom = (Classroom) getIntent().getSerializableExtra(EXTRA_CLASSROOM);
    }


    /**
     * Each teacher activity with {@link DrawerTeacherMainFragment} must define what section it is part of.
     * @return
     */
    public abstract DrawerTeacherClassroomFragment.Section getSectionForDrawer();

}
