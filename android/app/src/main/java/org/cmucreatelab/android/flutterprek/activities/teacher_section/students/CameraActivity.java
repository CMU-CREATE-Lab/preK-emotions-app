package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.audio.audio_recording.SaveFileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Code originally from mfm-android project.
 */
public class CameraActivity extends AbstractActivity {

    private static final int defaultCameraId = 0;
    private static final int frontFacingCameraId = 1;

    public static final String EXTRA_PICTURE_FILENAME = "picture_filename";

    public static final String EXTRA_RESULT_PICTURE = "result_picture";

    public static final int REQUEST_CODE = 1021;

    public static final int RESULT_START_OVER = 101;

    public static int cameraId;

    private Camera mCamera;
    private CameraPreview mPreview;
    private byte[] possiblePhoto;
    private boolean pictureTaken;

    private ImageView flipCameraImageView;
    private ImageView loadImageView;

    private static final int REQUEST_LOAD_IMAGE = 1;
    private boolean loadImage;
    private Uri photoUri;
    private String pictureFilename;


    private static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open(cameraId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }


    private int getOrientation() {
        int result = 0;

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 1;
                break;
            case Surface.ROTATION_180:
                degrees = 2;
                break;
            case Surface.ROTATION_270:
                degrees = 3;
                break;
        }

        result = degrees;

