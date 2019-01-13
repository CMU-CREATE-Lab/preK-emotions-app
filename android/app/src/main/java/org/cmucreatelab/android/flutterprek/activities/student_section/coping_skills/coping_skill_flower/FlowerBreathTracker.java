package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import android.os.Handler;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;

public class FlowerBreathTracker {

    private static final long COUNTER_TIME_INTERVAL_IN_MILLISECONDS = 1000;
    private static final int BREATHE_IN_CYCLE_MAX = 5;
    private static final int BREATHE_OUT_CYCLE_MAX = 5;
    private static final int TOTAL_NUMBER_OF_CYCLES = 3;

    private final FlowerCopingSkillActivity activity;
    private final Listener listener;

    private final Handler handler = new Handler();
    private Runnable currentCallback;
    private int numberOfCycles = 0;
    private int currentCycleCounter = 0;
    private Runnable breatheInCallback = new Runnable() {
        @Override
        public void run() {
            currentCycleCounter++;
            if (currentCycleCounter <= BREATHE_IN_CYCLE_MAX) {
                displayFlowerFromCycleCounter(currentCycleCounter, true);
                setCurrentCallbackForHandler(breatheInCallback);
            } else {
                currentCycleCounter = 0;
                activity.displayBreatheOut();
                setCurrentCallbackForHandler(breatheOutCallback);
            }
        }
    };
    private Runnable breatheOutCallback = new Runnable() {
        @Override
        public void run() {
            currentCycleCounter++;
            if (currentCycleCounter <= BREATHE_OUT_CYCLE_MAX) {
                displayFlowerFromCycleCounter(currentCycleCounter, false);
                setCurrentCallbackForHandler(breatheOutCallback);
            } else {
                currentCycleCounter = 0;
                numberOfCycles++;
                if (numberOfCycles < TOTAL_NUMBER_OF_CYCLES) {
                    activity.displayBreatheIn();
                    setCurrentCallbackForHandler(breatheInCallback);
                } else {
                    listener.onFinishedBreathing();
                    resetTracker();
                }
            }
        }
    };


    private void displayFlowerFromCycleCounter(int counter, boolean highlighting) {
        int[] allPetals = new int[] {
                R.id.imageViewFlowerPetal1,
                R.id.imageViewFlowerPetal2,
                R.id.imageViewFlowerPetal3,
                R.id.imageViewFlowerPetal4,
                R.id.imageViewFlowerPetal5,
        };
        if (highlighting) {
            for (int i=0; i<allPetals.length; i++) {
                int resource = (i<counter) ? R.drawable.flower_petal_highlighted : R.drawable.flower_petal;
                ((ImageView)activity.findViewById(allPetals[i])).setImageResource(resource);
            }
        } else {
            for (int i=allPetals.length-1; i>=0; i--) {
                int resource = (i>=allPetals.length-counter) ? R.drawable.flower_petal : R.drawable.flower_petal_highlighted;
                ((ImageView)activity.findViewById(allPetals[i])).setImageResource(resource);
            }
        }
    }


    private void setCurrentCallbackForHandler(Runnable callback) {
        // remove current
        handler.removeCallbacks(currentCallback);
        // add new
        this.currentCallback = callback;
        handler.postDelayed(currentCallback, COUNTER_TIME_INTERVAL_IN_MILLISECONDS);
    }



    public FlowerBreathTracker(FlowerCopingSkillActivity activity, Listener listener) {
        this.activity = activity;
        this.listener = listener;
    }


    public void resetTracker() {
        numberOfCycles = 0;
        currentCycleCounter = 0;
        handler.removeCallbacks(currentCallback);
        // reset flower un-highlighted
        displayFlowerFromCycleCounter(0, true);
    }


    public void startTracker() {
        setCurrentCallbackForHandler(breatheInCallback);
    }


    public interface Listener {
        void onFinishedBreathing();
    }

}
