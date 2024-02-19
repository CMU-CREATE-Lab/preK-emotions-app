package org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.ModelUpdateHeaderFragment;
import org.cmucreatelab.android.flutterprek.activities.fragments.OptionCheckItemSoundFragment;
import org.cmucreatelab.android.flutterprek.database.DbHelperWandMusicSongs;

import java.util.ArrayList;

// TODO see StudentUpdateAbstractActivity
//public class CopingSkillsEditActivity extends StudentUpdateAbstractActivity {
public class CopingSkillEditActivity extends AbstractActivity implements OptionCheckItemSoundFragment.OptionCheckItemSoundPlayListener  {

    public static final String COPING_SKILL_KEY = "coping_skill_with_customizations";

    private LinearLayout linearLayoutOtherOptions;
    private DbHelperWandMusicSongs dbHelperWandMusicSongs;
    private ArrayList<OptionCheckItemSoundFragment> fragments;
    private ModelUpdateHeaderFragment headerFragment;

    private String copingSkillUuid;


    private void initializeOptionItems() {
        this.fragments = new ArrayList<>();
        this.linearLayoutOtherOptions = findViewById(R.id.linearLayoutOtherOptions);

        dbHelperWandMusicSongs.readFromDb(new DbHelperWandMusicSongs.Listener() {
            @Override
            public void onReadFromDb(ArrayList<DbHelperWandMusicSongs.MusicFileJson> arrayListJson) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                for(DbHelperWandMusicSongs.MusicFileJson musicFileJson: arrayListJson) {
                    final OptionCheckItemSoundFragment musicFragment = new OptionCheckItemSoundFragment();
                    //musicFragment.setAttributes(musicFileJson.dbFieldUuid, musicFileJson.songName, (musicFileJson.selected == 1));
                    musicFragment.setMusicFileJson(musicFileJson);
                    musicFragment.setOnPlayListener(CopingSkillEditActivity.this);
                    fragments.add(musicFragment);

                    ft.add(R.id.linearLayoutOtherOptions, musicFragment);
                    ft.runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            musicFragment.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            musicFragment.updateViews();
                        }
                    });
                }
                ft.commit();
            }
        });
    }


    private void updateDb() {
        ArrayList<DbHelperWandMusicSongs.MusicFileJson> list = new ArrayList<>();
        for (OptionCheckItemSoundFragment fragment: fragments) {
            list.add(fragment.musicFileJson);
        }
        dbHelperWandMusicSongs.writeToDb(list);
    }


    //@Override
    public String getHeaderTitle() {
        return "Edit Coping Skill";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.copingSkillUuid = getIntent().getStringExtra(COPING_SKILL_KEY);
        Log.v(Constants.LOG_TAG, copingSkillUuid == null ? "Failed to set coping skill uuid." : String.format("got coping skill uuid=%s", copingSkillUuid));
        this.dbHelperWandMusicSongs = new DbHelperWandMusicSongs(this, copingSkillUuid);
        initializeOptionItems();
        this.headerFragment = (ModelUpdateHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
        headerFragment.setHeaderTransparency(true);
        headerFragment.imageButtonDelete.setVisibility(View.INVISIBLE);
        headerFragment.textViewBack.setText("Back");
        headerFragment.textViewTitle.setText(getHeaderTitle());
        headerFragment.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO finishEditActivity(StudentUpdateAbstractActivity.ModelAction.CANCEL);
                finish();
            }
        });
        headerFragment.imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO finishEditActivity(StudentUpdateAbstractActivity.ModelAction.CANCEL);
                finish();
            }
        });
        headerFragment.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO finishEditActivity(StudentUpdateAbstractActivity.ModelAction.UPDATE);
                finish();
            }
        });
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_coping_skills_edit;
    }


    @Override
    public void onClickSongPlayPause(OptionCheckItemSoundFragment fragment, String audioFilepath, boolean play) {
        for (OptionCheckItemSoundFragment item: fragments) {
            item.setSongIsPlaying(false);
        }
        stopAudio();
        if (play) {
            playAudio(audioFilepath, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    fragment.setSongIsPlaying(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragment.updateViews();
                        }
                    });
                }
            });
            fragment.setSongIsPlaying(true);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OptionCheckItemSoundFragment item: fragments) {
                    item.updateViews();
                }
            }
        });
    }

    @Override
    public void onCheckboxSongName(OptionCheckItemSoundFragment fragment, boolean isChecked) {
        updateDb();
    }

}