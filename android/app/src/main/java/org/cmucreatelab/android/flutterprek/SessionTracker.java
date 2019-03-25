package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.content.Intent;

import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseEmotionActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.ItineraryItemToIntentMapper;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;

public class SessionTracker {

    public static final String ITINERARY_INDEX = "itinerary_index";

    private final Date startedAt;
    private final Student student;
    private final ArrayList<SelectedEmotion> selectedEmotions = new ArrayList<>();

    private boolean emotionPromptDisplayed = false, isFinished = false;
    private Date finishedAt;

    public static class SelectedCopingSkill {
        public final CopingSkill copingSkill;
        public final List<ItineraryItem> itineraryItems;

        public SelectedCopingSkill(CopingSkill copingSkill, List<ItineraryItem> itineraryItems) {
            this.copingSkill = copingSkill;
            this.itineraryItems = itineraryItems;
        }
    }

    public static class SelectedEmotion {
        public final Emotion emotion;
        public final List<ItineraryItem> itineraryItems;
        public final ArrayList<SelectedCopingSkill> selectedCopingSkills = new ArrayList<>();
        public boolean promptDisplayed = false;

        public SelectedEmotion(Emotion emotion, List<ItineraryItem> itineraryItems) {
            this.emotion = emotion;
            this.itineraryItems = itineraryItems;
        }
    }


    public static Intent getIntentForEndSession(AbstractActivity currentActivity) {
        Intent intent = new Intent(currentActivity, ChooseStudentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    private void populateChooseAnotherIntentWithEmotionInfo(Context context, Intent intent) {
        String backgroundColor = GlobalHandler.getInstance(context).studentSectionNavigationHandler.emotionBackgroundColor;
        String somethingElseMessage = GlobalHandler.getInstance(context).studentSectionNavigationHandler.somethingElseMessage;
        String somethingElseAudio = GlobalHandler.getInstance(context).studentSectionNavigationHandler.somethingElseAudio;
        if (backgroundColor.isEmpty()) {
            backgroundColor = "#ffffff";
        }
        if (somethingElseMessage.isEmpty()) {
            somethingElseMessage = "Would you like to try something else?";
        }
        if (somethingElseAudio.isEmpty()) {
            somethingElseAudio = "etc/audio_prompts/audio_something_else.wav";
        }

        intent.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
        intent.putExtra(INTENT_MESSAGE, somethingElseMessage);
        intent.putExtra(INTENT_AUDIO_FILE, somethingElseAudio);
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
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            return selectedEmotion.emotion;
        }
        return null;
    }


    public CopingSkill getLastSelectedCopingSkill() {
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            if (selectedEmotion.selectedCopingSkills.size() > 0) {
                SelectedCopingSkill selectedCopingSkill = selectedEmotion.selectedCopingSkills.get(selectedEmotion.selectedCopingSkills.size() - 1);
                return selectedCopingSkill.copingSkill;
            }
        }
        return null;
    }


    public Intent getNextIntent(AbstractActivity currentActivity) {
        if (!emotionPromptDisplayed) {
            emotionPromptDisplayed = true;
            return new Intent(currentActivity, ChooseEmotionActivity.class);
        } else {
            if (selectedEmotions.size() > 0) {
                SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
                if (!selectedEmotion.promptDisplayed) {
                    selectedEmotion.promptDisplayed = true;
                    return new Intent(currentActivity, ChooseCopingSkillActivity.class);
                }
                if (selectedEmotion.selectedCopingSkills.size() > 0) {
//                    SelectedCopingSkill selectedCopingSkill = selectedEmotion.selectedCopingSkills.get(selectedEmotion.selectedCopingSkills.size() - 1);
//                    if (selectedCopingSkill.itineraryItems.size() > 0) {
//                        ItineraryItem itineraryItem = selectedCopingSkill.itineraryItems.get(0);
//                        // TODO make intent from itinerary item
//                    } else {
//                        // { do something else }
//                        Intent intent = new Intent(this, nextClass);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    }
                    Intent intent = new Intent(currentActivity, ChooseCopingSkillActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    populateChooseAnotherIntentWithEmotionInfo(currentActivity.getApplicationContext(), intent);
                    return intent;
                } else {
                    // should never get here
                }
            }
            // defaults to ending the current session
            endSession();
            return SessionTracker.getIntentForEndSession(currentActivity);
        }
    }

    public Intent getNextIntentFromItinerary(AbstractActivity currentActivity, int index) {
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            if (selectedEmotion.selectedCopingSkills.size() > 0) {
                SelectedCopingSkill selectedCopingSkill = selectedEmotion.selectedCopingSkills.get(selectedEmotion.selectedCopingSkills.size() - 1);
                if (index < selectedCopingSkill.itineraryItems.size()) {
                    // TODO increment index
                    // TODO make intent from itinerary item
                    //return null;
                    ItineraryItem itineraryItem = selectedCopingSkill.itineraryItems.get(index);
                    int incrementedIndex = index + 1;

                    Intent intent = ItineraryItemToIntentMapper.createIntentFromItineraryItem(currentActivity, itineraryItem);
                    intent.putExtra(ITINERARY_INDEX, incrementedIndex);
                    return intent;
                }
            }
        }
        // defaut to "rejoin friends"
        return new Intent(currentActivity, RejoinFriendsActivity.class);
    }


    public void onSelectedEmotion(AbstractActivity currentActivity, Emotion emotion, List<ItineraryItem> itineraryItems) {
        selectedEmotions.add(new SelectedEmotion(emotion, itineraryItems));
    }


    public void onSelectedCopingSkill(AbstractActivity currentActivity, CopingSkill copingSkill, List<ItineraryItem> itineraryItems) {
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            selectedEmotion.selectedCopingSkills.add(new SelectedCopingSkill(copingSkill, itineraryItems));
        }
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
