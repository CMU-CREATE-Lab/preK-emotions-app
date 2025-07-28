package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.collapsible_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class CollapsibleInfoView extends ConstraintLayout implements View.OnClickListener {

    // Internal Helpers for performing Collapse/Expand animations.
    // The best combination is Collapse.withScaleY and Expand.withScaleY
    // Other Expand methods appear glitchy, and pairing differing methods will not work (e.g. pairing Expand.withScaleY() with Collapse.withTransformation())
    private static class Collapse {

        public static void withScaleY(final View view) {
            view.animate()
                    .scaleY(0f)
                    .setDuration(300)
                    .withEndAction(() -> view.setVisibility(View.GONE))
                    .start();
        }

        public static void withTransformation(final View view) {
            final int initialHeight = view.getMeasuredHeight();
            Log.v(Constants.LOG_TAG, String.format("collapse: initialHeight = %d", initialHeight));

            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        view.setVisibility(View.GONE);
                    } else {
                        view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        view.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            animation.setDuration((int)(initialHeight / view.getContext().getResources().getDisplayMetrics().density));
            view.startAnimation(animation);
        }

    }

    private static class Expand {

        public static void withScaleY(final View view) {
            view.setVisibility(View.VISIBLE);
            view.setScaleY(0f);

            view.animate()
                    .scaleY(1f)
                    .setDuration(300)
                    .start();
        }

        public static void withTransformation(final View view) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = view.getMeasuredHeight();
            Log.v(Constants.LOG_TAG, String.format("expand: targetHeight = %d", targetHeight));

            view.getLayoutParams().height = 0;
            view.setVisibility(View.VISIBLE);

            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    view.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int)(targetHeight * interpolatedTime);
                    view.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            animation.setDuration((int)(targetHeight / view.getContext().getResources().getDisplayMetrics().density));
            view.startAnimation(animation);
        }

        public static void withValueAnimator(final View view) {
            view.setVisibility(View.VISIBLE);

            // Measure the target height
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = 154;//view.getMeasuredHeight();

            // Set initial height to 0
            view.getLayoutParams().height = 0;
            view.requestLayout();

            ValueAnimator animator = ValueAnimator.ofInt(0, targetHeight);
            animator.setDuration(300); // Duration in ms
            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();
                view.getLayoutParams().height = animatedValue;
                view.requestLayout();
            });

            animator.start();
        }

    }

    private final ConstraintLayout collapsibleLayout;
    private boolean isCollapsed;


    public CollapsibleInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);
        // NOTE: the layout should be collapsed by default (View.GONE)
        this.isCollapsed = true;
        this.collapsibleLayout = findViewById(R.id.collapsibleLayout);
        collapsibleLayout.setVisibility(View.GONE);
        // TODO should collapsibleLayout have OnClickListener as well?
        //TextView textViewCollapsible = findViewById(R.id.textViewCollapsible);
        //textViewCollapsible.setText(R.string.highlights_design_info_description_placeholder);
    }


    public void setCollapsed(boolean isCollapsed) {
        boolean valueChanged = (this.isCollapsed != isCollapsed);
        if (valueChanged) {
            this.isCollapsed = isCollapsed;
            if (isCollapsed) {
                Collapse.withScaleY(collapsibleLayout);
            } else {
                Expand.withScaleY(collapsibleLayout);
            }
        }
    }


    // NOTE: make sure 'collapsibleLayout' is defined in the XML
    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_collapsible_info;
    }


    @Override
    public void onClick(View v) {
        Log.v(Constants.LOG_TAG, "CollapsibleInfoView.onClick() triggered.");
        setCollapsed(!isCollapsed);
    }

}