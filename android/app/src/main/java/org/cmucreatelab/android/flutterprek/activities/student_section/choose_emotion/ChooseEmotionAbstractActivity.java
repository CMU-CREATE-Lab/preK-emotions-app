package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithTimeout;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.List;

public abstract class ChooseEmotionAbstractActivity extends StudentSectionActivityWithTimeout {

    private static final String filepathHowAreYouFeelingPrompt = "etc/audio_prompts/audio_how_are_you_feeling.wav";

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion, List<ItineraryItem> itineraryItems) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            if (!activityShouldHandleOnClickEvents()) {
                Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                return;
            }
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

            // track selection with GlobalHandler
            globalHandler.studentSectionNavigationHandler.emotionUuid = emotion.getUuid();

            // next activity
            globalHandler.getSessionTracker().onSelectedEmotion(ChooseEmotionAbstractActivity.this, emotion, itineraryItems);
            //Intent nextActivityIntent = globalHandler.getSessionTracker().getNextIntent(ChooseEmotionAbstractActivity.this);
            Intent nextActivityIntent = new Intent(ChooseEmotionAbstractActivity.this, DisplayEmotionActivity.class);
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_UUID, emotion.getUuid());
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_NAME, emotion.getName());
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_IMAGEFILE_UUID, emotion.getImageFileUuid());

            startActivity(nextActivityIntent);
        }
    };


    private LiveData<List<Emotion>> getLiveDataFromQuery(String classroomUuid, String studentUuid) {
        ArrayList<String> uuids = new ArrayList<>();
        if (!classroomUuid.isEmpty()) uuids.add(classroomUuid);
        if (!studentUuid.isEmpty()) uuids.add(studentUuid);
        return AppDatabase.getInstance(this).emotionDAO().getEmotionsOwnedBy(uuids);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imagePlayAudioView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activityShouldHandleOnClickEvents()) {
                    Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                    return;
                }
                //playAudioHowAreYouFeeling();
                // NOTE: user interaction cancels reprompt timer
                cancelTimerToReprompt();
                playAudio(filepathHowAreYouFeelingPrompt);
            }
        });

        GlobalHandler globalHandler = GlobalHandler.getInstance(this);
        if (!globalHandler.currentSessionIsActive()) {
            Log.e(Constants.LOG_TAG, "ChooseEmotionActivity.onCreate while current session is not active; ending session");
            globalHandler.endCurrentSession(this);
        } else {
            Student student = globalHandler.getSessionTracker().getStudent();

            LiveData<List<Emotion>> liveData = getLiveDataFromQuery(student.getClassroomUuid(), student.getUuid());
            liveData.observe(this, new Observer<List<Emotion>>() {
                @Override
                public void onChanged(@Nullable List<Emotion> emotions) {
                    GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                    emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionAbstractActivity.this, emotions, listener));
                }
            });
        }
    }


    public void playAudioHowAreYouFeeling() {
        startTimerToRepromptAndPlayAudio(filepathHowAreYouFeelingPrompt);
    }

}
