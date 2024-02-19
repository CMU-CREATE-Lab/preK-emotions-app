package org.cmucreatelab.android.flutterprek.activities.teacher_section.coping_skills;

import static org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations.CUSTOMIZATION_IS_DISABLED;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.fragments.ModelUpdateHeaderFragment;
import org.cmucreatelab.android.flutterprek.activities.fragments.OptionCheckItemSoundFragment;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DbHelperWandMusicSongs;
import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;

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
    private CopingSkillWithCustomizations copingSkillWithCustomizations;


    private void initializeOptionItems() {
        this.fragments = new ArrayList<>();
        this.linearLayoutOtherOptions = findViewById(R.id.linearLayoutOtherOptions);

        dbHelperWandMusicSongs.readFromDb(new DbHelperWandMusicSongs.Listener() {
            @Override
            public void onReadFromDb(ArrayList<DbHelperWandMusicSongs.MusicFileJson> arrayListJson) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (arrayListJson.size() > 0) {
                    findViewById(R.id.labelCopingSkillOptions).setVisibility(View.VISIBLE);
                }
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


    private void initializeCopingSkillAvailable() {
        Switch switchCopingSkillAvailable = findViewById(R.id.switchCopingSkillAvailable);
        Log.v(Constants.LOG_TAG, "initializeCopingSkillAvailable with isDisabled= "+copingSkillWithCustomizations.isDisabled());
        switchCopingSkillAvailable.setChecked(!(copingSkillWithCustomizations.isDisabled()));

        switchCopingSkillAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.v(Constants.LOG_TAG, "onCheckedChanged: "+b);

                if (copingSkillWithCustomizations.hasCustomization(CUSTOMIZATION_IS_DISABLED)) {
                    copingSkillWithCustomizations.isDisabled(getApplicationContext(), !b);
                } else {
                    // if the customization doesn't already exist in the table, add it
                    final Customization customization = new Customization(CUSTOMIZATION_IS_DISABLED, b ? "" : "1");
                    customization.setBasedOnUuid(copingSkillWithCustomizations.copingSkill.getUuid());
                    copingSkillWithCustomizations.customizations.add(customization);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(getApplicationContext()).customizationDAO().insert(customization);
                        }
                    });
                }
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
//        this.copingSkillUuid = getIntent().getStringExtra(COPING_SKILL_KEY);
        this.copingSkillWithCustomizations = (CopingSkillWithCustomizations) (getIntent().getSerializableExtra(COPING_SKILL_KEY));
        this.copingSkillUuid = copingSkillWithCustomizations.copingSkill.getUuid();
        Log.v(Constants.LOG_TAG, copingSkillUuid == null ? "Failed to set coping skill uuid." : String.format("got coping skill uuid=%s", copingSkillUuid));
        this.dbHelperWandMusicSongs = new DbHelperWandMusicSongs(this, copingSkillUuid);
        initializeOptionItems();
        this.headerFragment = (ModelUpdateHeaderFragment) (getSupportFragmentManager().findFragmentById(R.id.headerFragment));
        headerFragment.setHeaderTransparency(true);
        headerFragment.imageButtonDelete.setVisibility(View.INVISIBLE);
        headerFragment.buttonSave.setVisibility(View.INVISIBLE);
        headerFragment.textViewBack.setText("Back");
        headerFragment.textViewTitle.setText(getHeaderTitle());
        headerFragment.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        headerFragment.imageButtonBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.labelCopingSkillOptions).setVisibility(View.INVISIBLE);

        initializeCopingSkillAvailable();
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