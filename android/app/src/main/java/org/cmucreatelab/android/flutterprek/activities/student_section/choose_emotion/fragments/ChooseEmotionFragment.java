package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import java.io.File;

public class ChooseEmotionFragment extends TalkAboutItFragment {


    @Override
    public int getInflatedLayoutResource() {
        return R.layout._student_section__fragment_choose_emotion_with_options;
    }


    @Override
    public void displayFragment(FragmentState fragmentStateToDisplay, ActivityCallback activity) {
        super.displayFragment(fragmentStateToDisplay, activity);
        boolean display = (fragmentStateToDisplay == FragmentState.EMOTION_OR_RECORD || fragmentStateToDisplay == FragmentState.EMOTION_OR_PLAYBACK);
        getFragmentView().setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            initializeFragment();
        }
    }


    @Override
    public void initializeFragment() {
        FragmentState fragmentState = getCurrentFragmentState();
        View fragmentView = getFragmentView();

        if (fragmentState == FragmentState.EMOTION_OR_PLAYBACK) {
            ((TextView) fragmentView.findViewById(R.id.textTitle)).setText(R.string.what_feeling_prompt);
            fragmentView.findViewById(R.id.layoutTalkAboutIt).setVisibility(View.GONE);
            fragmentView.findViewById(R.id.imageViewPlayButton).setVisibility(View.VISIBLE);
        } else {
            ((TextView) fragmentView.findViewById(R.id.textTitle)).setText(R.string.choose_emotion_prompt);
            fragmentView.findViewById(R.id.layoutTalkAboutIt).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.imageViewPlayButton).setVisibility(View.GONE);
        }

        fragmentView.findViewById(R.id.layoutTalkAboutIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityCallback().setFragment(FragmentState.RECORD);
            }
        });

        fragmentView.findViewById(R.id.imageViewPlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordedAudio(true);
            }
        });
    }


    public void addRecordedAudio(boolean playback) {
        TalkAboutItFragment.ActivityCallback activityCallback = getActivityCallback();

        // release timers if we are playing back audio
        activityCallback.releaseOverlayTimers();

        File recordedAudioFile = GlobalHandler.getInstance(getActivity().getApplicationContext()).studentSectionNavigationHandler.recordedAudioFile;
        if (recordedAudioFile != null) {
            Log.d(Constants.LOG_TAG, "Added audio to play: " + recordedAudioFile.getAbsolutePath());
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getActivity().getApplicationContext());
            if (playback) {
                audioPlayer.stop();
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath(), activityCallback);
                audioPlayer.playAudio();
            } else {
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath());
            }
        }
    }

}
