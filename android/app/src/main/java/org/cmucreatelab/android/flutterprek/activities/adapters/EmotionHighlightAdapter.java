package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.util.List;

public class EmotionHighlightAdapter extends AbstractListAdapter<Emotion>{

    private final AbstractActivity activity;
    private final List<Emotion> emotions;
    private final boolean onClickListener;
    private final EmotionHighlightAdapter.ClickListener clickListener;

    public interface ClickListener {
        void onClick(Emotion emotion, List<ItineraryItem> itineraryItems);
    }


    public EmotionHighlightAdapter(AbstractActivity activity, List<Emotion> emotions) {
        this(activity, emotions, null);
    }


    public EmotionHighlightAdapter(AbstractActivity activity, List<Emotion> emotions, EmotionHighlightAdapter.ClickListener clickListener) {
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
        } else if (e.equals("Scared")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_scared.wav");
        } else if (e.equals("Excited")) {
            audioPlayer.addAudioFromAssets("etc/audio_prompts/audio_emotion_excited.wav");
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_custom_emotion, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }

        TextView textView = result.findViewById(R.id.text1);
        textView.setText(emotion.getName());


        //TODO check for and grab custom emotion pictures
        if (emotion.getImageFileUuid() != null) {
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(emotion.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    Util.setImageViewWithDbFile(appContext,(ImageView) result.findViewById(R.id.imageView), dbFile);
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

                    result.findViewById(R.id.cameraPlusIcon).setOnClickListener(new View.OnClickListener() {
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
