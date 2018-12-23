package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.util.List;

public class CopingSkillIndexAdapter extends AbstractListAdapter<CopingSkill> {

    private final AbstractActivity activity;
    private final List<CopingSkill> copingSkills;


    public CopingSkillIndexAdapter(AbstractActivity activity, List<CopingSkill> copingSkills) {
        this.activity = activity;
        this.copingSkills = copingSkills;
    }


    @Override
    public List<CopingSkill> getList() {
        return copingSkills;
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
