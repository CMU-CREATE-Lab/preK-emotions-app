package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

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


    public static void setImageViewWithDbFile(Context appContext, ImageView imageView, DbFile dbFile) {
        String fileType = dbFile.getFileType();
        if (fileType.equals(DbFile.getStringForFileType(DbFile.FILE_TYPE.ASSET_IMAGE))) {
            setImageViewWithAsset(appContext, imageView, dbFile.getFilePath());
        } else if (fileType.equals(DbFile.getStringForFileType(DbFile.FILE_TYPE.FILEPATH))) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(dbFile.getFilePath()));
        } else {
            Log.e(LOG_TAG, String.format("failed to set ImageView: unknown filetype '%s'", fileType));
        }
    }


    public static Long getCurrentTimestamp() {
        return DateConverter.toTimestamp(new Date());
    }

}
