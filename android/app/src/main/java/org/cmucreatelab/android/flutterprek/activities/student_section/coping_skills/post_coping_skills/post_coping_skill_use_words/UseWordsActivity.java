package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;

public class UseWordsActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        startActivity(intent);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_use_words.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_use_words;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RejoinFriendsActivity.class);
            }
        });
        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RecordUseWordsActivity.class);
            }
        });
    }

}
