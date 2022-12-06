package org.cmucreatelab.android.flutterprek;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.io.File;
import java.util.List;

public abstract class Hotfix {

    public enum Status {
        SUCCESSFUL,
        FAILED,
        PROCESSING
    }

    public interface Listener {
        void onStatusUpdated(String identifier, Status status);
    }

    // Hotfix implementations

    /**
     * Updates the resolution of all Student picture files in the DB (see Util.defaultReformatImageFile).
     */
    public static class Hotfix_h20221206_updateResForStudentImages extends Hotfix {

        private static final String identifier = HotfixManager.h20221206_updateResForStudentImages;
        private MindfulnestApplication mindfulnestApplication;
        private Listener listener;

        public Hotfix_h20221206_updateResForStudentImages(MindfulnestApplication mindfulnestApplication, Listener listener) {
            this.mindfulnestApplication = mindfulnestApplication;
            this.listener =  listener;
        }

        @Override
        public Status doHotfix() {
            AppDatabase.getInstance(mindfulnestApplication.getApplicationContext()).dbFileDAO().getAllStudentPictureDbFiles().observe(ProcessLifecycleOwner.get(), new Observer<List<DbFile>>() {
                @Override
                public void onChanged(@Nullable List<DbFile> dbFiles) {
                    for (DbFile dbFile: dbFiles) {
                        Util.defaultReformatImageFile(new File(dbFile.getFilePath()));
                    }
                    listener.onStatusUpdated(identifier, Status.SUCCESSFUL);
                }
            });

            return Status.PROCESSING;
        }
    }

    public static final Hotfix emptyHotfix = new Hotfix() {
        @Override
        public Status doHotfix() {
            Log.v(Constants.LOG_TAG, "emptyHotfix does nothing; returning false");
            return Status.FAILED;
        }
    };


    // public methods


    public static @NonNull Hotfix createFromIdentifier(MindfulnestApplication mindfulnestApplication, Listener listener, String id) {
        Log.v(Constants.LOG_TAG, String.format("Hotfix.createFromIdentifier: id=%s", id));
        switch (id) {
            case HotfixManager.h20221206_updateResForStudentImages:
                Log.v(Constants.LOG_TAG, "Hotfix.createFromIdentifier: constructing class instance Hotfix_h20221206_updateResForStudentImages");
                return new Hotfix_h20221206_updateResForStudentImages(mindfulnestApplication, listener);
            default:
                Log.w(Constants.LOG_TAG, String.format("Hotfix.createFromIdentifier: Could not find hotfix with identifier '%s', returning emptyHotfix instance.", id));
        }
        return emptyHotfix;
    }


    /**
     * Abstract method for operations related to the hotfix.
     *
     * @return true if the hotfix operation is successful, false otherwise.
     */
    public abstract Status doHotfix();

}
