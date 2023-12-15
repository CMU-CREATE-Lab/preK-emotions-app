package org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills;

import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.OptionCheckItemSoundFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

// TODO see StudentUpdateAbstractActivity
//public class CopingSkillsEditActivity extends StudentUpdateAbstractActivity {
public class CopingSkillEditActivity extends AbstractActivity implements OptionCheckItemSoundFragment.OptionCheckItemSoundPlayListener  {

    //private static final String headerTitleEditStudent = "Edit Student";
    private OptionCheckItemSoundFragment fragment1, fragment2, fragment3, fragment4;


    private void initializeOptionItems() {
        this.fragment1 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option1Fragment);
        this.fragment2 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option2Fragment);
        this.fragment3 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option3Fragment);
        this.fragment4 =  (OptionCheckItemSoundFragment) getSupportFragmentManager().findFragmentById(R.id.option4Fragment);

        fragment1.setAttributes("etc/music/WandMusic.wav", "Wand Music", true);
        fragment2.setAttributes("etc/music/minfulnest_song1.mp3", "Test 1", true);
        fragment3.setAttributes("etc/music/minfulnest_song2.mp3", "Test 2", true);
        fragment4.setAttributes("etc/music/minfulnest_song3.mp3", "Test 3", true);

        fragment1.setOnPlayListener(this);
        fragment2.setOnPlayListener(this);
        fragment3.setOnPlayListener(this);
        fragment4.setOnPlayListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment1.updateViews();
                fragment2.updateViews();
                fragment3.updateViews();
                fragment4.updateViews();
            }
        });

        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.addAudioFromAssets("etc/music/WandMusic.wav");
        audioPlayer.playAudio();
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
        initializeOptionItems();
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_coping_skills_edit;
    }


    @Override
    public void onClickSongPlayPause(OptionCheckItemSoundFragment fragment, String audioFilepath, boolean play) {
        fragment1.setSongIsPlaying(false);
        fragment2.setSongIsPlaying(false);
        fragment3.setSongIsPlaying(false);
        fragment4.setSongIsPlaying(false);
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
                fragment1.updateViews();
                fragment2.updateViews();
                fragment3.updateViews();
                fragment4.updateViews();
            }
        });
    }

}