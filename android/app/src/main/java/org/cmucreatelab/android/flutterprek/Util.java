package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import static org.cmucreatelab.android.flutterprek.Constants.LOG_TAG;

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

}
