package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

public class RecordUseWordsActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, MoveOnUseWordsActivity.class);
        startActivity(intent);
    }


    // TODO wait until audio finishes playing/audio cue before start recording and animation
    private void circleRevealAnimation(View myView) {
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius);
            anim.setDuration(8000);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            myView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_record.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_record;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // previously invisible view
        View myView = findViewById(R.id.imageViewCircleWhite);
        myView.setVisibility(View.INVISIBLE);

        myView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                circleRevealAnimation(v);
            }
        });

        findViewById(R.id.imageViewStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save recorded audio
                goToNextPostCopingSkillActivity();
            }
        });
    }

}
