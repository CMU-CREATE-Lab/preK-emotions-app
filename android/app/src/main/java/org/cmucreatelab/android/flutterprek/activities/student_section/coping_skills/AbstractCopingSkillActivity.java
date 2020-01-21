package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public abstract class AbstractCopingSkillActivity extends AbstractActivity {

    private int itineraryIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

        // close
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.LOG_TAG, "clicked buttonClose; now finishing activity");
                finish();
            }
        });
    }


    /**
     * When finishing Coping Skill activities, begin the follow-up coping skill prompts.
     */
    @Override
    public void finish() {
        Intent postCopingActivity = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(postCopingActivity);
    }


    @Override
    public boolean activityUsesDelayedOnClickHandler() {
        return false;
    }

}
