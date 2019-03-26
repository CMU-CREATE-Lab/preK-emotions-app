package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.media.MediaPlayer;
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
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.ChooseEmotionFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.TalkAboutItFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
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

public class ChooseEmotionAndTalkAboutItActivity extends StudentSectionActivityWithTimeout implements TalkAboutItFragment.ActivityCallback {

    private ChooseEmotionFragment chooseEmotionFragment;
    private RecordFragment recordFragment;

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion, List<ItineraryItem> itineraryItems) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

            // track selection with GlobalHandler
            globalHandler.studentSectionNavigationHandler.emotionUuid = emotion.getUuid();

            // next activity
            globalHandler.getSessionTracker().onSelectedEmotion(ChooseEmotionAndTalkAboutItActivity.this, emotion, itineraryItems);
            Intent chooseCopingSkillActivity = globalHandler.getSessionTracker().getNextIntent(ChooseEmotionAndTalkAboutItActivity.this);

            // add custom message/background
            // TODO generate this elsewhere
            String message=null, backgroundColor=null, audioFile=null, somethingElseMessage="", somethingElseAudio="";
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
                try {
                    audioFile = jsonObject.getString("audio");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    somethingElseMessage = jsonObject.getString("somethingElseMessage");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    somethingElseAudio = jsonObject.getString("somethingElseAudio");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (message != null) {
                chooseCopingSkillActivity.putExtra(INTENT_MESSAGE, message);
            }
            if (backgroundColor != null) {
                chooseCopingSkillActivity.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
            } else {
                backgroundColor = "";
            }
            if (audioFile != null) {
                chooseCopingSkillActivity.putExtra(INTENT_AUDIO_FILE, audioFile);
            }

            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionBackgroundColor = backgroundColor;
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseMessage = somethingElseMessage;
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseAudio = somethingElseAudio;

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
        audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_how_are_you_feeling.wav");
        audioPlayer.playAudio();
    }


    private void playAudioRecord() {
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_record_press_button.wav");
        audioPlayer.playAudio();
    }


    private void playAudioWhichFeelingDidYouDescribe() {
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        // TODO replace audio file/path
        audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_how_are_you_feeling.wav");
        audioPlayer.playAudio();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imagePlayAudioView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionAndTalkAboutItActivity.this, emotions, listener));
                }
            });
        }

        this.recordFragment = (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.recordFragment);
        this.chooseEmotionFragment = (ChooseEmotionFragment) getSupportFragmentManager().findFragmentById(R.id.chooseEmotionFragment);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setFragment(TalkAboutItFragment.FragmentState.EMOTION_OR_RECORD);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_emotion_and_talk_about_it;
    }


    @Override
    public void setFragment(TalkAboutItFragment.FragmentState fragmentState) {
        recordFragment.displayFragment(fragmentState, this);
        chooseEmotionFragment.displayFragment(fragmentState, this);

        if (fragmentState == TalkAboutItFragment.FragmentState.RECORD) {
            // TODO handle playback first, then play this audio
            playAudioWhichFeelingDidYouDescribe();
        } else if (fragmentState == TalkAboutItFragment.FragmentState.EMOTION_OR_PLAYBACK) {
            playAudioRecord();
        } else {
            playAudioHowAreYouFeeling();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

}
