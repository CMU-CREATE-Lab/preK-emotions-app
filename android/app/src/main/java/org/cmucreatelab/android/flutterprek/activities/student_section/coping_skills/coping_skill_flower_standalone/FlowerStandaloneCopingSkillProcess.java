package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerStandaloneCopingSkillProcess {

    enum StepNumber {
        STEP_1_HOLD,
        STEP_2_SMELL,
        STEP_3_BLOW,
        STEP_4_OVERLAY
    }

    private static @IdRes int[] ALL_VIEWS = {
            R.id.overlayYesNo,
            R.id.imageViewFlowerHand,
            R.id.imageViewCloud1,
            R.id.imageViewCloud2,
            R.id.imageViewCloud3,
            R.id.imageViewSilhouette,
            R.id.imageViewBreatheIn,
            R.id.imageViewBreatheOut,
            R.id.imageViewFlowerStem,
            R.id.imageViewFlowerMiddle,
            R.id.imageViewFlowerPetal1,
            R.id.imageViewFlowerPetal2,
            R.id.imageViewFlowerPetal3,
            R.id.imageViewFlowerPetal4,
            R.id.imageViewFlowerPetal5
    };

    private final FlowerStandaloneCopingSkillActivity flowerCopingSkillActivity;


    private void setVisibilityForResources(@IdRes int[] resources, int visibility) {
        for (int resource: resources) {
            flowerCopingSkillActivity.findViewById(resource).setVisibility(visibility);
        }
    }


    private void setTextForTitle(@StringRes int stringResource) {
        TextView textView = flowerCopingSkillActivity.findViewById(R.id.textViewTitle);
        textView.setText(stringResource);
    }


    private void animateFlower(boolean breatheIn) {
        // breatheIn rotates forward, backward otherwise
        int fromDegrees = 0;
        int toDegrees = breatheIn ? 20 : -20;
        // breatheIn pivots from bottom-center of the layout, bottom-right otherwise
        float pivotXValue = breatheIn ? 0.5f : 1.0f;
        float pivotYValue = 1.0f;
        // pivot layout relative to self
        int pivotType = Animation.RELATIVE_TO_SELF;

        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotType, pivotXValue, pivotType, pivotYValue);
        // animate for 1.3 seconds, then repeat animation in reverse
        rotateAnimation.setDuration(1300);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        flowerCopingSkillActivity.findViewById(R.id.constraintLayoutFlower).startAnimation(rotateAnimation);
    }


    public FlowerStandaloneCopingSkillProcess(FlowerStandaloneCopingSkillActivity flowerCopingSkillActivity) {
        this.flowerCopingSkillActivity = flowerCopingSkillActivity;
    }


    public void goToStep(final StepNumber stepNumber) {
        final @IdRes int[] viewsToDisplay;
        final @StringRes int stringResourceForTitle;

        if (stepNumber == StepNumber.STEP_1_HOLD) {
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
            stringResourceForTitle = R.string.coping_skill_flower_standalone_step_1;
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
            animateFlower(true);
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
            animateFlower(false);
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
                // set title (for overlay only)
                if (stepNumber == StepNumber.STEP_4_OVERLAY) {
                    ((TextView)flowerCopingSkillActivity.findViewById(R.id.textViewOverlayTitle)).setText(R.string.coping_skill_flower_overlay);
                }
            }
        });
    }

}
