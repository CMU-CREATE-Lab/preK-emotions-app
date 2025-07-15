package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.zigis.segmentedarcview.SegmentedArcView;
import com.zigis.segmentedarcview.custom.ArcSegment;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.ArrayList;
import java.util.List;

public class CopingSkillHighlightWCIndexAdapter extends AbstractListAdapter<CopingSkillWithCustomizations> {

    private final AbstractActivity activity;
    private final List<CopingSkillWithCustomizations> copingSkillsWithCustomizations;

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


    public CopingSkillHighlightWCIndexAdapter(AbstractActivity activity, List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {
        this.activity = activity;
        this.copingSkillsWithCustomizations = copingSkillsWithCustomizations;
    }

    private void setRings(SegmentedArcView arcView) {

        List<ArcSegment> segments = new ArrayList<>();
        segments.add(new ArcSegment(Color.RED, Color.RED,false, 45f));    // 45 degrees
        segments.add(new ArcSegment(Color.GREEN, Color.GREEN,false, 90f)); // 90 degrees
        segments.add(new ArcSegment(Color.BLUE, Color.BLUE,false, 225f));  // 225 degrees

        // Set the segments (custom sweep angles are taken from constructor)
        arcView.setSegments(segments);
    }


    @Override
    public List<CopingSkillWithCustomizations> getList() {
        return copingSkillsWithCustomizations;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_copingskill_circle, parent, false);
            // NOTE: requires api level 21
            SegmentedArcView arcView = result.findViewById(R.id.arcView);
            setRings(arcView);
            result.findViewById(R.id.imageView).setClipToOutline(false);
        } else {
            result = convertView;
        }
        final CopingSkillWithCustomizations copingSkillWithCustomizations = copingSkillsWithCustomizations.get(position);
        final CopingSkill copingSkill = copingSkillWithCustomizations.copingSkill;

        if (copingSkillWithCustomizations.isDisabled()) {
            result.setAlpha(0.5f);
        } else {
            result.setAlpha(1.0f);
        }


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
//                if (onClickListener) {
//                    result.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            clickListener.onClick(copingSkillWithCustomizations, itineraryItems, result);
//                        }
//                    });
//                }
            }
        });

        return result;
    }
}
