package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.check_in.DisplayEmotionCheckInActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.ChooseEmotionActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.ChooseEmotionAndTalkAboutItActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_AUDIO_FILE;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_CHOOSE_ANOTHER;
import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_MESSAGE;

public class SessionTracker {

    public static final String ITINERARY_INDEX = "itinerary_index";
    public static final boolean promptHeartbeatForCheckin = false;
    public static final boolean promptDisplayEmotionForCheckIn = true;

    private final Session roomSession;
    private final SessionMode sessionMode;
    private final Date startedAt;
    private final Student student;
    private final boolean audioIsDisabled;
    private final ArrayList<SelectedEmotion> selectedEmotions = new ArrayList<>();
    private final ItineraryItemToIntentMapper itineraryItemToIntentMapper = new ItineraryItemToIntentMapper(this);
    public final Context appContext;

    private boolean emotionPromptDisplayed = false, isFinished = false;
    private Date finishedAt;

    public enum SessionMode {
        NORMAL, CHECK_IN
    }

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
            somethingElseAudio = "audio_something_else";
        }

        intent.putExtra(INTENT_CHOOSE_ANOTHER, true);
        intent.putExtra(INTENT_BACKGROUND_COLOR, backgroundColor);
        intent.putExtra(INTENT_MESSAGE, somethingElseMessage);
        intent.putExtra(INTENT_AUDIO_FILE, somethingElseAudio);
    }


    private void insertSessionModel() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(appContext).sessionDAO().insert(roomSession);
            }
        });
    }


    private void updateSessionModel() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(appContext).sessionDAO().update(roomSession);
            }
        });
    }


    public SessionTracker(Context appContext, StudentWithCustomizations student) {
        this(appContext, new Date(), student, SessionMode.NORMAL);
    }


    public SessionTracker(Context appContext, StudentWithCustomizations student, SessionMode sessionMode) {
        this(appContext, new Date(), student, sessionMode);
    }


    public SessionTracker(Context appContext, Date startedAt, StudentWithCustomizations student, SessionMode sessionMode) {
        this.startedAt = startedAt;
        this.student = student.student;
        // TODO you need to make sure that coping skills/post coping skills are not available as well.
        this.audioIsDisabled = student.disableAudio();
        this.sessionMode = sessionMode;

        this.appContext = appContext;
        this.roomSession = new Session(this.student.getUuid(), this.startedAt);
        insertSessionModel();
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


    /**
     * Construct an intent for the next activity to be run in the session.
     *
     * @param currentActivity
     * @return The Intent object for the next activity to be passed to startActivity().
     */
    public Intent getNextIntent(AbstractActivity currentActivity) {
        if (!emotionPromptDisplayed) {
            emotionPromptDisplayed = true;
            Class chooseEmotionClass = (Constants.CHOOSE_EMOTION_WITH_TALK_ABOUT_IT_OPTION && !audioIsDisabled) ? ChooseEmotionAndTalkAboutItActivity.class : ChooseEmotionActivity.class;
            return new Intent(currentActivity, chooseEmotionClass);
        } else {
            // check-in will end the session after an emotion is selected
            if (sessionMode == SessionMode.CHECK_IN) {
                // TODO probably remove heartbeat prompt
//                if (promptHeartbeatForCheckin) {
//                    Intent intent = new Intent(currentActivity, HeartBeatingActivity.class);
                if (promptDisplayEmotionForCheckIn) {
                    Intent intent = new Intent(currentActivity, DisplayEmotionCheckInActivity.class);
                    intent.putExtra(ITINERARY_INDEX, 0);
                    return intent;
                } else {
                    endSession();
                    return SessionTracker.getIntentForEndSession(currentActivity);
                }
            }
            if (selectedEmotions.size() > 0) {
                SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
                if (!selectedEmotion.promptDisplayed) {
                    selectedEmotion.promptDisplayed = true;
                    return new Intent(currentActivity, ChooseCopingSkillActivity.class);
                }
                if (selectedEmotion.selectedCopingSkills.size() > 0) {
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


    /**
     * Construct an intent for the next activity to be run in the session from within an ItineraryItem.
     *
     * @param currentActivity
     * @param index the index (usually sequenceId) of the ItineraryItem that is currently running.
     * @return The Intent object for the next activity to be passed to startActivity().
     */
    public Intent getNextIntentFromItinerary(AbstractActivity currentActivity, int index) {
        // check-in will end the session after prompting for heartbeat
        if (sessionMode == SessionMode.CHECK_IN) {
            endSession();
            return SessionTracker.getIntentForEndSession(currentActivity);
        }
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            if (selectedEmotion.selectedCopingSkills.size() > 0) {
                SelectedCopingSkill selectedCopingSkill = selectedEmotion.selectedCopingSkills.get(selectedEmotion.selectedCopingSkills.size() - 1);
                if (index < selectedCopingSkill.itineraryItems.size()) {
                    Intent intent;
                    ItineraryItem itineraryItem = selectedCopingSkill.itineraryItems.get(index);
                    int incrementedIndex = index + 1;
                    intent = itineraryItemToIntentMapper.createIntentFromItineraryItem(currentActivity, itineraryItem);
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
        roomSession.setEmotionUuid(emotion.getUuid());
        updateSessionModel();
    }


    public void onSelectedCopingSkill(AbstractActivity currentActivity, CopingSkill copingSkill, List<ItineraryItem> itineraryItems) {
        if (selectedEmotions.size() > 0) {
            SelectedEmotion selectedEmotion = selectedEmotions.get(selectedEmotions.size() - 1);
            selectedEmotion.selectedCopingSkills.add(new SelectedCopingSkill(copingSkill, itineraryItems));
        }
    }


    /**
     * Ends the session.
     *
     * @return true if the state changes from not finished to finished, false otherwise.
     */
    public boolean endSession() {
        if (!isFinished) {
            isFinished = true;
            finishedAt = new Date();
            roomSession.setEndedAt(finishedAt);
            updateSessionModel();
            return true;
        }
        return false;
    }

}
