package org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithTimeout;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.ResolvedEmotionWithImageFile;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizationsAndEmotions;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ChooseEmotionAbstractActivity extends StudentSectionActivityWithTimeout {

    private static final String filepathHowAreYouFeelingPrompt = "etc/audio_prompts/audio_how_are_you_feeling.wav";

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion, List<ItineraryItem> itineraryItems) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            if (!activityShouldHandleOnClickEvents()) {
                Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                return;
            }
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

            // track selection with GlobalHandler
            globalHandler.studentSectionNavigationHandler.emotionUuid = emotion.getUuid();

            // next activity
            globalHandler.getSessionTracker().onSelectedEmotion(ChooseEmotionAbstractActivity.this, emotion, itineraryItems);
            //Intent nextActivityIntent = globalHandler.getSessionTracker().getNextIntent(ChooseEmotionAbstractActivity.this);
            Intent nextActivityIntent = new Intent(ChooseEmotionAbstractActivity.this, DisplayEmotionActivity.class);
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_UUID, emotion.getUuid());
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_NAME, emotion.getName());
            nextActivityIntent.putExtra(DisplayEmotionActivity.EMOTION_IMAGEFILE_UUID, emotion.getImageFileUuid());

            startActivity(nextActivityIntent);
        }
    };


    private final Executor executor = Executors.newSingleThreadExecutor();
    private LiveData<StudentWithCustomizationsAndEmotions> getLiveDataForStudentEmotionImages(String studentUuid) {
        MutableLiveData<StudentWithCustomizationsAndEmotions> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            StudentWithCustomizationsAndEmotions studentWithCustomizationsAndEmotions = AppDatabase.getInstance(this).embeddedDAO().getStudentWithEmotionsAndCustomImageFiles(studentUuid);
            liveData.postValue(studentWithCustomizationsAndEmotions);
        });
        return liveData;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imagePlayAudioView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activityShouldHandleOnClickEvents()) {
                    Log.w(Constants.LOG_TAG, "ignoring onclick event when activityShouldHandleOnClickEvents is false");
                    return;
                }
                //playAudioHowAreYouFeeling();
                // NOTE: user interaction cancels reprompt timer
                cancelTimerToReprompt();
                playAudio(filepathHowAreYouFeelingPrompt);
            }
        });

        GlobalHandler globalHandler = GlobalHandler.getInstance(this);
        if (!globalHandler.currentSessionIsActive()) {
            Log.e(Constants.LOG_TAG, "ChooseEmotionActivity.onCreate while current session is not active; ending session");
            globalHandler.endCurrentSession(this);
        } else {
            Student student = globalHandler.getSessionTracker().getStudent();
            //
            // NOTE: an example row for use in the DB Seed file under "customizations" table:
            // { "uuid": "custom_emotion1", "basedOnUuid": "emotion1", "key": "imageFileUuid", "value": "ic_yoga", "ownerUuid": "student1" }
            //
            // TODO consider emotions with classroom/student ownership
            AppDatabase.getInstance(getApplicationContext()).embeddedDAO().getResolvedEmotionsForStudent(student.getUuid()).observe(ChooseEmotionAbstractActivity.this, new Observer<List<ResolvedEmotionWithImageFile>>() {
                @Override
                public void onChanged(List<ResolvedEmotionWithImageFile> resolvedEmotionWithImageFiles) {
                    // replaces adapter code from above
                    Log.v(Constants.LOG_TAG, String.format("Room DB getResolvedEmotionsForStudent() returned with list results size = %d", resolvedEmotionWithImageFiles.size()));
                    final List<Emotion> emotionList = Util.EmotionMapper.fromResolvedList(resolvedEmotionWithImageFiles);
                    GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                    emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionAbstractActivity.this, emotionList, listener));
                }
            });
        }
    }


    public void playAudioHowAreYouFeeling() {
        startTimerToRepromptAndPlayAudio(filepathHowAreYouFeelingPrompt);
    }

}



