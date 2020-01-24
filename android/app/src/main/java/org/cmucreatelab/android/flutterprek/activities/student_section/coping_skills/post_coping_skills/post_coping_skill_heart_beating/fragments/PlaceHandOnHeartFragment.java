package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.VideoView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.video.VideoPlayer;

import java.io.IOException;


public class PlaceHandOnHeartFragment extends HeartBeatingFragment  {

    public String getFilePathForVideo() {
        return "android.resource://" + getActivity().getPackageName() + "/" + R.raw.hand_on_heart;
    }

    private final MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            getActivityCallback().setFragment(FragmentState.HOW_FAST_IS_HEART_BEATING);
        }
    };

    @Override
    public int getInflatedLayoutResource() {
        return R.layout._coping_skill__fragment_place_hand_on_heart;
    }

    @Override
    public void initializeFragment() {
        View fragmentView = getFragmentView();
        VideoView videoView = fragmentView.findViewById(R.id.videoView);

        VideoPlayer videoPlayer = VideoPlayer.getInstance(getActivity().getApplicationContext());
        videoPlayer.prepareViewWithVideo(getActivity(), videoView, getFilePathForVideo());
        videoPlayer.playVideo(false,listener);
    }


}
