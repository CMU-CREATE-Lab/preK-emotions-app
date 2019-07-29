package org.cmucreatelab.android.flutterprek.activities.adapters.session_index;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;

import java.util.List;

public class SessionCopingSkillAdapter extends RecyclerView.Adapter<SessionCopingSkillAdapter.ItemSessionCopingSkillRecyclerViewHolder> {

    private final AbstractActivity activity;
    private final List<SessionCopingSkill> sessionCopingSkills;

    public static class ItemSessionCopingSkillRecyclerViewHolder extends RecyclerView.ViewHolder {
        public final Context appContext;
        public final ImageView imageSessionCopingSkill;

        public ItemSessionCopingSkillRecyclerViewHolder(View itemView) {
            super(itemView);
            appContext = itemView.getContext().getApplicationContext();
            imageSessionCopingSkill = itemView.findViewById(R.id.imageSessionCopingSkill);
            imageSessionCopingSkill.setImageResource(R.drawable.ic_placeholder);
        }

        public synchronized void updateWithAsset(String asset) {
            Util.setImageViewWithAsset(appContext, imageSessionCopingSkill, asset);
        }

    }


    public SessionCopingSkillAdapter(AbstractActivity activity, List<SessionCopingSkill> sessionCopingSkills) {
        this.activity = activity;
        this.sessionCopingSkills = sessionCopingSkills;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ItemSessionCopingSkillRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_session_coping_skill, parent, false);
        ItemSessionCopingSkillRecyclerViewHolder vh = new ItemSessionCopingSkillRecyclerViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemSessionCopingSkillRecyclerViewHolder holder, int position) {
        SessionCopingSkill sessionCopingSkill = sessionCopingSkills.get(position);
        // TODO there's a better way to query this with JOINs
        final AppDatabase appDatabase = AppDatabase.getInstance(activity.getApplicationContext());

        appDatabase.copingSkillDAO().getCopingSkill(sessionCopingSkill.getCopingSkillUuid()).observe(activity, new Observer<CopingSkill>() {
            @Override
            public void onChanged(@Nullable CopingSkill copingSkill) {
                if (copingSkill == null) return;
                appDatabase.dbFileDAO().getDbFile(copingSkill.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                    @Override
                    public void onChanged(@Nullable DbFile dbFile) {
                        if (dbFile == null)return;
                        // TODO check file type
                        holder.updateWithAsset(dbFile.getFilePath());
                    }
                });
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sessionCopingSkills.size();
    }

}