package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;

public class OptionCheckItemSoundFragment extends AbstractFragment {

    private boolean songIsPlaying = false;
    private boolean isChecked = true;
    private String audioFilepath, songName;
    private OptionCheckItemSoundPlayListener listener;

    private ImageView imageViewSongPlayPause;
    private CheckBox checkboxSongName;

    public interface OptionCheckItemSoundPlayListener {
        void onClickSongPlayPause(OptionCheckItemSoundFragment fragment, String audioFilepath, boolean play);
    }


    public void setOnPlayListener(OptionCheckItemSoundPlayListener listener) {
        this.listener = listener;
    }


    public void setAttributes(String audioFilepath, String songName, boolean isChecked) {
        this.audioFilepath = audioFilepath;
        this.songName = songName;
        this.isChecked = isChecked;
    }


    public void setSongIsPlaying(boolean songIsPlaying) {
        this.songIsPlaying = songIsPlaying;
    }


    public void updateViews() {
        if (songIsPlaying) {
            imageViewSongPlayPause.setImageResource(R.drawable.ic_music_stop);
        } else {
            imageViewSongPlayPause.setImageResource(R.drawable.ic_music_play);
        }
        checkboxSongName.setChecked(isChecked);
        checkboxSongName.setText(songName);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.imageViewSongPlayPause = view.findViewById(R.id.imageViewSongPlayPause);
        this.checkboxSongName = view.findViewById(R.id.checkboxSongName);

        // TODO set state (song name, checkbox)

        imageViewSongPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSongIsPlaying(!songIsPlaying);
                if (listener != null) {
                    listener.onClickSongPlayPause(OptionCheckItemSoundFragment.this, audioFilepath, songIsPlaying);
                }
            }
        });
    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout.fragment_coping_skill_option_check_item_sound;
    }

}
