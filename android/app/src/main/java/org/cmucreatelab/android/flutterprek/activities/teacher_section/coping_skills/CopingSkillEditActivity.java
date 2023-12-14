package org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

// TODO see StudentUpdateAbstractActivity
//public class CopingSkillsEditActivity extends StudentUpdateAbstractActivity {
public class CopingSkillEditActivity extends AbstractActivity {

    //private static final String headerTitleEditStudent = "Edit Student";


    //@Override
    public String getHeaderTitle() {
        return "Edit Coping Skill";
    }


    //@Override
    public boolean isDisplayDeleteButton() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO actions

    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_coping_skills_edit;
    }

}