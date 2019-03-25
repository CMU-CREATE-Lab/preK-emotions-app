package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.CuddleCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.DanceCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.EmptyStaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.ShareCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticSqueezeCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_talk_about_it.TalkAboutItActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.JumpingJacksCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaHappyActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaMadExcitedActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.YogaSadActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_finished_exercise.FinishedExerciseActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.HeartBeatingActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.UseWordsActivity;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;

public class ItineraryItemToIntentMapper {


    public static Intent createIntentFromItineraryItem(Activity activity, ItineraryItem itineraryItem) {
        Intent result;
        Class copingSkillClass;
        String capabilityId = itineraryItem.getCapabilityId();

        switch (capabilityId) {
            case "coping_skill_flower":
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Flower Breathing");
                copingSkillClass = FlowerCopingSkillActivity.class;
                break;
//            case "coping_skill_2":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Static");
//                copingSkillClass = EmptyStaticCopingSkillActivity.class;
//                break;
//            case "coping_skill_3":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Cuddle");
//                copingSkillClass = CuddleCopingSkillActivity.class;
//                break;
//            case "coping_skill_4":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Dance");
//                copingSkillClass = DanceCopingSkillActivity.class;
//                break;
//            case "coping_skill_5":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Jumping Jacks");
//                copingSkillClass = JumpingJacksCopingSkillActivity.class;
//                break;
//            case "coping_skill_6":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Share");
//                copingSkillClass = ShareCopingSkillActivity.class;
//                break;
//            case "coping_skill_7":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Yoga Happy");
//                copingSkillClass = YogaHappyActivity.class;
//                break;
//            case "coping_skill_8":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Yoga Sad");
//                copingSkillClass = YogaSadActivity.class;
//                break;
//            case "coping_skill_9":
//                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Yoga Mad");
//                copingSkillClass = YogaMadExcitedActivity.class;
//                break;
//            case "coping_skill_10":
//                Log.v(Constants.LOG_TAG,  "createIntentFromCopingSkill: Static Squeeze");
//                copingSkillClass = StaticSqueezeCopingSkillActivity.class;
//                break;
//            case "coping_skill_11":
//                Log.v(Constants.LOG_TAG,  "createIntentFromCopingSkill: Talk About It");
//                copingSkillClass = TalkAboutItActivity.class;
//                break;
            case "post_coping_skill_heart_beating":
                copingSkillClass = HeartBeatingActivity.class;
                break;
            case "post_coping_skill_finished_exercise":
                copingSkillClass = FinishedExerciseActivity.class;
                break;
            case "post_coping_skill_use_words":
                copingSkillClass = UseWordsActivity.class;
                break;
            default:
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: None (default)");
                // TODO default coping skill settings?
                copingSkillClass = EmptyStaticCopingSkillActivity.class;
        }

        result = new Intent(activity, copingSkillClass);
        return result;
    }

}
