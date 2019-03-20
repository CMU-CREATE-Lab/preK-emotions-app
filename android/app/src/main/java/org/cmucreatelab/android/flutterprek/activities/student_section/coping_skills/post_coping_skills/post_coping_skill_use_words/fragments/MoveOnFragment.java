package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.fragments.AbstractFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.MoveOnUseWordsActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.RecordUseWordsActivity;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import java.io.File;

public class MoveOnFragment extends AbstractFragment {

    public MoveOnUseWordsActivity activity;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToNextPostCopingSkillActivity(RecordUseWordsActivity.class);
            }
        });
        view.findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToNextPostCopingSkillActivity(RejoinFriendsActivity.class);
            }
        });
        view.findViewById(R.id.layoutPlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordedAudio(true);
            }
        });
    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout._coping_skill__fragment_move_on;
    }


    public void addRecordedAudio(boolean playback) {
        // release timers if we are playing back audio
        activity.releaseOverlayTimers();

        File recordedAudioFile = GlobalHandler.getInstance(getActivity().getApplicationContext()).studentSectionNavigationHandler.recordedAudioFile;
        if (recordedAudioFile != null) {
            Log.d(Constants.LOG_TAG, "Added audio to play: " + recordedAudioFile.getAbsolutePath());
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getActivity().getApplicationContext());
            if (playback) {
                audioPlayer.stop();
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath(), activity);
                audioPlayer.playAudio();
            } else {
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath());
            }
        }
    }


    public void displayFragment(MoveOnUseWordsActivity activity) {
        this.activity = activity;
        getView().setVisibility(View.VISIBLE);
    }

}
