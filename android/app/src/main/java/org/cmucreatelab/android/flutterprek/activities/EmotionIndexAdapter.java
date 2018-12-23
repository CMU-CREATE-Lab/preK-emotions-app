package org.cmucreatelab.android.flutterprek.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class EmotionIndexAdapter extends BaseAdapter {

    private final Context appContext;
    private final List<Emotion> emotions;

    public EmotionIndexAdapter(Context appContext, List<Emotion> emotions) {
        this.appContext = appContext;
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
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.emotion_grid_view_item, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(emotions.get(position).getName());
        // TODO demo emotion (remove later and replace with DB-defined image)
        Util.setImageViewWithAsset(appContext, (ImageView) result.findViewById(R.id.imageView), getAssetPathFromPosition(position));

        return result;
    }

}
