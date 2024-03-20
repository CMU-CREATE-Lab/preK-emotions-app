package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings.troubleshoot_device;

import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.DrawerTeacherMainFragment;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeaderAndDrawer;

public class SettingsTroubleshootDeviceShow extends TeacherSectionActivityWithHeaderAndDrawer {

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_troubleshoot_device_show_with_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public DrawerTeacherMainFragment.Section getSectionForDrawer() {
        return null;
    }
}
