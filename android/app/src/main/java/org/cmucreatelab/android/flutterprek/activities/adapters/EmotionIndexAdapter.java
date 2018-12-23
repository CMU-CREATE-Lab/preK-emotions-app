package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.app.Activity;
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
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class EmotionIndexAdapter extends BaseAdapter {

    private final AppCompatActivity activity;
    private final List<Emotion> emotions;

    public EmotionIndexAdapter(AppCompatActivity activity, List<Emotion> emotions) {
        this.activity = activity;
        this.emotions = emotions;
    }

    @Override
    public int getCount() {
        return emotions.size();
    }

    @Override
    public Object getItem(int position) {
        return emotions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO for demo emotions only; remove later
    private String getAssetPathFromPosition(int position) {
        switch (position % 5) {
            case 1:
                return "emotions/ic_mad.png";
            case 2:
                return "emotions/ic_neutral.png";
            case 3:
                return "emotions/ic_sad.png";
            case 4:
                return "emotions/ic_scared.png";
        }
        return "emotions/ic_happy.png";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.emotion_grid_view_item, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        Emotion emotion = emotions.get(position);
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(emotion.getName());
//        // TODO demo emotion (remove later and replace with DB-defined image)
//        Util.setImageViewWithAsset(appContext, (ImageView) result.findViewById(R.id.imageView), getAssetPathFromPosition(position));

        if (emotion.getImageFileUuid() != null) {
            final Context appContext = activity.getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(emotion.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
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
