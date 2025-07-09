package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.mylibrary.CameraActivity;

import java.io.File;

public class StudentHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    public static final int HAPPY_CODE = 100;
    public static final int SAD_CODE = 101;
    public static final int ANGRY_CODE = 102;
    public static final int SCARED_CODE = 103;
    public static final int EXCITED_CODE = 104;

    private Button buttonAngry, buttonHappy, buttonSad;

    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        buttonAngry= findViewById(R.id.buttonAngry);
        buttonHappy= findViewById(R.id.buttonHappy);
        buttonSad= findViewById(R.id.buttonSad);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewPlaceholder.setText("StudentHighlightsActivity");
            }
        });
        initListeners();
    }

    private void initListeners(){
        buttonAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
                Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
                GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
                startActivityForResult(intent, ANGRY_CODE);
            }
        });

        buttonHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
                Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
                GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
                startActivityForResult(intent, HAPPY_CODE);
            }
        });

        buttonSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
                Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
                GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
                startActivityForResult(intent, SAD_CODE);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //process picture from uploadPhotoActivity
        Intent intent = getIntent();
        if(intent.getStringExtra("path") !=null){
            int resultCode = intent.getIntExtra("resultCode", -1);
            int requestCode = intent.getIntExtra("requestCode", -1);
            handleResult(requestCode, resultCode, intent);
        }

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
        handleResult(requestCode, resultCode, data);
        GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = false;

    }

    private void handleResult(int requestCode, int resultCode, Intent data) {
      switch(resultCode) {
          case Activity.RESULT_OK:
              launchUploadPhotoActivity(requestCode, resultCode, data);
              break;

          case Activity.RESULT_CANCELED:
              break;

          case UploadPhotoActivity.UPDATE:
              String path = data.getStringExtra("path");
              File file = new File(path);

              Uri uri = Uri.fromFile(file);
              ImageView imageView = findViewById(R.id.testImage);
              imageView.setImageURI(uri);
              break;

          case UploadPhotoActivity.KEEP:
              break;

          case UploadPhotoActivity.RESET:
              break;

      }
    }

    private void launchUploadPhotoActivity(int requestCode, int resultCode, Intent data){

        if (data != null) {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d AND data not null", resultCode));

            Intent  intent = new Intent(StudentHighlightsActivity.this, UploadPhotoActivity.class);
            Uri imageUri = data.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
            intent.putExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI, imageUri);
            intent.putExtra("requestCode", requestCode);
            startActivity(intent);

        } else {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d (data null)", resultCode));
        }

    }

    private void saveImage(){
        return;
    }

}



