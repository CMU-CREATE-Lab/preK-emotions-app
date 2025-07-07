package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.mylibrary.CameraActivity;

public class UploadPhotoActivity extends AbstractActivity {

    private ImageView resetToIconButton, keepOldImageButton,
            updateImageButton, displayedImage, closeButton;

    private Uri displayedImagedUri;
    private CropOverlayView cropOverlay;

    private Bitmap getCroppedPicture() {

//        RectF cropRect = cropOverlay.getCropRect();
//        // 1. Take a screenshot of the root view
//        View rootView = getWindow().getDecorView().getRootView();
//        rootView.setDrawingCacheEnabled(true);
//        rootView.buildDrawingCache();
//        Bitmap screenBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//
//        // 2. Crop using the provided RectF (in screen/view coords)
//        int x = (int) cropRect.left;
//        int y = (int) cropRect.top;
//        int width = (int) cropRect.width();
//        int height = (int) cropRect.height();
//
//        // Clamp to screen bounds
//        x = Math.max(0, x);
//        y = Math.max(0, y);
//        width = Math.min(width, screenBitmap.getWidth() - x);
//        height = Math.min(height, screenBitmap.getHeight() - y);
//
//        return Bitmap.createBitmap(screenBitmap, x, y, width, height);

        if (displayedImage.getDrawable() == null) return null;

        // Step 1: Convert Drawable to Bitmap
        Bitmap originalBitmap = ((BitmapDrawable) displayedImage.getDrawable()).getBitmap();

        // Step 2: Get actual image bounds inside the ImageView
        RectF imageBounds = getImageBounds(displayedImage, originalBitmap);

        // Step 3: Get crop rect from overlay (in screen/imageView coords)
        RectF cropRect = cropOverlay.getCropRect();

        // Step 4: Convert cropRect (view-space) to Bitmap-space
        float scaleX = originalBitmap.getWidth() / imageBounds.width();
        float scaleY = originalBitmap.getHeight() / imageBounds.height();

        float offsetX = cropRect.left - imageBounds.left;
        float offsetY = cropRect.top - imageBounds.top;

        int cropLeft = Math.round(offsetX * scaleX);
        int cropTop = Math.round(offsetY * scaleY);
        int cropWidth = Math.round(cropRect.width() * scaleX);
        int cropHeight = Math.round(cropRect.height() * scaleY);

        // Clamp to Bitmap bounds to avoid crash
        cropLeft = Math.max(0, Math.min(cropLeft, originalBitmap.getWidth() - 1));
        cropTop = Math.max(0, Math.min(cropTop, originalBitmap.getHeight() - 1));
        cropWidth = Math.min(cropWidth, originalBitmap.getWidth() - cropLeft);
        cropHeight = Math.min(cropHeight, originalBitmap.getHeight() - cropTop);

        // Step 5: Return cropped Bitmap
        return Bitmap.createBitmap(originalBitmap, cropLeft, cropTop, cropWidth, cropHeight);
    }
    private RectF getImageBounds(ImageView imageView, Bitmap bitmap) {
        float viewWidth = imageView.getWidth();
        float viewHeight = imageView.getHeight();
        float drawableWidth = bitmap.getWidth();
        float drawableHeight = bitmap.getHeight();

        Matrix imageMatrix = imageView.getImageMatrix();
        float[] values = new float[9];
        imageMatrix.getValues(values);

        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        float actualWidth = drawableWidth * scaleX;
        float actualHeight = drawableHeight * scaleY;

        float left = transX;
        float top = transY;

        return new RectF(left, top, left + actualWidth, top + actualHeight);
    }

    private void initOnClickListeners() {
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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //detect when the cropper is changed
        cropOverlay.setOnCropRectChangedListener(new CropOverlayView.OnCropRectChangedListener() {
            @Override
            public void onCropRectChanged(RectF newRect) {

                Bitmap cropped = getCroppedPicture();
                updateImageButton.setImageBitmap(cropped);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

       // TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        this.resetToIconButton = findViewById(R.id.resetToIconButton);
        this.keepOldImageButton = findViewById(R.id.keepOldImageButton);
        this.updateImageButton = findViewById(R.id.updateImageButton);
        this.displayedImage = findViewById(R.id.displayedImageView);
        this.closeButton = findViewById(R.id.closeButton);


      //  textViewPlaceholder.setText("UploadPhotoActivity");

        Glide.with(this)
                .load(displayedImagedUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(displayedImage);

//        init cropper view
        cropOverlay = findViewById(R.id.cropOverlay);
        cropOverlay.setImageView(displayedImage);

        initOnClickListeners();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
        Intent intent = getIntent();
        displayedImagedUri = intent.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }
}



