package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.cmucreatelab.android.flutterprek.Constants.LOG_TAG;

import org.cmucreatelab.android.flutterprek.database.DateConverter;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

public class Util {


    private static void setImageViewWithAsset(Context appContext, ImageView imageView, String assetPath) {
        try {
            InputStream assetInStream = appContext.getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(assetInStream);

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to set ImageView with asset="+assetPath);
            e.printStackTrace();
        }
    }


    /**
     * Edits the resolution and compression quality for a JPEG image file. Note that this method
     * will modify the contents of the picture parameter.
     *
     * This method was designed to avoid trying to render large resolution images files in a list
     * view, which causes slow scrolling when trying to load multiple images at once. To avoid this,
     * it is recommended to use a small maximumWidth value (most ImageView instances are around 200
     * dpi) whereas the jpegQuality value is less important (use 100 to avoid quality loss).
     *
     * @param picture The image file to be modified.
     * @param maximumWidth Specifies the width for the resolution of the modified image.
     * @param jpegQuality Compression value for the modified JPEG image. Use 100 to avoid quality loss
     * @param usesBilinearFilter see documentation for Bitmap.createScaledBitmap.
     */
    public static void reformatImageFile(File picture, int maximumWidth, int jpegQuality, boolean usesBilinearFilter) {
        Bitmap image = BitmapFactory.decodeFile(picture.getPath());

        try {
            int originalWidth = image.getWidth();
            int height = (int) ((originalWidth == 0) ? 0.0 : ((double)maximumWidth / (double)originalWidth ) * image.getHeight());

            if (maximumWidth == 0 || height == 0) {
                Log.w(Constants.LOG_TAG, "Ignoring scaled image where width/height is zero.");
                return;
            }

            FileOutputStream fos = new FileOutputStream(picture);
            Bitmap.createScaledBitmap(image, maximumWidth, height, usesBilinearFilter).compress(Bitmap.CompressFormat.JPEG, jpegQuality, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper method to perform the default application image reformatting, where width is 200
     * pixels.
     *
     * @param picture The image file to be modified.
     */
    public static void defaultReformatImageFile(File picture) {
        Util.reformatImageFile(picture, 200, 100, true);
    }


    public static void setImageViewWithDbFile(Context appContext, ImageView imageView, DbFile dbFile) {
        String fileType = dbFile.getFileType();
        if (fileType.equals(DbFile.getStringForFileType(DbFile.DbFileType.ASSET_IMAGE))) {
            setImageViewWithAsset(appContext, imageView, dbFile.getFilePath());
        } else if (fileType.equals(DbFile.getStringForFileType(DbFile.DbFileType.FILEPATH))) {
            Bitmap bitmap = BitmapFactory.decodeFile(dbFile.getFilePath());
            imageView.setImageBitmap(bitmap);
            Log.v(LOG_TAG, String.format("setImageViewWithDbFile: imageView= %d x %d && image = %d x %d", imageView.getWidth(), imageView.getHeight(), bitmap.getWidth(), bitmap.getHeight() ));
        } else {
            Log.e(LOG_TAG, String.format("failed to set ImageView: unknown filetype '%s'", fileType));
        }
    }


    public static Long getCurrentTimestamp() {
        return DateConverter.toTimestamp(new Date());
    }

}
