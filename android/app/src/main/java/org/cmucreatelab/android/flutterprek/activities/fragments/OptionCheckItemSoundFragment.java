package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DbHelperWandMusicSongs;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

public class OptionCheckItemSoundFragment extends AbstractFragment {

    private boolean songIsPlaying = false;

    public DbHelperWandMusicSongs.MusicFileJson musicFileJson;
//    private boolean isChecked = true;
//    private String dbFileUuid, songName;
    private OptionCheckItemSoundPlayListener listener;

    private ImageView imageViewSongPlayPause;
    private CheckBox checkboxSongName;

    public interface OptionCheckItemSoundPlayListener {
        void onClickSongPlayPause(OptionCheckItemSoundFragment fragment, String audioFilepath, boolean play);
        void onCheckboxSongName(OptionCheckItemSoundFragment fragment, boolean isChecked);
    }


    public void setOnPlayListener(OptionCheckItemSoundPlayListener listener) {
        this.listener = listener;
    }


    public void setMusicFileJson(DbHelperWandMusicSongs.MusicFileJson musicFileJson) {
        this.musicFileJson = musicFileJson;
    }
//    public void setAttributes(String dbFileUuid, String songName, boolean isChecked) {
//        this.dbFileUuid = dbFileUuid;
//        this.songName = songName;
//        this.isChecked = isChecked;
//    }


    public void setSongIsPlaying(boolean songIsPlaying) {
        this.songIsPlaying = songIsPlaying;
    }


    public void updateViews() {
        if (songIsPlaying) {
            imageViewSongPlayPause.setImageResource(R.drawable.ic_music_stop);
        } else {
            imageViewSongPlayPause.setImageResource(R.drawable.ic_music_play);
        }
        checkboxSongName.setChecked((musicFileJson.selected == 1));
        checkboxSongName.setText(musicFileJson.songName);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.imageViewSongPlayPause = view.findViewById(R.id.imageViewSongPlayPause);
        this.checkboxSongName = view.findViewById(R.id.checkboxSongName);

        // TODO set state (song name, checkbox)
        updateViews();

        checkboxSongName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                musicFileJson.selected = b ? 1 : 0;
                listener.onCheckboxSongName(OptionCheckItemSoundFragment.this, b);
            }
        });

        imageViewSongPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSongIsPlaying(!songIsPlaying);
                if (listener != null) {
                    AppDatabase.getInstance(getContext()).dbFileDAO().getDbFile(musicFileJson.dbfileuuid).observe(getViewLifecycleOwner(), new Observer<DbFile>() {
                        @Override
                        public void onChanged(@Nullable DbFile dbFile) {
                            String audioFilepath = dbFile.getFilePath();
                            listener.onClickSongPlayPause(OptionCheckItemSoundFragment.this, audioFilepath, songIsPlaying);
                        }
                    });
                }
            }
        });

    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout.fragment_coping_skill_option_check_item_sound;
    }

}
