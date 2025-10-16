package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

public abstract class HighlightsDesignActivityWithAsyncScrollViewStub extends HighlightsDesignActivityWithHeaderAndDrawer {

    private ViewStub stub;
    private ProgressBar progressBar;


    private void inflateScrollViewStub() {
        // Postpone inflation to after the first frame
        stub.post(() -> {
            View inflated = stub.inflate();

            new Thread(() -> {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    ViewGroup vg = (ViewGroup) progressBar.getParent();
                    if (vg != null) {
                        vg.removeView(progressBar);
                    }

                    // callback
                    onAsyncScrollViewLoaded();
                });
            }).start();
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.stub = findViewById(getResourceIdForScrollViewStub());
        this.progressBar = findViewById(getResourceIdForProgressBar());

        inflateScrollViewStub();
    }

    public abstract int getResourceIdForProgressBar();

    public abstract int getResourceIdForScrollViewStub();

    /**
     * Implement this as if it were code to be placed in onCreate() for finding/populating views.
     */
    public abstract void onAsyncScrollViewLoaded();

}
