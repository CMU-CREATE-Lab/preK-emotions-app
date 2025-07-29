package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionHighlightAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students.StudentHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.CropOverlayView;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.UpdateStudentModelAsyncTask;
import org.cmucreatelab.android.flutterprek.audio.audio_recording.SaveFileHandler;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.customization.CustomizationDAO;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.ResolvedEmotionWithImageFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.mylibrary.CameraActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadPhotoActivity extends AbstractActivity {

    public static final int UPDATE = 200;
    public static final int KEEP = 201;
    public static final int RESET = 202;
    private int requestCode;
    private ImageView resetToIconButton, keepOldImageButton,
            updateImageButton, displayedImage, closeButton;

    private String classroomName, studentUuid;
    private Student student;
    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";
    public static final String STUDENT_UUID = "student_uuid";
    private static final String CUSTOMIZATION_KEY = "imageFileUuid";
    private static final String HAPPY_UUID = "emotion1";
    private static final String SAD_UUID = "emotion2";
    private static final String MAD_UUID = "emotion3";
    private static final String SCARED_UUID = "emotion5";
    private static final String EXCITED_UUID = "emotion6";

    private Uri displayedImagedUri;
    private CropOverlayView cropOverlay;
    private boolean fromFile;
    private Customization currentEmotionCustomization;
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
        String pictureFilename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
        //String pictureFilename = "test";
        File picture = SaveFileHandler.getOutputMediaFile(getApplicationContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, pictureFilename);
        FileOutputStream out = new FileOutputStream(picture);
        getCroppedPicture().compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();

        updateModel(student, picture);

        String path = picture.getAbsolutePath();
        Intent intent = new Intent(this, StudentHighlightsActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("resultCode", UPDATE);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra(STUDENT_UUID, studentUuid);
        intent.putExtra(EXTRA_CLASSROOM_NAME, classroomName);
        startActivity(intent);


    }

    public void updateModel(final Student student, final File newStudentPicture) {
        Log.d(Constants.LOG_TAG, "performing DB writes in updateModel()");
        String filePath = newStudentPicture.getPath();

        switch(requestCode) {
            case StudentHighlightsActivity.STUDENT_CODE:
                new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.UPDATE, student, newStudentPicture, new UpdateStudentModelAsyncTask.PostExecute() {
                    @Override
                    public void onPostExecute(Boolean modelSaved) {
                        if (!modelSaved) {
                            Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }).execute();
                break;

            case StudentHighlightsActivity.HAPPY_CODE:
                checkForCustomizationAndUpdate(HAPPY_UUID, filePath);
                break;
            case StudentHighlightsActivity.SAD_CODE:
                checkForCustomizationAndUpdate(SAD_UUID, filePath);
                break;
            case StudentHighlightsActivity.ANGRY_CODE:
                checkForCustomizationAndUpdate(MAD_UUID, filePath);
                break;
            case StudentHighlightsActivity.SCARED_CODE:
                checkForCustomizationAndUpdate(SCARED_UUID, filePath);
                break;
            case StudentHighlightsActivity.EXCITED_CODE:
                checkForCustomizationAndUpdate(EXCITED_UUID, filePath);
                break;
        }

    }
    //checks to see if the student already has a custom emotion image and then calls insertEmotionCustomization
    //to update or insert new customization
    private void checkForCustomizationAndUpdate(String emotionUuid, String filePath){

        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        appDatabase.customizationDAO().getCustomizationsOwnedBy(studentUuid).observe(this, new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> customizations) {
                boolean hasCustimization = false;
                for(Customization customization : customizations){
                    if(customization.getBasedOnUuid().equals(emotionUuid)){
                        hasCustimization = true;
                        insertEmotionCustomization(emotionUuid, filePath, hasCustimization, customization);

                    }
                }
                if(!hasCustimization){
                    insertEmotionCustomization(emotionUuid, filePath, hasCustimization, null);
                }

            }

        });
    }
    private void resetImage(){
        File newStudentPicture = placeHolderToFile(R.drawable.ic_placeholder_png);

        if(requestCode == StudentHighlightsActivity.STUDENT_CODE){
            new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.UPDATE, student, newStudentPicture, new UpdateStudentModelAsyncTask.PostExecute() {
                @Override
                public void onPostExecute(Boolean modelSaved) {
                    if (!modelSaved) {
                        Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }).execute();
        } else{
            deleteCustomEmotion();
        }
    }

    //used to reset the emotion image back to default by deleting customization row
    private void deleteCustomEmotion(){
        String emotionUuid = grabEmotionUuid();
        //grab customization and delete from db
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        appDatabase.customizationDAO().getCustomizationsOwnedBy(studentUuid).observe(this, new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> customizations) {

                for(Customization customization : customizations){
                    if(customization.getBasedOnUuid().equals(emotionUuid)){
                        Executors.newSingleThreadExecutor().execute(() -> {
                            appDatabase.customizationDAO().delete(customization);

                        });

                    }
                }


            }

        });
    }

    private String grabEmotionUuid(){
        switch(requestCode){
            case StudentHighlightsActivity.HAPPY_CODE:
                return HAPPY_UUID;
            case StudentHighlightsActivity.SAD_CODE:
                return SAD_UUID;
            case StudentHighlightsActivity.ANGRY_CODE:
                return MAD_UUID;
            case StudentHighlightsActivity.SCARED_CODE:
                return SCARED_UUID;
            case StudentHighlightsActivity.EXCITED_CODE:
                return EXCITED_UUID;
            default:
                return null;
        }
    }
    private File placeHolderToFile(int drawableId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);

        String pictureFilename = String.format("%s_%d", student.getUuid(), Util.getCurrentTimestamp());
        File picture = SaveFileHandler.getOutputMediaFile(getApplicationContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, pictureFilename);

        // Step 2: Create a file in the cache directory

        try {
            FileOutputStream outputStream = new FileOutputStream(picture);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // or JPEG
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return picture;
    }

    private void initOnClickListeners() {
        resetToIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetImage();
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

        setLayoutCrop();

//        init cropper view
        cropOverlay = findViewById(R.id.cropOverlay);
        cropOverlay.setImageView(displayedImage);

        initOnClickListeners();
        setButtonColors(requestCode);

        Glide.with(this)
                .load(displayedImagedUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() { //glide and layout listener to set updateimage icon at create
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {

                        displayedImage.setImageDrawable(resource);

                        displayedImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                displayedImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                cropOverlay.centerCropRectOnImage();

                                Bitmap cropped = getCroppedPicture();

                                updateImageButton.setImageBitmap(cropped);
                            }
                        });

                        return true;
                    }
                })
                .into(displayedImage);

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
            case StudentHighlightsActivity.STUDENT_CODE:
                setButtonColorsHelper(Color.GRAY);
        }
    }

    private void setButtonImages(){
        switch(requestCode){
            case StudentHighlightsActivity.STUDENT_CODE:
               // ImageView resetToIconButton = findViewById(R.id.resetToIconButton);

                //keep button
                if (student.getPictureFileUuid() != null) {
                    final Context appContext = getApplicationContext();
                    AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(this, new Observer<DbFile>() {
                        @Override
                        public void onChanged(@Nullable DbFile dbFile) {
                            Util.setImageViewWithDbFile(appContext, keepOldImageButton, dbFile);
                        }
                    });
                } else {
                    keepOldImageButton.setImageResource(R.drawable.ic_placeholder);
                }

                //reset button
                resetToIconButton.setImageResource(R.drawable.ic_placeholder);
                break;

            //ALL THE CASES FOR THE EMOTION IMAGES
            case StudentHighlightsActivity.HAPPY_CODE:
                setEmotionKeepOldButton(HAPPY_UUID);
                resetToIconButton.setImageResource(R.drawable.ic_happy);
                break;

            case StudentHighlightsActivity.SAD_CODE:
                setEmotionKeepOldButton(SAD_UUID);
                resetToIconButton.setImageResource(R.drawable.ic_sad);
                break;

            case StudentHighlightsActivity.ANGRY_CODE:
                setEmotionKeepOldButton(MAD_UUID);
                resetToIconButton.setImageResource(R.drawable.ic_mad);
                break;

            case StudentHighlightsActivity.SCARED_CODE:
                setEmotionKeepOldButton(SCARED_UUID);
                resetToIconButton.setImageResource(R.drawable.ic_scared);
                break;

            case StudentHighlightsActivity.EXCITED_CODE:
                setEmotionKeepOldButton(EXCITED_UUID);
                resetToIconButton.setImageResource(R.drawable.ic_excited);
                break;
        }
    }

    private void setEmotionKeepOldButton(String emotionUuid){


        AppDatabase.getInstance(getApplicationContext()).embeddedDAO().getResolvedEmotionsForStudent(student.getUuid()).observe(UploadPhotoActivity.this, new Observer<List<ResolvedEmotionWithImageFile>>() {
            @Override
            public void onChanged(List<ResolvedEmotionWithImageFile> resolvedEmotionWithImageFiles) {
                // replaces adapter code from above
                Log.v(Constants.LOG_TAG, String.format("Room DB getResolvedEmotionsForStudent() returned with list results size = %d", resolvedEmotionWithImageFiles.size()));
                final List<Emotion> emotionList = Util.EmotionMapper.fromResolvedList(resolvedEmotionWithImageFiles);
                for(Emotion emotion : emotionList){
                   if(emotion.getUuid().equals(emotionUuid)){
                       if (emotion.getImageFileUuid() != null) {
                           AppDatabase.getInstance(UploadPhotoActivity.this).dbFileDAO().getDbFile(emotion.getImageFileUuid()).observe(UploadPhotoActivity.this, new Observer<DbFile>() {
                               @Override
                               public void onChanged(@Nullable DbFile dbFile) {
                                   Util.setImageViewWithDbFile(UploadPhotoActivity.this,(ImageView) keepOldImageButton, dbFile);
                               }
                           });
                       } else {
                           keepOldImageButton.setImageResource(R.drawable.ic_placeholder);
                       }
                   }
               }

            }
        });

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

        fromFile = intent.getBooleanExtra("fromFiles", false);



        if(getIntent().getStringExtra(EXTRA_CLASSROOM_NAME) != null && getIntent().getStringExtra(EXTRA_STUDENT) != null) {
            this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
            this.studentUuid = getIntent().getStringExtra(EXTRA_STUDENT);

            //grab student object
            AppDatabase.getInstance(this).studentDAO().getStudent(studentUuid).observe(this, new Observer<Student>() {
                @Override
                public void onChanged(@Nullable Student student) {
                    UploadPhotoActivity.this.student = student;
                    setButtonImages();

                }
            });


        }

        //test
        //    { "uuid": "custom_emotion1", "basedOnUuid": "emotion1", "key": "imageFileUuid", "value": "ic_yoga", "ownerUuid": "student1" }


    }

    //either creates a new customization or updates current
    private void insertEmotionCustomization(String emotionUuid, String filePath, boolean hasCustomization, Customization customization) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // Insert DbFile
            DbFile dbFile = new DbFile(DbFile.DbFileType.FILEPATH, filePath);
            db.dbFileDAO().insert(dbFile);

            // Insert Customization
            if(hasCustomization){
                customization.setValue(dbFile.getUuid());
                db.customizationDAO().update(customization);
            } else {
                String customUuid = String.format("%s_%s_%d", student.getUuid(), "custom_emotion", Util.getCurrentTimestamp());
                Customization testCustom = new Customization(customUuid, CUSTOMIZATION_KEY, dbFile.getUuid());
                testCustom.setOwnerUuid(studentUuid);
                testCustom.setBasedOnUuid(emotionUuid);
                db.customizationDAO().insert(testCustom);
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fromFile = data.getBooleanExtra("fromFiles", false);
        Log.v("twitter", "fromFile: " + fromFile);
        //handles logic for retaking a picture after X clicked
        if(resultCode == RESULT_OK){
            displayedImagedUri = data.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);
            setLayoutCrop();

        } else if(resultCode == RESULT_CANCELED){

            Intent intent = new Intent(UploadPhotoActivity.this, StudentHighlightsActivity.class);
            intent.putExtra("resultCode", UPDATE);
            intent.putExtra("requestCode", requestCode);
            intent.putExtra(STUDENT_UUID, studentUuid);
            intent.putExtra(EXTRA_CLASSROOM_NAME, classroomName);
            intent.putExtra("fromFiles", fromFile);
            setResult(RESULT_CANCELED, intent);
            startActivity(intent);
        }

    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }

    private void setLayoutCrop(){
        if(fromFile){
            displayedImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            displayedImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}



