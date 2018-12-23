package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.util.List;

public class CopingSkillIndexAdapter extends BaseAdapter {

    private final AppCompatActivity activity;
    private final List<CopingSkill> copingSkills;

    public CopingSkillIndexAdapter(AppCompatActivity activity, List<CopingSkill> copingSkills) {
        this.activity = activity;
        this.copingSkills = copingSkills;
    }

    @Override
    public int getCount() {
        return copingSkills.size();
    }

    @Override
    public Object getItem(int position) {
        return copingSkills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.coping_skill_grid_view_item, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        CopingSkill copingSkill = copingSkills.get(position);
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(copingSkill.getName());

        if (copingSkill.getImageFileUuid() != null) {
            final Context appContext = activity.getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(copingSkill.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    // TODO check if file type is asset
                    Util.setImageViewWithAsset(appContext, (ImageView) result.findViewById(R.id.imageView), dbFile.getFilePath());
                }
            });
        }

        return result;
    }

}
