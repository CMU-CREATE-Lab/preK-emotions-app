package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomIndexActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStudentsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ClassroomShowStatsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.UpdateClassroomModelAsyncTask;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

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


    private void displayClassroomNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getLayoutInflater().inflate(R.layout.dialog_classroom_name, null);
        builder.setView(view)
                .setPositiveButton(R.string.alert_option_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String classroomName = ((EditText) view.findViewById(R.id.editTextClassroomName)).getText().toString();
                        Log.d(Constants.LOG_TAG, String.format("dialog_classroom_name onClick positive; name='%s'", classroomName));
                        updateClassroom(classroomName);
                    }
                })
                .setNegativeButton(R.string.alert_option_cancel, null)
                .setTitle(R.string.alert_title_update_classroom)
                .setMessage(R.string.alert_message_update_classroom);
        builder.create().show();
    }


    private void displayClassroomDeleteDialog() {
        final ManageClassroomActivityWithHeaderAndDrawer activity = (ManageClassroomActivityWithHeaderAndDrawer) getActivity();
        final Classroom classroom = activity.getClassroom();

        // TODO confirm by input classroom name?
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setMessage(R.string.alert_message_delete_classroom);
        builder.setTitle(R.string.alert_title_delete_classroom);
        builder.setPositiveButton(R.string.alert_option_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new UpdateClassroomModelAsyncTask(AppDatabase.getInstance(activity.getApplicationContext()), UpdateClassroomModelAsyncTask.ActionType.DELETE, classroom, new UpdateClassroomModelAsyncTask.PostExecute() {
                    @Override
                    public void onPostExecute(Boolean modelSaved, Classroom classroom) {
                        // deleting classroom triggers return to index
                        startClassroomIndexActivity();
                    }
                }).execute();
            }
        });
        builder.setNegativeButton(R.string.alert_option_cancel, null);
        builder.create().show();
    }


    private void updateClassroom(String classroomName) {
        ManageClassroomActivityWithHeaderAndDrawer activity = (ManageClassroomActivityWithHeaderAndDrawer) getActivity();
        Classroom classroom = activity.getClassroom();
        if (!classroomName.isEmpty() && !classroom.getName().equals(classroomName)) {
            classroom.setName(classroomName);
            new UpdateClassroomModelAsyncTask(AppDatabase.getInstance(activity.getApplicationContext()), UpdateClassroomModelAsyncTask.ActionType.UPDATE, classroom, new UpdateClassroomModelAsyncTask.PostExecute() {
                @Override
                public void onPostExecute(Boolean modelSaved, Classroom classroom) {
                    // update views
                    onResume();
                }
            }).execute();
        } else {
            Log.w(Constants.LOG_TAG, "Classroom model not updated when classroomName either empty or unchanged");
        }
    }


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

        view.findViewById(R.id.imageButtonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClassroomNameDialog();
            }
        });
        view.findViewById(R.id.textViewDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClassroomDeleteDialog();
            }
        });
        view.findViewById(R.id.imageButtonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClassroomDeleteDialog();
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
