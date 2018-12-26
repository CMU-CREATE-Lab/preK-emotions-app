package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.List;

public class EmotionIndexAdapter extends AbstractListAdapter<Emotion> {

    private final AppCompatActivity activity;
    private final List<Emotion> emotions;


    public EmotionIndexAdapter(AppCompatActivity activity, List<Emotion> emotions) {
        this.activity = activity;
        this.emotions = emotions;
    }


    @Override
    public List<Emotion> getList() {
        return emotions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_emotion, parent, false);
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
