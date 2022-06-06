package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.StudentEditFragment;

public class StudentEditActivity extends AbstractActivity {

    private StudentEditFragment headerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.headerFragment = (StudentEditFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
        this.headerFragment.setHeaderTransparency(true);

        // avoid EditText focus when activity starts: https://stackoverflow.com/questions/4149415/onscreen-keyboard-opens-automatically-when-activity-starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // demo click listener on textview
        headerFragment.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO method for handling return back (save vs. cancel)
                finish();
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_students_edit;
    }

}
