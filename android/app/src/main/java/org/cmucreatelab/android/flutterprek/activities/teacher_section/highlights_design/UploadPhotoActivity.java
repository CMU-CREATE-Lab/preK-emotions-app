package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.mylibrary.CameraActivity;

public class UploadPhotoActivity extends AbstractActivity {

    private ImageView resetToIconButton, keepOldImageButton, updateImageButton;
    private ImageView displayedImage;

    private Uri displayedImagedUri;


    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        this.resetToIconButton = findViewById(R.id.resetToIconButton);
        this.keepOldImageButton = findViewById(R.id.keepOldImageButton);
        this.updateImageButton = findViewById(R.id.updateImageButton);

        this.displayedImage = findViewById(R.id.displayedImageView);

        textViewPlaceholder.setText("UploadPhotoActivity");

        resetToIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        keepOldImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(2);
                finish();
            }
        });
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("foo", "bar");
                setResult(3, data);
                finish();
            }
        });

        Glide.with(this)
                .load(displayedImagedUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(displayedImage);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
        Intent intent = getIntent();
        displayedImagedUri = intent.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
        if(displayedImagedUri == null) {
            Log.v("penguin", "null");
        } else {
            Log.v("penguin", "not null");
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }
}



