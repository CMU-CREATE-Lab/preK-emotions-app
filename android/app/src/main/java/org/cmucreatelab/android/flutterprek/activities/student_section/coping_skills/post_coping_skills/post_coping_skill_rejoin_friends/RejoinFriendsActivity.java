package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;

public class RejoinFriendsActivity extends PostCopingSkillActivity {


    private void populateIntentWithEmotionInfo(Intent intent) {
        String backgroundColor = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionBackgroundColor;
        String somethingElseMessage = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseMessage;
        String somethingElseAudio = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.somethingElseAudio;
        if (backgroundColor.isEmpty()) {
            backgroundColor = "#ffffff";
        }
        if (somethingElseMessage.isEmpty()) {
            somethingElseMessage = "Would you like to try something else?";
        }
        if (somethingElseAudio.isEmpty()) {
            somethingElseAudio = "etc/audio_prompts/audio_something_else.wav";
        }

        intent.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
        intent.putExtra(INTENT_MESSAGE, somethingElseMessage);
        intent.putExtra(INTENT_AUDIO_FILE, somethingElseAudio);
    }


    private void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (nextClass == ChooseCopingSkillActivity.class) {
            populateIntentWithEmotionInfo(intent);
        }
        startActivity(intent);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_rejoin_friends.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_rejoin_friends;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(ChooseCopingSkillActivity.class);
            }
        });
        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(ChooseStudentActivity.class);
            }
        });
    }

}
