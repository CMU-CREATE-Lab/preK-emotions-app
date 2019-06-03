package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.AppHeaderFragment;

public abstract class TeacherSectionActivityWithHeader extends AbstractActivity {

    private AppHeaderFragment headerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.headerFragment = (AppHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.appHeader));
        this.headerFragment.setHeaderTransparency(false);

        findViewById(R.id.imageStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageStudent();
            }
        });
    }



    public void onClickImageStudent() {
        // does nothing by default
        // TODO bring to student section by default?
    }

}
