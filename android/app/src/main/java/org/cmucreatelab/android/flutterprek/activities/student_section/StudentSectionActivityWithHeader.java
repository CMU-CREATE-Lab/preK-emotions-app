package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.AppHeaderFragment;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

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

        findViewById(R.id.imageStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageStudent();
            }
        });

        updateImageStudent(StudentSectionActivityWithHeader.this);
        findViewById(R.id.imageInfo).setVisibility( isInfoIconVisible() ? View.VISIBLE : View.GONE );
    }


    public void onClickImageStudent() {
        // go back to students page
        GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
    }


    public void updateImageStudent(AppCompatActivity activity) {
        String imageID = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.imageUuid;

        if (imageID != null && !imageID.isEmpty()) {
            final Context appContext = activity.getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(imageID).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    ImageView imageStudent = findViewById(R.id.imageStudent);
                    imageStudent.setBackground(null);
                    // TODO check if file type is asset
                    Util.setImageViewWithAsset(appContext, imageStudent, dbFile.getFilePath());

                    // make header/thumbnail larger
                    android.view.ViewGroup.LayoutParams params = imageStudent.getLayoutParams();
                    params.height = 72;
                    params.width = 72;
                    imageStudent.setLayoutParams(params);

                    View appHeader = findViewById(R.id.appHeader);
                    params = appHeader.getLayoutParams();
                    params.height = 130;
                    appHeader.setLayoutParams(params);
                }
            });
        }
    }


    public boolean isInfoIconVisible() {
        return false;
    }

}
