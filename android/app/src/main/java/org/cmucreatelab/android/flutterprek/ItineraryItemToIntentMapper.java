package org.cmucreatelab.android.flutterprek;

import android.content.Intent;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze.SqueezeCuddleCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow.FlowerRainbowCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone.FlowerStandaloneCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle.CuddleCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.DanceCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.EmptyStaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.ShareCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze.SqueezeCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_talk_to_the_teacher.TalkToTheTeacherCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_talk_about_it.TalkAboutItActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.JumpingJacksCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.ParentVideoCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaHappyActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaMadExcitedActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaSadActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaScaredActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand.WandCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone.WandStandaloneActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_finished_exercise.FinishedExerciseActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.HeartBeatingActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.UseWordsActivity;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

import java.io.Serializable;
import java.util.HashMap;

import static org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.ParentVideoCopingSkillActivity.CLASS_3B_STUDENT2;
import static org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.ParentVideoCopingSkillActivity.DEFAULT;
import static org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.ParentVideoCopingSkillActivity.EXTRA_VIDEO_TYPE;

public class ItineraryItemToIntentMapper {

    private final SessionTracker sessionTracker;


    public ItineraryItemToIntentMapper(SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }


    public Intent createIntentFromItineraryItem(AbstractActivity activity, ItineraryItem itineraryItem) {
        Intent result;
        HashMap<String, Serializable> extras = new HashMap<>();
        Class copingSkillClass;
        String capabilityId = itineraryItem.getCapabilityId();

        if (capabilityId.equals("finish")) {
            sessionTracker.endSession();
            return SessionTracker.getIntentForEndSession(activity);
        }

        switch (capabilityId) {
            case "coping_skill_flower":
                copingSkillClass = FlowerCopingSkillActivity.class;
                break;
            case "coping_skill_flower_rainbow":
                copingSkillClass = FlowerRainbowCopingSkillActivity.class;
                break;
            case "coping_skill_static.cuddle":
                copingSkillClass = CuddleCopingSkillActivity.class;
                break;
            case "coping_skill_static.dance":
                copingSkillClass = DanceCopingSkillActivity.class;
                break;
            case "coping_skill_video.jumping_jacks":
                copingSkillClass = JumpingJacksCopingSkillActivity.class;
                break;
            case "coping_skill_static.share":
                copingSkillClass = ShareCopingSkillActivity.class;
                break;
            case "coping_skill_video.yoga_happy":
                copingSkillClass = YogaHappyActivity.class;
                break;
            case "coping_skill_video.yoga_sad":
                copingSkillClass = YogaSadActivity.class;
                break;
            case "coping_skill_video.yoga_mad":
                copingSkillClass = YogaMadExcitedActivity.class;
                break;
            case "coping_skill_video.yoga_scared":
                copingSkillClass = YogaScaredActivity.class;
                break;
            case "coping_skill_video.parent":
                copingSkillClass = ParentVideoCopingSkillActivity.class;
                String ownerUuid = itineraryItem.getOwnerUuid();
                if (ownerUuid.equals("coping_skill_15")) {
                    extras.put(EXTRA_VIDEO_TYPE, CLASS_3B_STUDENT2);
                } else {
                    extras.put(EXTRA_VIDEO_TYPE, DEFAULT);
                }
                break;
            case "coping_skill_squeeze":
                copingSkillClass = SqueezeCopingSkillActivity.class;
                break;
            case "coping_skill_talk_about_it":
                copingSkillClass = TalkAboutItActivity.class;
                break;
            case "coping_skill_wand":
                copingSkillClass = WandCopingSkillActivity.class;
                break;
            case "coping_skill_wand_standalone":
                copingSkillClass = WandStandaloneActivity.class;
                break;
            case "post_coping_skill_heart_beating":
                copingSkillClass = HeartBeatingActivity.class;
                break;
            case "post_coping_skill_finished_exercise":
                copingSkillClass = FinishedExerciseActivity.class;
                break;
            case "post_coping_skill_use_words":
                copingSkillClass = UseWordsActivity.class;
                break;
            case "coping_skill_talk_to_the_teacher":
                copingSkillClass = TalkToTheTeacherCopingSkillActivity.class;
                break;
            case "coping_skill_flower_standalone":
                copingSkillClass = FlowerStandaloneCopingSkillActivity.class;
                break;
            case "coping_skill_cuddle_squeeze":
                copingSkillClass = SqueezeCuddleCopingSkillActivity.class;
                break;
            default:
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: None (default)");
                // TODO default coping skill settings?
                copingSkillClass = EmptyStaticCopingSkillActivity.class;
        }

        result = new Intent(activity, copingSkillClass);
        for (String key: extras.keySet()) {
            result.putExtra(key, extras.get(key));
        }
        return result;
    }

}
