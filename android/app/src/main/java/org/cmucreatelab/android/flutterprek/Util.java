package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class Util {

    private static final String LOG_TAG = "flutterprek";


    public static void setImageViewWithAsset(Context appContext, ImageView imageView, String assetPath) {
        try {
            InputStream assetInStream = appContext.getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(assetInStream);

            imageView.setImageBitmap(bitmap);
            // TODO really just demos for the emotion
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to set ImageView with asset="+assetPath);
            e.printStackTrace();
        }
    }

}
