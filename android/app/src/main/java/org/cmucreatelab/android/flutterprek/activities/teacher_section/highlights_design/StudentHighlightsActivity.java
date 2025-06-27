package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.mylibrary.CameraActivity;

public class StudentHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {


    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        Button buttonPlaceholder = findViewById(R.id.buttonPlaceholder);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPlaceholder.setText("StudentHighlightsActivity");
                buttonPlaceholder.setText("Launch Camera Activity");
            }
        });

        buttonPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
                //Intent intent = new Intent(StudentHighlightsActivity.this, UploadPhotoActivity.class);
                Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_placeholder;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
        if (requestCode == 1) {
            if (data != null) {
                Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d AND data not null", resultCode));
            } else {
                Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d (data null)", resultCode));
            }
        }
    }

}



