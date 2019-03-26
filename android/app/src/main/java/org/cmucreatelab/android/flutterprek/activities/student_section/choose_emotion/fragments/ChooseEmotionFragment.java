package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.fragments;

import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;

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
            ((TextView) fragmentView.findViewById(R.id.textTitle)).setText(R.string.which_emotion_prompt);
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
                // TODO playback audio
            }
        });
    }

}
