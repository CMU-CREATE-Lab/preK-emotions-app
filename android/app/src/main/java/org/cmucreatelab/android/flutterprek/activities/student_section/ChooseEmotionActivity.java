package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class ChooseEmotionActivity extends StudentSectionActivityWithHeader {

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            // TODO send to next activity
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).emotionDAO().getAllEmotions().observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionActivity.this, emotions, listener));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_emotion;
    }

}
