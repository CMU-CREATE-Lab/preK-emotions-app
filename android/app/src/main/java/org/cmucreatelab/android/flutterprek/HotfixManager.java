package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class HotfixManager implements Hotfix.Listener {

    public static final String h20221206_updateResForStudentImages = "student_img_res_update";

    //private final Context appContext;
    private final MindfulnestApplication mindfulnestApplication;
    private final String[] applicationHotfixes = {
            h20221206_updateResForStudentImages
    };


    private HotfixManager(MindfulnestApplication mindfulnestApplication) {
        this.mindfulnestApplication = mindfulnestApplication;
    }


    // Singleton Implementation


    private static HotfixManager classInstance;


    public static synchronized HotfixManager getInstance(MindfulnestApplication mindfulnestApplication) {
        if (classInstance == null) {
            classInstance = new HotfixManager(mindfulnestApplication);
        }
        return classInstance;
    }


    // public methods


    public synchronized void updateHotfixCollectionWithIdentifier(String hotfixId) {
        SharedPreferences sp = GlobalHandler.getSharedPreferences(mindfulnestApplication.getApplicationContext());
        Set<String> hotfixCollection = sp.getStringSet(Constants.PreferencesKeys.hotfixCollection, Collections.<String>emptySet());

        // (See https://developer.android.com/reference/android/content/SharedPreferences#getStringSet(java.lang.String,%20java.util.Set%3Cjava.lang.String%3E)
        // "Note that you must not modify the set instance returned by this call"
        HashSet<String> mutableSet = new HashSet<>(hotfixCollection);
        mutableSet.add(hotfixId);
        sp.edit().putStringSet(Constants.PreferencesKeys.hotfixCollection, mutableSet).apply();
    }


    /**
     * This method checks the hotfixCollection preference in SharedPreferences against all
     * String objects stored in applicationHotfixes and, for each String that is not contained
     * within hotfixCollection, performs the operations related to the hotfix and then adds the
     * String to hotfixCollection.
     *
     * Called by the application's onCreate() method and is used to perform application updates,
     * typically relating to persistence data (e.g. SharedPreferences, RoomDatabase).
     */
    public void checkAndRunHotfixes() {
        SharedPreferences sp = GlobalHandler.getSharedPreferences(mindfulnestApplication.getApplicationContext());
        final Set<String> hotfixCollection = sp.getStringSet(Constants.PreferencesKeys.hotfixCollection, Collections.<String>emptySet());

        for (String hotfixId: applicationHotfixes) {
            if (hotfixCollection.contains(hotfixId)) {
                Log.v(Constants.LOG_TAG, String.format("hotfixCollection contains id=%s", hotfixId));
            } else {
                Log.i(Constants.LOG_TAG, String.format("hotfixCollection does not contain id=%s; creating hotfix instance", hotfixId));
                Hotfix hotfix = Hotfix.createFromIdentifier(mindfulnestApplication, this, hotfixId);

                switch (hotfix.doHotfix()) {
                    case SUCCESSFUL:
                        Log.i(Constants.LOG_TAG, String.format("Hotfix id=%s was successful. Now adding to hotfixCollection.", hotfixId));
                        updateHotfixCollectionWithIdentifier(hotfixId);
                        break;
                    case PROCESSING:
                        Log.i(Constants.LOG_TAG, String.format("Hotfix id=%s marked as processing; will wait for onStatusUpdated in listener.", hotfixId));
                        break;
                    case FAILED:
                    default:
                        Log.i(Constants.LOG_TAG, String.format("Failed to perform Hotfix id=%s", hotfixId));
                }
            }
        }
    }


    @Override
    public void onStatusUpdated(String identifier, Hotfix.Status status) {
        if (status != Hotfix.Status.SUCCESSFUL) {
            Log.e(Constants.LOG_TAG, String.format("(onStatusUpdated) Hotfix id=%s was not successful.", identifier));
            return;
        }
        Log.i(Constants.LOG_TAG, String.format("(onStatusUpdated) Hotfix id=%s was successful. Now adding to hotfixCollection.", identifier));
        updateHotfixCollectionWithIdentifier(identifier);
    }

}
