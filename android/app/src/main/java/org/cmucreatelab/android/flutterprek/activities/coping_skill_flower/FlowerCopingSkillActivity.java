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

        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowerStateHandler.initializeState();
            }
        });
        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        flowerStateHandler.initializeState();
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

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

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
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_4_OVERLAY);
    }

}
