package org.cmucreatelab.android.flutterprek.activities.coping_skill_flower;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;

public class FlowerCopingSkillProcess {

    enum StepNumber {
        STEP_1_HOLD_FLOWER
    }
    private static @IdRes int[] ALL_VIEWS = {
            R.id.imageViewCloud1,
            R.id.imageViewCloud2,
            R.id.imageViewCloud3,
            R.id.imageViewSilhouette,
            R.id.imageViewBreatheIn,
            R.id.imageViewBreatheOut,
            R.id.imageViewFlowerHand,
            R.id.imageViewFlowerLadyBug,
            R.id.imageViewFlowerLadyBugArrow,
            R.id.imageViewFlowerStem,
            R.id.imageViewFlowerMiddle,
            R.id.imageViewFlowerPetal1,
            R.id.imageViewFlowerPetal2,
            R.id.imageViewFlowerPetal3,
            R.id.imageViewFlowerPetal4,
            R.id.imageViewFlowerPetal5
    };

    private final FlowerCopingSkillActivity flowerCopingSkillActivity;


    private void setVisibilityForResources(@IdRes int[] resources, int visibility) {
        for (int resource: resources) {
            flowerCopingSkillActivity.findViewById(resource).setVisibility(visibility);
        }
    }


    private void setTextForTitle(@StringRes int stringResource) {
        TextView textView = flowerCopingSkillActivity.findViewById(R.id.textViewTitle);
        textView.setText(stringResource);
    }


    public FlowerCopingSkillProcess(FlowerCopingSkillActivity flowerCopingSkillActivity) {
        this.flowerCopingSkillActivity = flowerCopingSkillActivity;
    }


    public void goToStep(StepNumber stepNumber) {
        @IdRes int[] viewsToDisplay;
        @StringRes int stringResourceForTitle;

        // TODO actions
        if (stepNumber == StepNumber.STEP_1_HOLD_FLOWER) {
            viewsToDisplay = new int[]{
                    R.id.imageViewFlowerLadyBug,
                    R.id.imageViewFlowerLadyBugArrow,
                    R.id.imageViewFlowerStem,
                    R.id.imageViewFlowerMiddle,
                    R.id.imageViewFlowerPetal1,
                    R.id.imageViewFlowerPetal2,
                    R.id.imageViewFlowerPetal3,
                    R.id.imageViewFlowerPetal4,
                    R.id.imageViewFlowerPetal5
            };
            stringResourceForTitle = R.string.coping_skill_flower_step_1_hold;
        } else {
            viewsToDisplay = new int[0];
            stringResourceForTitle = R.string.coping_skill_flower_placeholder;
        }

        setVisibilityForResources(ALL_VIEWS, View.INVISIBLE);
        setVisibilityForResources(viewsToDisplay, View.VISIBLE);
        setTextForTitle(stringResourceForTitle);
    }

}
