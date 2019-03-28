package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.ChooseEmotionFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments.TalkAboutItFragment;

public class ChooseEmotionAndTalkAboutItActivity extends ChooseEmotionAbstractActivity implements TalkAboutItFragment.ActivityCallback {

    private ChooseEmotionFragment chooseEmotionFragment;
    private RecordFragment recordFragment;

    private static final String
            filepathRecordPrompt = "etc/audio_prompts/audio_record_press_button.wav",
            filepathWhatFeelingPrompt = "etc/audio_prompts/audio_what_feeling.wav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.recordFragment = (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.recordFragment);
        this.chooseEmotionFragment = (ChooseEmotionFragment) getSupportFragmentManager().findFragmentById(R.id.chooseEmotionFragment);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setFragment(TalkAboutItFragment.FragmentState.EMOTION_OR_RECORD);
    }


    @Override
    protected void onPause() {
        // Avoid recording while in background
        recordFragment.onPauseActivity();
        super.onPause();
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
            playAudio(filepathRecordPrompt);
        } else if (fragmentState == TalkAboutItFragment.FragmentState.EMOTION_OR_PLAYBACK) {
            chooseEmotionFragment.addRecordedAudio(false);
            playAudio(filepathWhatFeelingPrompt);
        } else {
            playAudioHowAreYouFeeling();
        }
        restartOverlayTimers();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }

}
