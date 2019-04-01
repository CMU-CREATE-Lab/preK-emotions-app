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
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.List;

public class EmotionIndexAdapter extends AbstractListAdapter<Emotion> {

    private final AppCompatActivity activity;
    private final List<Emotion> emotions;
    private final boolean onClickListener;
    private final ClickListener clickListener;

    public interface ClickListener {
        void onClick(Emotion emotion, List<ItineraryItem> itineraryItems);
    }


    public EmotionIndexAdapter(AppCompatActivity activity, List<Emotion> emotions) {
        this(activity, emotions, null);
    }


    public EmotionIndexAdapter(AppCompatActivity activity, List<Emotion> emotions, ClickListener clickListener) {
        this.activity = activity;
        this.emotions = emotions;
        this.clickListener = clickListener;
        this.onClickListener = (clickListener != null);
    }


    @Override
    public List<Emotion> getList() {
        return emotions;
    }


    private void playAudioFeeling(String e) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance(activity.getApplicationContext());
        audioPlayer.stop();
        if (e.equals("Happy")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_happy.wav");
        } else if (e.equals("Sad")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_sad.wav");
        } else if (e.equals("Mad")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_mad.wav");
        }
        audioPlayer.playAudio();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context appContext = activity.getApplicationContext();
        final Emotion emotion = emotions.get(position);

        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_emotion, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        TextView textView = result.findViewById(R.id.text1);
        textView.setText(emotion.getName());

        result.findViewById(R.id.imagePlayAudioView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioFeeling(emotion.getName());
            }
        });

        if (emotion.getImageFileUuid() != null) {
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(emotion.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    // TODO check if file type is asset
                    Util.setImageViewWithAsset(appContext, (ImageView) result.findViewById(R.id.imageView), dbFile.getFilePath());
                }
            });
        } else {
            ((ImageView) result.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_placeholder);
        }

        AppDatabase.getInstance(appContext).intermediateTablesDAO().getItineraryItemsForEmotion(emotion.getUuid()).observe(activity, new Observer<List<ItineraryItem>>() {
            @Override
            public void onChanged(@Nullable final List<ItineraryItem> itineraryItems) {
                if (onClickListener) {
                    result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListener.onClick(emotion, itineraryItems);
                        }
                    });
                }
            }
        });

        return result;
    }

}
