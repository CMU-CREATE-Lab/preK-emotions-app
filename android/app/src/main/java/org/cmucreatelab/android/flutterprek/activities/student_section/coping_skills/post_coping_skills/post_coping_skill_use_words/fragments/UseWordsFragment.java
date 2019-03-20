package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.cmucreatelab.android.flutterprek.activities.fragments.AbstractFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.RecordUseWordsActivity;

public abstract class UseWordsFragment extends AbstractFragment {

    private RecordUseWordsActivity activity;
    private @NonNull View fragmentView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentView = view;
        fragmentView.setVisibility(View.GONE);
    }


    public void displayFragment(boolean display, RecordUseWordsActivity activity) {
        this.activity = activity;
        fragmentView.setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            initializeFragment();
        }
    }


    public RecordUseWordsActivity getPostCopingSkillActivity() {
        return activity;
    }


    @NonNull
    public View getFragmentView() {
        return fragmentView;
    }


    /** this method runs to initialize the displayed fragment (example: visibility on views, class logic). */
    public abstract void initializeFragment();

}
