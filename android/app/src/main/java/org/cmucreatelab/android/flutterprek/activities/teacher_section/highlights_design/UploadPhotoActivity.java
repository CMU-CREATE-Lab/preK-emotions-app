package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

public class UploadPhotoActivity extends AbstractActivity {


    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        Button buttonPlaceholder = findViewById(R.id.buttonPlaceholder);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPlaceholder.setText("UploadPhotoActivity");
                //buttonPlaceholder.setText("Launch Camera Activity");
                buttonPlaceholder.setVisibility(View.GONE);
            }
        });

//        buttonPlaceholder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO next activity
//            }
//        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }

}
