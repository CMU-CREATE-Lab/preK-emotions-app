package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerRainbowCopingSkillProcess {

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

    private final FlowerRainbowCopingSkillActivity flowerCopingSkillActivity;


    private void setVisibilityForResources(@IdRes int[] resources, int visibility) {
        for (int resource: resources) {
            flowerCopingSkillActivity.findViewById(resource).setVisibility(visibility);
        }
    }


    private void setTextForTitle(@StringRes int stringResource) {
        TextView textView = flowerCopingSkillActivity.findViewById(R.id.textViewTitle);
        textView.setText(stringResource);
    }


    public FlowerRainbowCopingSkillProcess(FlowerRainbowCopingSkillActivity flowerCopingSkillActivity) {
        this.flowerCopingSkillActivity = flowerCopingSkillActivity;
    }


    public void goToStep(final StepNumber stepNumber) {
        final @IdRes int[] viewsToDisplay;
        final @StringRes int stringResourceForTitle;

        if (stepNumber == StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG) {
            viewsToDisplay = new int[] {
                    R.id.imageViewSilhouette,
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
                    R.id.imageViewSilhouette,
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
                // set title (for overlay only)
                if (stepNumber == StepNumber.STEP_4_OVERLAY) {
                    ((TextView)flowerCopingSkillActivity.findViewById(R.id.textViewOverlayTitle)).setText(R.string.coping_skill_flower_overlay);
                }
                // TODO refactor array check (force only once, then log warning if both exist?)
                // trigger animation whenever the view is in the array (ASSERT: only one will be in the array)
                for (int value: viewsToDisplay) {
                    if (value == R.id.imageViewBreatheOut) {
                        animateBreatheOut();
                    }
                    if (value == R.id.imageViewBreatheIn) {
                        animateBreatheIn();
                    }
                }
            }
        });
    }

    // TODO refactor these attributes, methods
    // animate clipdrawable taken from:  https://stackoverflow.com/questions/12127476/how-to-display-an-imageview-progressively-down-from-top-to-bottom
    private void animateBreatheOut() {
        ImageView breatheOut = flowerCopingSkillActivity.findViewById(R.id.imageViewBreatheOut);
        breatheOut.setVisibility(View.VISIBLE);
        animate(breatheOut);
    }
    private void animateBreatheIn() {
        ImageView breatheIn = flowerCopingSkillActivity.findViewById(R.id.imageViewBreatheIn);
        breatheIn.setVisibility(View.VISIBLE);
        animate(breatheIn);
    }

    private int mLevel = 0;
    private Handler handler = new Handler();
    private Drawable animatedDrawable;
    private Runnable animateImage = new Runnable() {
        @Override
        public void run() {
            doAnimateBreath();
        }
    };
    private void animate(ImageView imageView) {
        mLevel = 0;
        animatedDrawable = imageView.getDrawable();
        animatedDrawable.setLevel(0);
        handler.post(animateImage);
    }

    private void doAnimateBreath() {
        flowerCopingSkillActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO notice if you don't end with exactly 10000 the image will remain partially clipped
                mLevel += 100;
                animatedDrawable.setLevel(mLevel);
                if (mLevel <= 10000) {
                    handler.postDelayed(animateImage, 20);
                } else {
                    handler.removeCallbacks(animateImage);
                }
            }
        });
    }

}
