package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.List;

public class CopingSkillIndexAdapter extends AbstractListAdapter<CopingSkill> {

    private final AbstractActivity activity;
    private final List<CopingSkill> copingSkills;
    private final boolean onClickListener;
    private final ClickListener clickListener;

    public interface ClickListener {
        void onClick(CopingSkill copingSkill, List<ItineraryItem> itineraryItems, View view);
    }


    private LiveData<List<ItineraryItem>> getItineraryItems(String copingSkillUuid) {
        Context appContext = activity.getApplicationContext();
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(appContext);
        boolean usesPostCopingSkills = sharedPreferences.getBoolean(Constants.PreferencesKeys.settingsHeartBeatActivity, Constants.DEFAULT_USE_HEART_BEAT_ACTIVITY);
        if (usesPostCopingSkills) {
            return AppDatabase.getInstance(appContext).intermediateTablesDAO().getItineraryItemsForCopingSkill(copingSkillUuid);
        } else {
            //return AppDatabase.getInstance(appContext).intermediateTablesDAO().getItineraryItemsForCopingSkillWithoutPostCopingSkills(copingSkillUuid);
            return AppDatabase.getInstance(appContext).intermediateTablesDAO().getItineraryItemsForCopingSkillWithoutHeartBeatPrompt(copingSkillUuid);
        }
    }


    public CopingSkillIndexAdapter(AbstractActivity activity, List<CopingSkill> copingSkills) {
        this(activity, copingSkills, null);
    }


    public CopingSkillIndexAdapter(AbstractActivity activity, List<CopingSkill> copingSkills, ClickListener listener) {
        this.activity = activity;
        this.copingSkills = copingSkills;
        this.clickListener = listener;
        this.onClickListener = (clickListener != null);
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_coping_skill, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        final CopingSkill copingSkill = copingSkills.get(position);
        TextView textView = result.findViewById(R.id.text1);
        textView.setText(copingSkill.getName());

        if (copingSkill.getImageFileUuid() != null) {
            final Context appContext = activity.getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(copingSkill.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    Util.setImageViewWithDbFile(appContext, (ImageView) result.findViewById(R.id.imageView), dbFile);
                }
            });
        } else {
            ((ImageView) result.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_placeholder);
        }

        final Context appContext = activity.getApplicationContext();
        getItineraryItems(copingSkill.getUuid()).observe(activity, new Observer<List<ItineraryItem>>() {
            @Override
            public void onChanged(@Nullable final List<ItineraryItem> itineraryItems) {
                if (onClickListener) {
                    result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onClick(copingSkill, itineraryItems, result);
                        }
                    });
                }
            }
        });

        return result;
    }

}
