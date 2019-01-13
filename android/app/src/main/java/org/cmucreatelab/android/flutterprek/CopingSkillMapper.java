package org.cmucreatelab.android.flutterprek;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.activities.coping_skill_flower.FlowerCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.coping_skill_static.StaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;

public class CopingSkillMapper {


    public static Intent createIntentFromCopingSkill(Activity activity, CopingSkill copingSkill) {
        Intent result;
        Class copingSkillClass;

        switch (copingSkill.getUuid()) {
            case "coping_skill_1":
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Flower Breathing");
                copingSkillClass = FlowerCopingSkillActivity.class;
                break;
            case "coping_skill_2":
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: Static");
                copingSkillClass = StaticCopingSkillActivity.class;
                break;
            default:
                Log.v(Constants.LOG_TAG, "createIntentFromCopingSkill: None (default)");
                // TODO default coping skill settings?
                copingSkillClass = StaticCopingSkillActivity.class;
        }

        result = new Intent(activity, copingSkillClass);
        return result;
    }

}
