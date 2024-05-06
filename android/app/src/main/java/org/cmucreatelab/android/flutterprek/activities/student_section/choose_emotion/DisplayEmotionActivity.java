package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.check_in.DisplayEmotionCheckInActivity.INTENT_CHECKIN_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.check_in.DisplayEmotionCheckInActivity.INTENT_CHECKIN_MESSAGE;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithHeader;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DisplayEmotionActivity extends StudentSectionActivityWithHeader {

    public static String EMOTION_UUID = "emotion_uuid";
    public static String EMOTION_NAME = "emotion_name";
    public static String EMOTION_IMAGEFILE_UUID = "emotion_imagefile_uuid";
    public static String IT_ITEMS = "it_items";
    private String emotionUuid, emotionName, emotionImagefileUuid;
    private BackgroundTimer backgroundTimer;
    private static long millisecondsToWaitToStartNextActivity = 1200;
    private List<ItineraryItem> itineraryItems;

    // NOTE: does not extend WithTimeout because screen only lasts a few seconds


    /// NOTE: from EmotionIndexAdapter
    private void playAudioFeeling(String e) {
        //AudioPlayer audioPlayer = AudioPlayer.getInstance(activity.getApplicationContext());
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.stop();
        if (e.equals("Happy")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_happy.wav");
        } else if (e.equals("Sad")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_sad.wav");
        } else if (e.equals("Mad")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_mad.wav");
        } else if (e.equals("Scared")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_scared.wav");
        } else if (e.equals("Excited")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_excited.wav");
        }
        audioPlayer.playAudio();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.emotionUuid = getIntent().getStringExtra(EMOTION_UUID);
        this.emotionName = getIntent().getStringExtra(EMOTION_NAME);
        this.emotionImagefileUuid = getIntent().getStringExtra(EMOTION_IMAGEFILE_UUID);

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(emotionName);
        ImageView imageViewDisplayEmotion = findViewById(R.id.imageViewDisplayEmotion);
        AppDatabase.getInstance(getApplicationContext()).dbFileDAO().getDbFile(emotionImagefileUuid).observe(this, new Observer<DbFile>() {
            @Override
            public void onChanged(@Nullable DbFile dbFile) {
                Log.v(Constants.LOG_TAG, "trying to setImageViewWithDbFile with imagefileUuid="+ emotionImagefileUuid);
                Util.setImageViewWithDbFile(getApplicationContext(), imageViewDisplayEmotion, dbFile);
            }
        });

        AppDatabase.getInstance(getApplicationContext()).intermediateTablesDAO().getItineraryItemsForEmotion(emotionUuid).observe(DisplayEmotionActivity.this, new Observer<List<ItineraryItem>>() {
            @Override
            public void onChanged(@Nullable final List<ItineraryItem> itineraryItems) {
                DisplayEmotionActivity.this.itineraryItems = itineraryItems;
            }
        });

        this.backgroundTimer = new BackgroundTimer(millisecondsToWaitToStartNextActivity, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                Intent nextActivityIntent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntent(DisplayEmotionActivity.this);

                // add custom message/background
                /// TODO generate this elsewhere
                String message=null, backgroundColor=null, audioFile=null, somethingElseMessage="", somethingElseAudio="", checkInMessage=null, checkInAudio=null;
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

                    // something else
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

                    // check-in
                    try {
                        checkInMessage = jsonObject.getString("checkInMessage");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        checkInAudio = jsonObject.getString("checkInAudio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (message != null) {
                    nextActivityIntent.putExtra(INTENT_MESSAGE, message);
                }
                if (backgroundColor != null) {
                    nextActivityIntent.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
                } else {
                    backgroundColor = "";
                }
                if (audioFile != null) {
                    nextActivityIntent.putExtra(INTENT_AUDIO_FILE, audioFile);
                }
                if (checkInMessage != null) {
                    nextActivityIntent.putExtra(INTENT_CHECKIN_MESSAGE, checkInMessage);
                }
                if (checkInAudio != null) {
                    nextActivityIntent.putExtra(INTENT_CHECKIN_AUDIO_FILE, checkInAudio);
                }

                GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionBackgroundColor = backgroundColor;
                GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseMessage = somethingElseMessage;
                GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseAudio = somethingElseAudio;

                startActivity(nextActivityIntent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudioFeeling(emotionName);
        backgroundTimer.startTimer();
    }


    @Override
    protected void onPause() {
        super.onPause();
        backgroundTimer.stopTimer();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_display_emotion;
    }

}
