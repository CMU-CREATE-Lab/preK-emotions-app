package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.os.Bundle;
import android.view.WindowManager;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeader;

public class SettingsPasswordActivity extends TeacherSectionActivityWithHeader {

    // TODO actions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the keyboard when the activity starts (selects input box by default)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_change_password;
    }

}
