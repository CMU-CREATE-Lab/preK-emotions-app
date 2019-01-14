package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.StudentSectionNavigationHandler;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;

public class ChooseEmotionActivity extends StudentSectionActivityWithHeader {

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion, List<ItineraryItem> itineraryItems) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionUuid = emotion.getUuid();
            // send to next activity
            Intent chooseCopingSkillActivity = new Intent(ChooseEmotionActivity.this, ChooseCopingSkillActivity.class);

            // add custom message/background
            String message="",backgroundColor="#ffffff";
            for (ItineraryItem item: itineraryItems) {
                // TODO check for proper capabilityId?
                Log.i(Constants.LOG_TAG, "capabilityParams=" + item.getCapabilityParameters().toString());
                JSONObject jsonObject = item.getCapabilityParameters();
                try {
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    backgroundColor = jsonObject.getString("background-color");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            chooseCopingSkillActivity.putExtra(INTENT_MESSAGE,message);
            chooseCopingSkillActivity.putExtra(INTENT_BACKGROUND_COLOR,backgroundColor);

            startActivity(chooseCopingSkillActivity);
        }
    };


    private LiveData<List<Emotion>> getLiveDataFromQuery(String classroomUuid, String studentUuid) {
        ArrayList<String> uuids = new ArrayList<>();
        if (!classroomUuid.isEmpty()) uuids.add(classroomUuid);
        if (!studentUuid.isEmpty()) uuids.add(studentUuid);
        return AppDatabase.getInstance(this).emotionDAO().getEmotionsOwnedBy(uuids);
    }


    private void playAudioHowAreYouFeeling() {
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.addAudio("etc/audio_prompts/audio_how_are_you_feeling.wav");
        audioPlayer.playAudio();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StudentSectionNavigationHandler navigationHandler = GlobalHandler.getInstance(this).studentSectionNavigationHandler;
        LiveData<List<Emotion>> liveData = getLiveDataFromQuery(navigationHandler.classroomUuid, navigationHandler.studentUuid);
        liveData.observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionActivity.this, emotions, listener));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudioHowAreYouFeeling();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_emotion;
    }

}
