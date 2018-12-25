package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class DrawerTeacherMainFragment extends Fragment {

    public enum Section {
        ACTIVE_CLASS,
        CLASSES,
        COPING_SKILLS,
        EMOTIONS
    }


    private void setHighlighted(Section section, boolean isHighlighted) {
        View highlightView;
        TextView textView;
        switch (section) {
            case ACTIVE_CLASS:
                highlightView = getView().findViewById(R.id.highlightViewActiveClass);
                textView = getView().findViewById(R.id.textViewActiveClass);
                break;
            case CLASSES:
                highlightView = getView().findViewById(R.id.highlightViewClasses);
                textView = getView().findViewById(R.id.textViewClasses);
                break;
            case COPING_SKILLS:
                highlightView = getView().findViewById(R.id.highlightViewCopingSkills);
                textView = getView().findViewById(R.id.textViewCopingSkills);
                break;
            case EMOTIONS:
                highlightView = getView().findViewById(R.id.highlightViewEmotions);
                textView = getView().findViewById(R.id.textViewEmotions);
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not recognize section=" + section.name());
                return;
        }
        highlightView.setVisibility(isHighlighted ? View.VISIBLE : View.INVISIBLE);
        textView.setTextAppearance(getContext(), isHighlighted ? R.style.textBold : R.style.textNormal);
    }


    public void setCurrentSection(Section section) {
        for (Section mSection: Section.values()) {
            setHighlighted(mSection, mSection == section);
        }
    }


    /** Get the resource ("R.layout." ...) for the extended Fragment class. */
    @LayoutRes
    public int getInflatedLayoutResource() {
        return R.layout.fragment_drawer_teacher_main;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO actions when SECTION is clicked
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getInflatedLayoutResource(), container, false);
    }

}
