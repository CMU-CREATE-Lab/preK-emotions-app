package org.cmucreatelab.android.flutterprek;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Custom class for the instance of Application used in the app, as specified in the AndroidManifest.xml file.
 *
 * DO NOT STORE GLOBAL VARIABLES HERE! The initial purpose of this class was to track the application entering the background/foreground.
 */
public class MindfulnestApplication extends Application implements LifecycleObserver {


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        Log.v(Constants.LOG_TAG, "MindfulnestApplication.onCreate");
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        HotfixManager hotfixManager = HotfixManager.getInstance(this);
        hotfixManager.checkAndRunHotfixes();
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
