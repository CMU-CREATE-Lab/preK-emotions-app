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
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.audio.audio_recording.SaveFileHandler;
import org.cmucreatelab.android.mylibrary.CameraActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadPhotoActivity extends AbstractActivity {

    public static final int UPDATE = 200;
    public static final int KEEP = 201;
    public static final int RESET = 202;
    private int requestCode;
    private ImageView resetToIconButton, keepOldImageButton,
            updateImageButton, displayedImage, closeButton;

    private Uri displayedImagedUri;
    private CropOverlayView cropOverlay;

    private Bitmap getCroppedPicture() {

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
    private void acceptPhoto() throws IOException {
        //String pictureFilename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
        String pictureFilename = "test";
        File picture = SaveFileHandler.getOutputMediaFile(getApplicationContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, pictureFilename);
        FileOutputStream out = new FileOutputStream(picture);
        getCroppedPicture().compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();


        String path = picture.getAbsolutePath();
        Intent intent = new Intent(this, StudentHighlightsActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("resultCode", UPDATE);
        intent.putExtra("requestCode", requestCode);
        startActivity(intent);


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
                try {
                    acceptPhoto();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPhotoActivity.this, CameraActivity.class);
                GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
                startActivityForResult(intent, 1);
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

        this.resetToIconButton = findViewById(R.id.resetToIconButton);
        this.keepOldImageButton = findViewById(R.id.keepOldImageButton);
        this.updateImageButton = findViewById(R.id.updateImageButton);
        this.displayedImage = findViewById(R.id.displayedImageView);
        this.closeButton = findViewById(R.id.closeButton);


        Glide.with(this)
                .load(displayedImagedUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(displayedImage);

//        init cropper view
        cropOverlay = findViewById(R.id.cropOverlay);
        cropOverlay.setImageView(displayedImage);

        initOnClickListeners();
        setButtonColors(requestCode);
    }

    private void setButtonColors(int emotion){
        switch(emotion){
            case StudentHighlightsActivity.HAPPY_CODE:
                setButtonColorsHelper(ColorConstants.HAPPY_COLOR);
                break;
            case StudentHighlightsActivity.SAD_CODE:
                setButtonColorsHelper(ColorConstants.SAD_COLOR);
                break;
            case StudentHighlightsActivity.ANGRY_CODE:
                setButtonColorsHelper(ColorConstants.MAD_COLOR);
                break;
            case StudentHighlightsActivity.SCARED_CODE:
                setButtonColorsHelper(ColorConstants.SCARED_COLOR);
                break;
            case StudentHighlightsActivity.EXCITED_CODE:
                setButtonColorsHelper(ColorConstants.EXCITED_COLOR);
                break;
        }
    }

    private void setButtonColorsHelper(int color){
        ((CircleImageView) resetToIconButton).setBorderColor(color);
        ((CircleImageView) keepOldImageButton).setBorderColor(color);
        ((CircleImageView) updateImageButton).setBorderColor(color);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
        Intent intent = getIntent();
        requestCode = intent.getIntExtra("requestCode", 0);
        displayedImagedUri = intent.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("penguin", "result code: " + resultCode);

        //handles logic for retaking a picture after X clicked
        if(resultCode == RESULT_OK){
            displayedImagedUri = data.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
        } else if(resultCode == RESULT_CANCELED){
            Log.v("penguin", "cancled");
            Intent intent = new Intent(UploadPhotoActivity.this, StudentHighlightsActivity.class);
            setResult(RESULT_CANCELED, intent);
            startActivity(intent);
        }

    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }
}



