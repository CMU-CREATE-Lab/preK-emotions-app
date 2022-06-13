package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStudentsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStatsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;

/**
 * Fragment for the Drawer on the left-hand side of the screen in the teacher section of the app.
 */
public class DrawerTeacherClassroomFragment extends AbstractFragment {

    public enum Section {
        STATS,
        STUDENTS,
        COPING_SKILLS,
        EMOTIONS
    }

    private TextView textViewClassroomName;


    private void setHighlighted(Section section, boolean isHighlighted) {
        View highlightView;
        TextView textView;
        switch (section) {
            case STATS:
                highlightView = getView().findViewById(R.id.highlightViewStats);
                textView = getView().findViewById(R.id.textViewStats);
                break;
            case STUDENTS:
                highlightView = getView().findViewById(R.id.highlightViewStudents);
                textView = getView().findViewById(R.id.textViewStudents);
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


    private void startClassroomIndexActivity() {
        Intent intent = new Intent(getActivity(), ClassroomIndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.textViewClassroomName = view.findViewById(R.id.textViewClassroomName);

        view.findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClassroomIndexActivity();
            }
        });
        view.findViewById(R.id.imageButtonBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClassroomIndexActivity();
            }
        });

        view.findViewById(R.id.constraintRow1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classroomShowStatsActivity = new Intent(getContext(), ClassroomShowStatsActivity.class);
                ManageClassroomActivityWithHeaderAndDrawer activity = (ManageClassroomActivityWithHeaderAndDrawer) getActivity();
                classroomShowStatsActivity.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, activity.getClassroom());
                startActivity(classroomShowStatsActivity);
            }
        });
        view.findViewById(R.id.constraintRow2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classroomShowStudentsActivity = new Intent(getContext(), ClassroomShowStudentsActivity.class);
                ManageClassroomActivityWithHeaderAndDrawer activity = (ManageClassroomActivityWithHeaderAndDrawer) getActivity();
                classroomShowStudentsActivity.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, activity.getClassroom());
                startActivity(classroomShowStudentsActivity);
            }
        });
        // TODO implement other sections
//        view.findViewById(R.id.constraintRow3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent copingSkillsIndexActivity = new Intent(getContext(), CopingSkillIndexActivity.class);
//                startActivity(copingSkillsIndexActivity);
//            }
//        });
//        view.findViewById(R.id.constraintRow4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent emotionsIndexActivity = new Intent(getContext(), EmotionIndexActivity.class);
//                startActivity(emotionsIndexActivity);
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();

        ManageClassroomActivityWithHeaderAndDrawer activity = (ManageClassroomActivityWithHeaderAndDrawer) getActivity();
        textViewClassroomName.setText(activity.getClassroom().getName());
    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout.fragment_drawer_teacher_classroom;
    }


    public void setCurrentSection(Section section) {
        for (Section mSection: Section.values()) {
            setHighlighted(mSection, mSection == section);
        }
    }

}
