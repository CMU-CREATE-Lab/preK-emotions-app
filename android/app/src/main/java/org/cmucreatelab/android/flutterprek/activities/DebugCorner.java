package org.cmucreatelab.android.flutterprek.activities;

import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BuildConfig;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

/**
 * Displays build variant and git commit hash in bottom-left of screens.
 */
public class DebugCorner {

    private AbstractActivity activity;


    public DebugCorner(AbstractActivity activity) {
        this.activity = activity;

        if (BuildConfig.DEBUG) {
            String debugText = (Constants.HARDCODED_VALUES == null) ? "" : Constants.HARDCODED_VALUES.getStationName();
            ((TextView)activity.findViewById(R.id.textDebugTag)).setText(debugText);
        } else {
            activity.findViewById(R.id.debugCorner).setVisibility(View.GONE);
        }
    }

}
