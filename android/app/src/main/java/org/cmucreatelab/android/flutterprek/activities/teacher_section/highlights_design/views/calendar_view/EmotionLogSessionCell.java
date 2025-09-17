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

import com.mikhaellopez.circularimageview.CircularImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.ColorConstants;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.SessionWithSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmotionLogSessionCell extends ConstraintLayout {

    private final ConstraintLayout sessionContainerConstraintLayout;
    private final ConstraintLayout sessionHeaderConstraintLayout;
    private final LinearLayout copingSkillsLinearLayout;
    private final TextView textViewSessionTimestamp;
    private final TextView textViewSessionDuration;

    private long sessionDuration;

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


    // TODO hardcoded helper method (perhaps there's another way to properly fetch this info, but only once?)
    public static int uuidToCopingSkillResource(String copingSkillUuid) {
        //"copingSkills":
        //  [
        //    { "uuid": "coping_skill_14", "name": "Conduct", "imageFileUuid": "ic_conducting" },
        //    { "uuid": "coping_skill_18", "name": "Cuddle", "imageFileUuid": "ic_cuddle_alternative" },
        //    { "uuid": "coping_skill_1", "name": "Flower Breathing", "imageFileUuid": "ic_flower_breathing" },
        //    { "uuid": "coping_skill_5", "name": "Jumping Jacks", "imageFileUuid": "ic_jumping_jacks" }
        //  ],
        if (copingSkillUuid != null) {
            if (copingSkillUuid.equals("coping_skill_14")) {
                // wand
                return R.drawable.ic_conducting;
            }
            if (copingSkillUuid.equals("coping_skill_18")) {
                // sheep
                return R.drawable.ic_cuddle_alternative;
            }
            if (copingSkillUuid.equals("coping_skill_1")) {
                // flower
                return R.drawable.ic_flower_breathing;
            }
            if (copingSkillUuid.equals("coping_skill_5")) {
                // jumping jacks
                return R.drawable.ic_jumping_jacks;
            }
        }
        Log.w(Constants.LOG_TAG, String.format("Could not find Coping Skill match with uuid '%s'", copingSkillUuid));
        // TODO default image?
        return R.drawable.ic_placeholder;
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
        emotionLogSessionCellView.calculateTextForDuration(sessionWithSessionCopingSkills);
        emotionLogSessionCellView.setCopingSkills(context, sessionWithSessionCopingSkills);
        // TODO click listener?
        return emotionLogSessionCellView;
    }


    private static CircularImageView generate(Context context, SessionCopingSkill sessionCopingSkill) {
        //<de.hdodenhof.circleimageview.CircleImageView
        //                android:layout_width="20dp"
        //                android:layout_height="20dp"
        //                app:civ_border_width="1dp"
        //                app:civ_border_color="@android:color/black"
        //                android:src="@drawable/ic_jumping_jacks"
        //                android:layout_marginStart="2dp"
        //                />
        CircularImageView result = new CircularImageView(context, null);

        int dp1 = Util.pixelsFromDp(context, 1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                20*dp1,
                20*dp1
        );
        params.setMargins(2*dp1, 0, 0, 0);
        result.setLayoutParams(params);
        result.setBorderWidth(dp1);
        result.setBorderColor(R.color.black);
        result.setImageResource(uuidToCopingSkillResource(sessionCopingSkill.getCopingSkillUuid()));

        return result;
    }


    // TODO @tasota remove hanging zeroes?
    public static String getSessionDurationStringFrom(long duration) {
        String result;
        String minutes = String.valueOf(duration / 60);
        String seconds = String.valueOf(duration % 60);
        result = String.format("%sm%ss", minutes, seconds);
        return result;
    }


    private void setTextForTimestamp(SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        String result;
        Date startedAt = sessionWithSessionCopingSkills.session.getStartedAt();
        result = startTimeDateFormat.format(startedAt);
        textViewSessionTimestamp.setText(result);
    }


    private void calculateTextForDuration(SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        Date startedAt = sessionWithSessionCopingSkills.session.getStartedAt();
        Date endedAt = sessionWithSessionCopingSkills.session.getEndedAt();
        long duration;
        if (endedAt == null) {
            Log.w(Constants.LOG_TAG, String.format("EmotionLogSessionCell got endedAt null for session %s", sessionWithSessionCopingSkills.session.getUuid()));
            duration = 0;
        } else {
            duration = (endedAt.getTime() - startedAt.getTime()) / 1000;
        }
        textViewSessionDuration.setText(getSessionDurationStringFrom(getSessionDuration()));
        setSessionDuration(duration);
    }


    private void setCopingSkills(Context context, SessionWithSessionCopingSkills sessionWithSessionCopingSkills) {
        copingSkillsLinearLayout.removeAllViews();
        List<SessionCopingSkill> list = sessionWithSessionCopingSkills.sessionCopingSkills;
        for (SessionCopingSkill scs: list) {
            copingSkillsLinearLayout.addView(generate(context, scs));
        }
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
        this.copingSkillsLinearLayout = findViewById(R.id.copingSkillsLinearLayout);
        this.textViewSessionTimestamp = findViewById(R.id.textViewSessionTimestamp);
        this.textViewSessionDuration = findViewById(R.id.textViewSessionDuration);
    }


    public long getSessionDuration() {
        return sessionDuration;
    }


    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log_dow_item;
    }

}
