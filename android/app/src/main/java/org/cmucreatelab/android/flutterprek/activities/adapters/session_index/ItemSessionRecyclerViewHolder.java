package org.cmucreatelab.android.flutterprek.activities.adapters.session_index;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;

public class ItemSessionRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TextView textStudent, textNumberOfCopingSkills, textDate;
    public final ImageView imageStudent, imageEmotion;
    public final View layoutBottom;
    public final AbstractActivity activity;
    private final RecyclerView sessionsCopingSkillRecyclerView;


    private void onClickAudio(String audioFile) {
//        // AbstractActivity.playAudio method uses assets
//        activity.playAudio(audioFile);
        AudioPlayer audioPlayer = AudioPlayer.getInstance(activity.getApplicationContext());
        audioPlayer.stop();
        audioPlayer.addAudioFromInternalStorage(audioFile);
        audioPlayer.playAudio();
    }


    public ItemSessionRecyclerViewHolder(AbstractActivity activity, View v) {
        super(v);
        this.activity = activity;
        final Context appContext = activity.getApplicationContext();

        textStudent = v.findViewById(R.id.textStudent);
        imageEmotion = v.findViewById(R.id.imageEmotion);
        imageStudent = v.findViewById(R.id.imageStudent);
        textNumberOfCopingSkills = v.findViewById(R.id.textNumberOfCopingSkills);
        sessionsCopingSkillRecyclerView = v.findViewById(R.id.sessionCopingSkillsRecyclerView);
        sessionsCopingSkillRecyclerView.setLayoutManager(new LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false));
        textDate = v.findViewById(R.id.textDate);
        layoutBottom = v.findViewById(R.id.layoutBottom);
    }


    public synchronized void updateWithItem(SessionAdapter.Item item) {
        String textTitle = item.session.getUuid();
        String date = item.session.getStartedAt().toString();
        final Context appContext = activity.getApplicationContext();

        if (item.student != null) textStudent.setText(item.student.getName());

        if (item.studentDbFile == null) {
            imageStudent.setImageResource(R.drawable.ic_placeholder);
        } else {
            Util.setImageViewWithDbFile(appContext, imageStudent, item.studentDbFile);
        }
        if (item.emotionDbFile != null) Util.setImageViewWithDbFile(appContext, imageEmotion, item.emotionDbFile);
        if (item.sessionCopingSkillList != null) {
            textNumberOfCopingSkills.setText(String.valueOf(item.sessionCopingSkillList.size()));
            RecyclerView.Adapter adapter = new SessionCopingSkillAdapter(activity, item.sessionCopingSkillList);
            sessionsCopingSkillRecyclerView.setAdapter(adapter);
        }
        textDate.setText(date);
        layoutBottom.setVisibility(View.GONE);
        if (item.customizationList != null) {
            for (Customization customization : item.customizationList) {
                // TODO @tasota should not use hardcoded keys
                if (customization.getKey().equals("audio_talk_about_it")) {
                    layoutBottom.setVisibility(View.VISIBLE);
                    final String audioFile = customization.getValue();
                    layoutBottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickAudio(audioFile);
                        }
                    });
                }
            }
        }
    }

}