        return result;
    }


    private int getCameraRotation() {
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    private void setOnClickListeners() {
        findViewById(R.id.load_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadImage();
            }
        });
        findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePicture();
            }
        });
        findViewById(R.id.photo_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retakePhoto();
            }
        });
        findViewById(R.id.photo_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptPhoto();
            }
        });
        findViewById(R.id.flip_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCamera();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.flipCameraImageView = findViewById(R.id.flip_camera);
        this.loadImageView = findViewById(R.id.load_image);
        setOnClickListeners();

        this.pictureFilename = getIntent().getStringExtra(EXTRA_PICTURE_FILENAME);

        loadImage = false;
        pictureTaken = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!pictureTaken) {
            this.mCamera = getCameraInstance();
            this.mCamera.setPreviewCallback(null);
            this.mPreview = new CameraPreview(getApplicationContext(), this.mCamera);

            // set the orientation and camera parameters
            int rotation = this.getCameraRotation();
            this.mCamera.setDisplayOrientation(rotation);
            Camera.Parameters params = mCamera.getParameters();
            params.setRotation(rotation);

            ArrayList<String> list = (ArrayList<String>) params.getSupportedFocusModes();
            for (String item : list) {
                if (item.equals("continuous-picture")) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
            }

            mCamera.setParameters(params);

            final FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);

            updateCameraLayoutSizes();

            cameraPreview.addView(this.mPreview);
        }
    }


    private void updateCameraLayoutSizes() {
        final FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
        final RelativeLayout layoutCameraPreview = (RelativeLayout) findViewById(R.id.layout_camera_preview);
        final LinearLayout layoutCameraButtons = (LinearLayout) findViewById(R.id.layout_camera_buttons);

        final ViewTreeObserver cameraPreviewViewTreeObserver = cameraPreview.getViewTreeObserver();
        cameraPreviewViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    RelativeLayout.LayoutParams lp;

                    if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                        lp = new RelativeLayout.LayoutParams(cameraPreview.getWidth(), cameraPreview.getWidth() * 4 / 3);
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        cameraPreview.setLayoutParams(lp);
                    } else {
                        //for some reason, the layout params have to instantiated every time or they do not work
                        lp = new RelativeLayout.LayoutParams(cameraPreview.getHeight() * 4 / 3, cameraPreview.getHeight());
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        cameraPreview.setLayoutParams(lp);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            final ViewTreeObserver layoutCameraPreviewViewTreeObserver = layoutCameraPreview.getViewTreeObserver();
            layoutCameraPreviewViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    try {
                        RelativeLayout.LayoutParams lp;

                        //for some reason, the layout params have to instantiated every time or they do not work
                        lp = new RelativeLayout.LayoutParams(layoutCameraPreview.getHeight() * 4 / 3, layoutCameraPreview.getHeight());
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        layoutCameraPreview.setLayoutParams(lp);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final ViewTreeObserver layoutCameraButtonsViewTreeObserver = layoutCameraButtons.getViewTreeObserver();
            layoutCameraButtonsViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    try {
                        RelativeLayout.LayoutParams lp;

                        //for some reason, the layout params have to instantiated every time or they do not work
                        lp = new RelativeLayout.LayoutParams(layoutCameraButtons.getHeight() * 4 / 3, layoutCameraButtons.getHeight());
                        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        layoutCameraButtons.setLayoutParams(lp);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if (this.mCamera != null && this.mPreview != null) {
            this.mCamera.setPreviewCallback(null);
            this.mPreview.getHolder().removeCallback(this.mPreview);
            this.mCamera.release();
            this.mCamera = null;
            this.mPreview = null;
        }
    }


    public void onLoadImage() {
        if (!pictureTaken && !loadImage) {
            loadImageIntent();
        }
    }


    private void loadImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOAD_IMAGE:
                    photoUri = data.getData();
                    loadImage = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                    final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(photoUri);
                        preview.setForeground(Drawable.createFromStream(inputStream, photoUri.toString()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    ImageView photoNo = (ImageView) findViewById(R.id.photo_no);
                    ImageView photoYes = (ImageView) findViewById(R.id.photo_yes);
                    photoNo.setImageResource(R.mipmap.photo_no);
                    photoYes.setImageResource(R.mipmap.photo_yes);
                    findViewById(R.id.take_picture).setVisibility(View.INVISIBLE);

                    break;
            }
        }
    }


    public void onTakePicture() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // call back for creating the picture
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                ImageView photoNo = (ImageView) findViewById(R.id.photo_no);
                ImageView photoYes = (ImageView) findViewById(R.id.photo_yes);
                photoNo.setImageResource(R.mipmap.photo_no);
                photoYes.setImageResource(R.mipmap.photo_yes);
                findViewById(R.id.take_picture).setVisibility(View.INVISIBLE);
                possiblePhoto = bytes;

                final FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);

                cameraPreview.setForeground(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

                mCamera.stopPreview();
                pictureTaken = true;
            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);

        flipCameraImageView.setVisibility(View.GONE);
        loadImageView.setVisibility(View.GONE);
    }


    public void retakePhoto() {
        setResult(RESULT_START_OVER);
        finish();
    }


    public void acceptPhoto() {
        File picture = SaveFileHandler.getOutputMediaFile(getApplicationContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, pictureFilename);

        if (picture == null) {
            Log.e(Constants.LOG_TAG, "Could not create the media file");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(picture.getPath());

            if (loadImage) {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } else {
                fos.write(possiblePhoto);
            }

            fos.close();

            // I have to do this for amazon devices because the kindle does not save exif data on its own...
            // I also cannot do this for the other devices too because as you can see, the cases for ROTATION_90
            // and ROTATION_270 do not make sense. I think amazon made some sort of mistake with how they handle orientation.
            // ...or maybe I am doing this very wrong
            ExifInterface exifInterface = new ExifInterface(picture.getAbsolutePath());
            if (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1) == 0) {

                int orientation = getOrientation();
                switch (orientation) {
                    case Surface.ROTATION_0:
                        if (cameraId == defaultCameraId) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_VERTICAL));
                        }
                        break;
                    case Surface.ROTATION_90:
                        if (cameraId == defaultCameraId) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
                        }
                        break;
                    case Surface.ROTATION_180:
                        if (cameraId == defaultCameraId) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
                        }
                        break;
                    case Surface.ROTATION_270:
                        if (cameraId == defaultCameraId) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
                        }
                        break;
                    default:
                        exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        break;
                }
                exifInterface.saveAttributes();
            }

            Intent resultIntent = getIntent();
            resultIntent.putExtra(EXTRA_RESULT_PICTURE, picture);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
        }
        catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
        }
    }


    public void flipCamera() {
        if (!pictureTaken && !loadImage) {
            CameraActivity.cameraId = (cameraId == defaultCameraId) ? frontFacingCameraId : defaultCameraId;
            setResult(RESULT_START_OVER);
            finish();
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_camera;
    }

}
