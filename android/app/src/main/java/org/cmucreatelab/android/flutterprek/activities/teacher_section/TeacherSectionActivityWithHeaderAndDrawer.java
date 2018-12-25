package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.AppHeaderFragment;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;

public abstract class TeacherSectionActivityWithHeaderAndDrawer extends TeacherSectionActivityWithHeader {

    private DrawerTeacherMainFragment drawerTeacherMainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.drawerTeacherMainFragment = (DrawerTeacherMainFragment) (getSupportFragmentManager().findFragmentById(R.id.drawerTeacherMain));
        this.drawerTeacherMainFragment.setCurrentSection(getSectionForDrawer());
    }


    /**
     * Each teacher activity with {@link DrawerTeacherMainFragment} must define what section it is part of.
     * @return
     */
    public abstract DrawerTeacherMainFragment.Section getSectionForDrawer();

}
