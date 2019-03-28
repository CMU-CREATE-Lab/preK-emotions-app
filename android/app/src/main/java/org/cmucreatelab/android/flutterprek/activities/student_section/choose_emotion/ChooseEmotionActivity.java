package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import org.cmucreatelab.android.flutterprek.R;

public class ChooseEmotionActivity extends ChooseEmotionAbstractActivity {


    @Override
    protected void onResume() {
        super.onResume();
        playAudioHowAreYouFeeling();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_emotion;
    }

}
