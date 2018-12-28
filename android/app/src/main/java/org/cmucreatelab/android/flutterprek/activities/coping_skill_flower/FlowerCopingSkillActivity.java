package org.cmucreatelab.android.flutterprek.activities.coping_skill_flower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class FlowerCopingSkillActivity extends AppCompatActivity {

    private FlowerCopingSkillProcess flowerCopingSkillProcess;
    private FlowerCopingSkillStep1Timer step1Timer;
    private FlowerStateHandler flowerStateHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_coping_skill);

        // close
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.LOG_TAG, "clicked buttonClose; now finishing activity");
                finish();
            }
        });

        flowerCopingSkillProcess = new FlowerCopingSkillProcess(this);
        step1Timer = new FlowerCopingSkillStep1Timer(flowerCopingSkillProcess);
        flowerStateHandler = new FlowerStateHandler(this);

        // test set step
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
//        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_2_SMELL);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_3_BLOW);
//            }
//        }, 2000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.LOG_TAG,"Stopping LeScan...");
        flowerStateHandler.stopScan();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // TODO need to detect when nav bar is forced to display, so it can hide again after a few seconds
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        flowerStateHandler.lookForFlower();
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
    }


    public void displayBreatheIn() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_2_SMELL);
    }


    public void displayBreatheOut() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_3_BLOW);
    }


    public void displayOverlay() {
        // TODO overlay for finished
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
    }

}
