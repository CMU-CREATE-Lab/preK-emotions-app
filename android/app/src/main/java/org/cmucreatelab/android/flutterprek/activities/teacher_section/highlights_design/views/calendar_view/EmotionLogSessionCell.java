package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.ColorConstants;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.SessionWithSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmotionLogSessionCell extends ConstraintLayout {

    private final ConstraintLayout sessionContainerConstraintLayout;
    private final ConstraintLayout sessionHeaderConstraintLayout;
    private final TextView textViewSessionTimestamp;
    private final TextView textViewSessionDuration;

    private static final SimpleDateFormat startTimeDateFormat = new SimpleDateFormat("hh:mm a");

    enum CellViewEmotion {
        HAPPY,
        EXCITED,
        SAD,
        MAD,
        SCARED
    }

    // TODO hardcoded helper method (perhaps there's another way to properly fetch this info, but only once?)
    public static CellViewEmotion uuidToCellViewEmotion(String emotionUuid) {
        //"emotions" :
        //  [
        //    { "name": "Happy", "uuid": "emotion1", "imageFileUuid": "ic_happy" },
        //    { "name": "Sad", "uuid": "emotion2", "imageFileUuid": "ic_sad" },
        //    { "name": "Mad", "uuid": "emotion3", "imageFileUuid": "ic_mad" },
        //    { "name": "Scared", "uuid": "emotion5", "imageFileUuid": "ic_scared" },
        //    { "name": "Excited", "uuid": "emotion6", "imageFileUuid": "ic_excited" }
        //  ],
        if (emotionUuid != null) {
            if (emotionUuid.equals("emotion1")) {
                return CellViewEmotion.HAPPY;
            }
            if (emotionUuid.equals("emotion2")) {
                return CellViewEmotion.SAD;
            }
            if (emotionUuid.equals("emotion3")) {
                return CellViewEmotion.MAD;
            }
            if (emotionUuid.equals("emotion5")) {
                return CellViewEmotion.SCARED;
            }
            if (emotionUuid.equals("emotion6")) {
                return CellViewEmotion.EXCITED;
            }
        }
        Log.w(Constants.LOG_TAG, String.format("Could not find CellViewEmotion to match with uuid '%s' (returning null)", emotionUuid));
        return null;
    }


    public static EmotionLogSessionCell generate(Context context, CellViewEmotion cellViewEmotion, SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        EmotionLogSessionCell emotionLogSessionCellView = new EmotionLogSessionCell(context, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(2, 2, 2, 2);
        emotionLogSessionCellView.setLayoutParams(params);
        emotionLogSessionCellView.setBackgroundColor(cellViewEmotion);
        emotionLogSessionCellView.setTextForTimestamp(sessionWithSessionCopingSkills);
        emotionLogSessionCellView.setTextForDuration(sessionWithSessionCopingSkills);
        emotionLogSessionCellView.setCopingSkills(sessionWithSessionCopingSkills);
        // TODO click listener?
        return emotionLogSessionCellView;
    }


    private void setTextForTimestamp(SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        String result;
        Date startedAt = sessionWithSessionCopingSkills.session.getStartedAt();
        result = startTimeDateFormat.format(startedAt);
        textViewSessionTimestamp.setText(result);
    }


    private void setTextForDuration(SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        Date startedAt = sessionWithSessionCopingSkills.session.getStartedAt();
        Date endedAt = sessionWithSessionCopingSkills.session.getEndedAt();
        long duration;
        if (endedAt == null) {
            Log.w(Constants.LOG_TAG, String.format("EmotionLogSessionCell got endedAt null for session %s", sessionWithSessionCopingSkills.session.getUuid()));
            duration = 0;
        } else {
            duration = (endedAt.getTime() - startedAt.getTime()) / 1000;
        }
        String minutes = String.valueOf(duration / 60);
        String seconds = String.valueOf(duration % 60);
        textViewSessionDuration.setText(String.format("%sm%ss", minutes, seconds));
    }


    private void setCopingSkills(SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        // TODO coping skills (with sessionContainerConstraintLayout)
        List<SessionCopingSkill> list = sessionWithSessionCopingSkills.sessionCopingSkills;
    }


    private void setBackgroundColor(CellViewEmotion cellViewEmotion) {
        String color;
        switch (cellViewEmotion) {
            case HAPPY:
                color = ColorConstants.HEX_HAPPY;
                break;
            case EXCITED:
                color = ColorConstants.HEX_EXCITED;
                break;
            case SAD:
                color = ColorConstants.HEX_SAD;
                break;
            case MAD:
                color = ColorConstants.HEX_MAD;
                break;
            case SCARED:
                color = ColorConstants.HEX_SCARED;
                break;
            default:
                Log.w(Constants.LOG_TAG, "EmotionLogSessionCellView.setBackgroundColor could not parse CellViewEmotion; default to gray.");
                color = "#aaaaaa";
        }
        sessionContainerConstraintLayout.setBackgroundColor(Color.parseColor(color));
    }


    public EmotionLogSessionCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);

        this.sessionContainerConstraintLayout = findViewById(R.id.sessionContainerConstraintLayout);
        this.sessionHeaderConstraintLayout = findViewById(R.id.sessionHeaderConstraintLayout);
        this.textViewSessionTimestamp = findViewById(R.id.textViewSessionTimestamp);
        this.textViewSessionDuration = findViewById(R.id.textViewSessionDuration);
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log_dow_item;
    }

}
