package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

public abstract class HighlightsDesignActivityWithAsyncScrollViewStub extends HighlightsDesignActivityWithHeaderAndDrawer {


    private void viewStub() {
        ViewStub stub = findViewById(getResourceIdForScrollViewStub());

        // Postpone inflation to after the first frame
        stub.post(() -> {
            View inflated = stub.inflate();

            new Thread(() -> {
                runOnUiThread(() -> {
                    ProgressBar progressBar = findViewById(getResourceIdForProgressBar());
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
        viewStub();
    }

    public abstract void onAsyncScrollViewLoaded();

    public abstract int getResourceIdForProgressBar();

    public abstract int getResourceIdForScrollViewStub();

}
