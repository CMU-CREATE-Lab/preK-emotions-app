package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import org.cmucreatelab.android.flutterprek.R;

public class OptionCheckItemSoundFragment extends AbstractFragment {

    private boolean songIsPlaying = false;
    private Button buttonSongPlayPause;
    private CheckBox checkboxSongName;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.buttonSongPlayPause = view.findViewById(R.id.checkboxSongName);
        this.checkboxSongName = view.findViewById(R.id.checkboxSongName);

        // TODO set state (song name, checkbox)

        buttonSongPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songIsPlaying) {
                    // TODO stop music, change state
                    buttonSongPlayPause.setText("Play");
                } else {
                    // TODO play music, change state
                    buttonSongPlayPause.setText("Stop");
                }
                songIsPlaying = !songIsPlaying;
            }
        });
    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout.fragment_coping_skill_option_check_item_sound;
    }


    /** Set header to be transparent when {@param flag} is true. */
    public void setHeaderTransparency(boolean flag) {
        int color = flag ? Color.alpha(100) : getResources().getColor(R.color.colorPrimary);
        getView().setBackgroundColor(color);
    }

}
