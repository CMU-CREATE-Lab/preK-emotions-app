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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.check_in.DisplayEmotionCheckInActivity.INTENT_CHECKIN_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.check_in.DisplayEmotionCheckInActivity.INTENT_CHECKIN_MESSAGE;

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

//            // add custom message/background
//            /// TODO generate this elsewhere
//            String message=null, backgroundColor=null, audioFile=null, somethingElseMessage="", somethingElseAudio="", checkInMessage=null, checkInAudio=null;
//            for (ItineraryItem item: itineraryItems) {
//                // TODO check for proper capabilityId?
//                Log.i(Constants.LOG_TAG, "capabilityParams=" + item.getCapabilityParameters().toString());
//                JSONObject jsonObject = item.getCapabilityParameters();
//                try {
//                    message = jsonObject.getString("message");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    backgroundColor = jsonObject.getString("background-color");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    audioFile = jsonObject.getString("audio");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // something else
//                try {
//                    somethingElseMessage = jsonObject.getString("somethingElseMessage");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    somethingElseAudio = jsonObject.getString("somethingElseAudio");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // check-in
//                try {
//                    checkInMessage = jsonObject.getString("checkInMessage");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    checkInAudio = jsonObject.getString("checkInAudio");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (message != null) {
//                nextActivityIntent.putExtra(INTENT_MESSAGE, message);
//            }
//            if (backgroundColor != null) {
//                nextActivityIntent.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
//            } else {
//                backgroundColor = "";
//            }
//            if (audioFile != null) {
//                nextActivityIntent.putExtra(INTENT_AUDIO_FILE, audioFile);
//            }
//            if (checkInMessage != null) {
//                nextActivityIntent.putExtra(INTENT_CHECKIN_MESSAGE, checkInMessage);
//            }
//            if (checkInAudio != null) {
//                nextActivityIntent.putExtra(INTENT_CHECKIN_AUDIO_FILE, checkInAudio);
//            }
//
//            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionBackgroundColor = backgroundColor;
//            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseMessage = somethingElseMessage;
//            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseAudio = somethingElseAudio;

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
                playAudioHowAreYouFeeling();
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
        playAudio(filepathHowAreYouFeelingPrompt);
    }

}
