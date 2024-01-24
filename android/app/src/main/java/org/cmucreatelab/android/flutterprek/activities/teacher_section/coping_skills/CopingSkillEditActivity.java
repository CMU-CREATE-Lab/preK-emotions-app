package org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills;

import android.arch.lifecycle.Observer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.OptionCheckItemSoundFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DbHelperWandMusicSongs;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.util.ArrayList;

// TODO see StudentUpdateAbstractActivity
//public class CopingSkillsEditActivity extends StudentUpdateAbstractActivity {
public class CopingSkillEditActivity extends AbstractActivity implements OptionCheckItemSoundFragment.OptionCheckItemSoundPlayListener  {

    //private static final String headerTitleEditStudent = "Edit Student";
//    private OptionCheckItemSoundFragment fragment1, fragment2, fragment3, fragment4;

    private LinearLayout linearLayoutOtherOptions;
    private DbHelperWandMusicSongs dbHelperWandMusicSongs;
    private ArrayList<OptionCheckItemSoundFragment> fragments;


    private void initializeOptionItems() {
        // TODO use DbHelperWandMusicSongs to populate and read 'selected'
        // TODO test call to readFromDb and writeToDb (to make sure those functions work/read as expected)
//        this.fragment1 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option1Fragment);
//        this.fragment2 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option2Fragment);
//        this.fragment3 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option3Fragment);
//        this.fragment4 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option4Fragment);

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
//                    AppDatabase.getInstance(getApplicationContext()).dbFileDAO().getDbFile(musicFileJson.dbFieldUuid).observe(CopingSkillEditActivity.this, new Observer<DbFile>() {
//                        @Override
//                        public void onChanged(@Nullable DbFile dbFile) {
//                            musicFragment.setAttributes(dbFile.getFilePath(), musicFileJson.songName, (musicFileJson.selected == 1));
//                            musicFragment.setOnPlayListener(CopingSkillEditActivity.this);
//                            ft.add(R.id.linearLayoutOtherOptions, musicFragment);
//                            ft.runOnCommit(new Runnable() {
//                                @Override
//                                public void run() {
//                                    musicFragment.getView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                                    musicFragment.updateViews();
//                                }
//                            });
//                        }
//                    });
                }
                ft.commit();
            }
        });

//        fragment1.setAttributes("etc/music/WandMusic.wav", "Wand Music", true);
//        fragment2.setAttributes("etc/music/minfulnest_song1.mp3", "Test 1", true);
//        fragment3.setAttributes("etc/music/minfulnest_song2.mp3", "Test 2", true);
//        fragment4.setAttributes("etc/music/minfulnest_song3.mp3", "Test 3", true);
//
//        fragment1.setOnPlayListener(this);
//        fragment2.setOnPlayListener(this);
//        fragment3.setOnPlayListener(this);
//        fragment4.setOnPlayListener(this);
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                fragment1.updateViews();
//                fragment2.updateViews();
//                fragment3.updateViews();
//                fragment4.updateViews();
//            }
//        });
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


    //@Override
    public boolean isDisplayDeleteButton() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelperWandMusicSongs = new DbHelperWandMusicSongs(this, "coping_skill_14");
        initializeOptionItems();
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_coping_skills_edit;
    }


    @Override
    public void onClickSongPlayPause(OptionCheckItemSoundFragment fragment, String audioFilepath, boolean play) {
//        fragment1.setSongIsPlaying(false);
//        fragment2.setSongIsPlaying(false);
//        fragment3.setSongIsPlaying(false);
//        fragment4.setSongIsPlaying(false);
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

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                fragment1.updateViews();
//                fragment2.updateViews();
//                fragment3.updateViews();
//                fragment4.updateViews();
//            }
//        });
    }

    @Override
    public void onCheckboxSongName(OptionCheckItemSoundFragment fragment, boolean isChecked) {
        updateDb();
    }

}