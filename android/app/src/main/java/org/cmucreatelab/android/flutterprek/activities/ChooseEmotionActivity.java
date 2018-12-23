package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class ChooseEmotionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_emotion);

        // TODO pass in information on what to display (probably classroom ID)
        AppDatabase.getInstance(this).emotionDAO().getAllEmotions().observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionIndexAdapter(getApplicationContext(), emotions));
            }
        });
    }

}
