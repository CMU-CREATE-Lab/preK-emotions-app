package org.cmucreatelab.android.flutterprek.activities.coping_skill_flower;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerCopingSkillProcess {

    enum StepNumber {
        STEP_1A_HOLD_FLOWER_LADYBUG,
        STEP_1B_HOLD_FLOWER_HAND,
        STEP_2_SMELL,
        STEP_3_BLOW,
        STEP_4_OVERLAY
    }
    private static @IdRes int[] ALL_VIEWS = {
            R.id.overlayYesNo,
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
        final @IdRes int[] viewsToDisplay;
        final @StringRes int stringResourceForTitle;

        // TODO actions
        if (stepNumber == StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG) {
            viewsToDisplay = new int[] {
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
        } else if (stepNumber == StepNumber.STEP_1B_HOLD_FLOWER_HAND) {
            viewsToDisplay = new int[] {
                    R.id.imageViewFlowerHand,
                    R.id.imageViewFlowerStem,
                    R.id.imageViewFlowerMiddle,
                    R.id.imageViewFlowerPetal1,
                    R.id.imageViewFlowerPetal2,
                    R.id.imageViewFlowerPetal3,
                    R.id.imageViewFlowerPetal4,
                    R.id.imageViewFlowerPetal5
            };
            stringResourceForTitle = R.string.coping_skill_flower_step_1_hold;
        } else if (stepNumber == StepNumber.STEP_2_SMELL) {
            viewsToDisplay = new int[] {
                    R.id.imageViewSilhouette,
                    R.id.imageViewBreatheIn,
                    R.id.imageViewFlowerStem,
                    R.id.imageViewFlowerMiddle,
                    R.id.imageViewFlowerPetal1,
                    R.id.imageViewFlowerPetal2,
                    R.id.imageViewFlowerPetal3,
                    R.id.imageViewFlowerPetal4,
                    R.id.imageViewFlowerPetal5
            };
            stringResourceForTitle = R.string.coping_skill_flower_step_2_smell;
        } else if (stepNumber == StepNumber.STEP_3_BLOW) {
            viewsToDisplay = new int[] {
                    R.id.imageViewSilhouette,
                    R.id.imageViewBreatheOut,
                    R.id.imageViewFlowerStem,
                    R.id.imageViewFlowerMiddle,
                    R.id.imageViewFlowerPetal1,
                    R.id.imageViewFlowerPetal2,
                    R.id.imageViewFlowerPetal3,
                    R.id.imageViewFlowerPetal4,
                    R.id.imageViewFlowerPetal5
            };
            stringResourceForTitle = R.string.coping_skill_flower_step_3_blow;
        } else if (stepNumber == StepNumber.STEP_4_OVERLAY) {
            viewsToDisplay = new int[] {
                    R.id.imageViewSilhouette,
                    R.id.imageViewFlowerStem,
                    R.id.imageViewFlowerMiddle,
                    R.id.imageViewFlowerPetal1,
                    R.id.imageViewFlowerPetal2,
                    R.id.imageViewFlowerPetal3,
                    R.id.imageViewFlowerPetal4,
                    R.id.imageViewFlowerPetal5,
                    R.id.overlayYesNo
            };
            stringResourceForTitle = R.string.empty;
        } else {
            Log.e(Constants.LOG_TAG, "StepNumber not implemented: " + stepNumber.name());
            viewsToDisplay = new int[0];
            stringResourceForTitle = R.string.coping_skill_flower_placeholder;
        }

        flowerCopingSkillActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setVisibilityForResources(ALL_VIEWS, View.INVISIBLE);
                setVisibilityForResources(viewsToDisplay, View.VISIBLE);
                setTextForTitle(stringResourceForTitle);
            }
        });
    }

}
