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

public class ItemSessionRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TextView textStudent, textNumberOfCopingSkills;
    public final ImageView imageStudent, imageEmotion;
    public final AbstractActivity activity;
    private final RecyclerView sessionsCopingSkillRecyclerView;


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
    }


    public synchronized void updateWithItem(SessionAdapter.Item item) {
        String textTitle = item.session.getUuid();
        String date = item.session.getStartedAt().toString();
        final Context appContext = activity.getApplicationContext();

        if (item.student != null) textStudent.setText(item.student.getName());

        if (item.studentDbFile == null) {
            imageStudent.setImageResource(R.drawable.ic_placeholder);
        } else {
            Util.setImageViewWithAsset(appContext, imageStudent, item.studentDbFile.getFilePath());
        }
        if (item.emotionDbFile != null) Util.setImageViewWithAsset(appContext, imageEmotion, item.emotionDbFile.getFilePath());
        if (item.sessionCopingSkillList != null) {
            textNumberOfCopingSkills.setText(String.valueOf(item.sessionCopingSkillList.size()));
            RecyclerView.Adapter adapter = new SessionCopingSkillAdapter(activity, item.sessionCopingSkillList);
            sessionsCopingSkillRecyclerView.setAdapter(adapter);
        }
    }

}