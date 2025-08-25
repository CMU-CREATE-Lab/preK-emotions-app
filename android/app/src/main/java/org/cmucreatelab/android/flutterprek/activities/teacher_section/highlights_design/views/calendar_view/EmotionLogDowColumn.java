package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.EmbeddedDAO;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills.SessionWithSessionCopingSkills;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmotionLogDowColumn extends ConstraintLayout {

    private final TextView textViewTitleDayOfWeek;
    private final TextView textViewTitleDate;
    private final LinearLayout linearLayoutSessions;


    private String getStringForDayOfWeek(TypedArray typedArray) {
        int dayOfWeek = typedArray.getInt(R.styleable.EmotionLogDowColumnView_day_of_week, 0);
        switch (dayOfWeek) {
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thu";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";
            default:
                Log.e(Constants.LOG_TAG, String.format("getStringForDayOfWeek returning null from value %d", dayOfWeek));
                return null;
        }
    }


    private void initializeWithAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmotionLogDowColumnView);

            // app:day_of_week
            textViewTitleDayOfWeek.setText(getStringForDayOfWeek(typedArray));

            typedArray.recycle();
        }
    }


    // NOTE: this should be in the format "MM/DD" and be called upon the UI Thread
    @UiThread
    public void setTextForDate(String monthSlashDay) {
        textViewTitleDate.setText(monthSlashDay);
    }


    // selected = indicator for the "current day" (i.e. bold outline/border)
    public void setDowColumnIsSelected(boolean isSelected) {
        if (isSelected) {
            setBackgroundResource(R.drawable.background_dow_column_selected);
        } else {
            setBackgroundResource(R.drawable.background_dow_column_deselected);
        }
    }


    // active = regular or "grayed out" (for future days of week)
    public void setDowColumnIsActive(boolean isActive) {
        setAlpha(isActive ? 1.0f : 0.6f);
        // TODO demo only?
        if (!isActive) {
            // hide sessions as well
            findViewById(R.id.linearLayoutSessions).setVisibility(View.INVISIBLE);
        }
    }


    // visible = show views or completely gone (i.e. hide weekends when no data)
    public void setDowColumnIsVisible(boolean isVisible) {
        setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


    public void populateWithData(AbstractActivity activity, Student student, Calendar calendar) {
//        //dateTime.setHours();
//        Calendar c = Calendar.getInstance();
//        c.get(Calendar.DAY_OF_WEEK);
//        c.getTime()
//        calendar.getTime().getTime();

//        //long value = calendar.getTimeInMillis() / 1000;
//        long value = calendar.getTime().getTime();
//        long endTime = value + 86400; // 86400 seconds per day
        long startTime = calendar.getTimeInMillis();
        long endTime = startTime + 86400000; // ms per day (86400 seconds per day)

        AppDatabase.getInstance(activity).embeddedDAO().getSessionsWithSessionCopingSkillsForStudentBetweenTimes(student.getUuid(), startTime, endTime).observe(activity, new Observer<List<SessionWithSessionCopingSkills>>() {
            @Override
            public void onChanged(List<SessionWithSessionCopingSkills> sessionWithSessionCopingSkills) {
                Log.v(Constants.LOG_TAG, String.format("   query returned with result size %d", sessionWithSessionCopingSkills.size()));
            }
        });
    }


    public EmotionLogDowColumn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);

        this.textViewTitleDayOfWeek = findViewById(R.id.textViewTitleDayOfWeek);
        this.textViewTitleDate = findViewById(R.id.textViewTitleDate);
        this.linearLayoutSessions = findViewById(R.id.linearLayoutSessions);

        setDowColumnIsSelected(false);

        initializeWithAttributeSet(context, attrs);

        // TODO demo data
        linearLayoutSessions.addView(EmotionLogSessionCell.generate(getContext(), EmotionLogSessionCell.CellViewEmotion.MAD));
        linearLayoutSessions.addView(EmotionLogSessionCell.generate(getContext(), EmotionLogSessionCell.CellViewEmotion.MAD));
        linearLayoutSessions.addView(EmotionLogSessionCell.generate(getContext(), EmotionLogSessionCell.CellViewEmotion.SCARED));
        linearLayoutSessions.addView(EmotionLogSessionCell.generate(getContext(), EmotionLogSessionCell.CellViewEmotion.SAD));
        linearLayoutSessions.addView(EmotionLogSessionCell.generate(getContext(), EmotionLogSessionCell.CellViewEmotion.EXCITED));
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log_dow_column;
    }

}
