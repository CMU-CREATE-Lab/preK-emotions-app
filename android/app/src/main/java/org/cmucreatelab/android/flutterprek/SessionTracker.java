package org.cmucreatelab.android.flutterprek;

import android.content.Intent;

import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseEmotionActivity;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionTracker {

    private final Date startedAt;
    private final Student student;
    private final ArrayList<SelectedEmotion> selectedEmotions = new ArrayList<>();

    private boolean emotionPromptDisplayed = false, isFinished = false;
    private Date finishedAt;

    public static class SelectedEmotion {
        public final Emotion emotion;
        public final List<ItineraryItem> itineraryItems;
        public boolean promptDisplayed = false;

        public SelectedEmotion(Emotion emotion, List<ItineraryItem> itineraryItems) {
            this.emotion = emotion;
            this.itineraryItems = itineraryItems;
        }
    }


    public SessionTracker(Student student) {
        this(new Date(), student);
    }


    public SessionTracker(Date startedAt, Student student) {
        this.startedAt = startedAt;
        this.student = student;
    }


    public boolean isFinished() {
        return isFinished;
    }


    public Student getStudent() {
        return student;
    }


    public Emotion getLastSelectedEmotion() {
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size()-1);
            return selectedEmotion.emotion;
        }
        return null;
    }


    public Intent getNextIntent(AbstractActivity currentActivity) {
        if (!emotionPromptDisplayed) {
            emotionPromptDisplayed = true;
            return new Intent(currentActivity, ChooseEmotionActivity.class);
        } else {
            if (selectedEmotions.size() > 0) {
                SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size()-1);
                if (!selectedEmotion.promptDisplayed) {
                    selectedEmotion.promptDisplayed = true;
                    return new Intent(currentActivity, ChooseCopingSkillActivity.class);
                }
            }
            return new Intent();
        }
    }


    public void onSelectedEmotion(AbstractActivity currentActivity, Emotion emotion, List<ItineraryItem> itineraryItems) {
        selectedEmotions.add(new SelectedEmotion(emotion, itineraryItems));
    }


    public boolean endSession() {
        if (!isFinished) {
            isFinished = true;
            finishedAt = new Date();
            return true;
        }
        return false;
    }

}
