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

public class Util {


    public static void setImageViewWithAsset(Context appContext, ImageView imageView, String assetPath) {
        try {
            InputStream assetInStream = appContext.getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(assetInStream);

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to set ImageView with asset="+assetPath);
            e.printStackTrace();
        }
    }


    public static Long getCurrentTimestamp() {
        return DateConverter.toTimestamp(new Date());
    }

}
