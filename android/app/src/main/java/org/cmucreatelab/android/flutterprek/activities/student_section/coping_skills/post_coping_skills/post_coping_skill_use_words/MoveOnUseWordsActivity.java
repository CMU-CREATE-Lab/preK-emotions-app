package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;

public class MoveOnUseWordsActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        // avoid making new instance of RecordUseWordsActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_move_on.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_move_on;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RecordUseWordsActivity.class);
            }
        });
        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RejoinFriendsActivity.class);
            }
        });
    }

}
