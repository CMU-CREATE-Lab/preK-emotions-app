package org.cmucreatelab.android.flutterprek;

import android.app.Application;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseClassroomActivity;

/**
 * Custom class for the instance of Application used in the app, as specified in the AndroidManifest.xml file.
 *
 * DO NOT STORE GLOBAL VARIABLES HERE! The initial purpose of this class was to track the application entering the background/foreground.
 */
public class MindfulnestApplication extends Application implements LifecycleObserver {

    // Somewhat similar to "Singleton Implementation", this provides static access to the Application Context throughout code.
    // Some considerations (AI-generated):
    /**
     * WARNING: This static reference to the Application Context is provided for convenience,
     * but should be used with care.
     *
     * ✅ SAFE USE CASES:
     * - Accessing app-level resources, SharedPreferences, system services, etc.
     * - Initializing singletons or utility classes that require a context
     *
     * ❌ DO NOT USE THIS CONTEXT FOR:
     * - UI-related operations (e.g., creating Views, inflating layouts with theme)
     * - Starting Activities or showing Dialogs
     * - Anything that requires an Activity or View context
     *
     * ⚠️ NEVER store Activity or View contexts statically — this causes memory leaks.
     * Always prefer passing context explicitly, or use dependency injection (e.g., Hilt)
     * for more testable and maintainable architecture.
     */
    private static MindfulnestApplication classInstance;


    public static Context getInstanceOfApplicationContext() {
        return classInstance.getApplicationContext();
    }


    private void handleUncaughtException(Thread thread, Throwable throwable) {
        // NOTE: desired implementation is a combo of a few stack overflows:
        // - (set default uncaught exception handler) https://stackoverflow.com/questions/27829955/android-handle-application-crash-and-start-a-particular-activity
        // - (this one mentions intent flags, which actually provide desired functionality) https://stackoverflow.com/questions/37135787/ereza-customactivityoncrash-startactivity-with-intent
        // Mentions a library for custom activity but issues building with AndroidX
        Intent intent = new Intent(getApplicationContext(), ChooseClassroomActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        System.exit(2);
    }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onCreate");
        super.onCreate();
        classInstance = this;

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        HotfixManager hotfixManager = HotfixManager.getInstance(this);
        hotfixManager.checkAndRunHotfixes();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                handleUncaughtException(thread, throwable);
            }
        });
    }


    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onLowMemory");
        super.onLowMemory();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onEnterForeground");
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.onEnterForeground();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onEnterBackground");
    }

}



