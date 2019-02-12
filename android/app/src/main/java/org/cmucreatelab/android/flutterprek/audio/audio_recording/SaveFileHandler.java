package org.cmucreatelab.android.flutterprek.audio.audio_recording;

import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;

import java.io.File;

/**
 * Created by Steve on 3/18/2016.
 * Modified by tasota on 1/21/2019.
 *
 * SaveFileHandler
 *
 * Used to help create and save files of various types.
 */
public class SaveFileHandler {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_AUDIO = 2;


    public static File getOutputMediaFile(Context context, int type, GlobalHandler globalHandler) {
        File mediaStorageDir = new File("","");
        File mediaFile;


        //String name = globalHandler.sessionHandler.getMessageSender().getName();
        String name = globalHandler.studentSectionNavigationHandler.studentUuid;
        if (name.contains(" ")) {
            name = name.replace(' ', '_');
        }
//        // external file storage
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name);
//        } else if (type == MEDIA_TYPE_AUDIO) {
//            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);
//        }
        // internal file storage
        mediaStorageDir = new File(context.getFilesDir(), name);

        if (! mediaStorageDir.exists()) {
            if (! mediaStorageDir.mkdirs()) {
                Log.d(Constants.LOG_TAG, "failed to create directory");
                return null;
            }
        }

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + name + ".jpeg");
            mediaFile.delete();
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + name + ".jpeg");
        } else if (type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "AUDIO_" + name + ".amr-nb");
            mediaFile.delete();
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "AUDIO_" + name + ".amr-nb");
        } else {
            return null;
        }

        return mediaFile;
    }

}
