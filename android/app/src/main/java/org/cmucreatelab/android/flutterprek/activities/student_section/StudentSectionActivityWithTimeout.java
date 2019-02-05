package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.os.Bundle;

public abstract class StudentSectionActivityWithTimeout extends StudentSectionActivityWithHeader {

    private StudentSectionTimeoutOverlay timeoutOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeoutOverlay = new StudentSectionTimeoutOverlay(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        timeoutOverlay.onPauseActivity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        timeoutOverlay.onResumeActivity();
    }


    public void releaseOverlayTimers() {
        timeoutOverlay.releaseTimers();
    }

}
