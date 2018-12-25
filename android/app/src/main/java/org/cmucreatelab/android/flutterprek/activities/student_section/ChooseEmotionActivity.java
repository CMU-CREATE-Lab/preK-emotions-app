package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class ChooseEmotionActivity extends StudentSectionActivityWithHeader {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).emotionDAO().getAllEmotions().observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionActivity.this, emotions));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_choose_emotion;
    }

}
