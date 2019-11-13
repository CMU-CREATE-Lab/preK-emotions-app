package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;

public class ParentVideoCopingSkillActivity extends VideoCopingSkillActivity {

    public static final String EXTRA_VIDEO_TYPE = "video_type";
    public static final String CLASS_3B_STUDENT2 = "class_3b_student2";
    public static final String DEFAULT = "default";

    private int videoResource;


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_flower_sky;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String videoType = getIntent().getStringExtra(EXTRA_VIDEO_TYPE);
        if (videoType != null) {
            if (videoType.equals(CLASS_3B_STUDENT2)) {
                videoResource = R.raw.student2_video;
            } else {
                videoResource = R.raw.jumpingjacks;
            }
        } else {
            videoResource = R.raw.jumpingjacks;
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_video;
    }


    @Override
    public boolean useAudioFromVideo() {
        return true;
    }


    @Override
    public String getFilePathForVideo() {
        return "android.resource://" + getPackageName() + "/" + videoResource;
    }

}
