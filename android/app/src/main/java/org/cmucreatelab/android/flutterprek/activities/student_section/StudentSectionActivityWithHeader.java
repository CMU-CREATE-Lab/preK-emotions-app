package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.AppHeaderFragment;

public abstract class StudentSectionActivityWithHeader extends AbstractActivity {

    private AppHeaderFragment headerFragment;


    private void showAdminDialog() {
        // TODO https://developer.android.com/guide/topics/ui/dialogs
        // also, try and make this conform to the design (will serve as a good template)
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.headerFragment = (AppHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.appHeader));
        this.headerFragment.setHeaderTransparency(true);

        findViewById(R.id.imageLogo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(Constants.LOG_TAG, "long clicked the MindfulNest Logo.");
                showAdminDialog();
                return true;
            }
        });
    }

}
