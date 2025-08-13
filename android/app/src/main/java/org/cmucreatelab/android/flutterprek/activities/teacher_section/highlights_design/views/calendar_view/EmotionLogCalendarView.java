package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

import java.util.Calendar;
import java.util.Date;

public class EmotionLogCalendarView extends ConstraintLayout {

    private final EmotionLogDowColumnView mondayDowColumnView;
    private final EmotionLogDowColumnView tuesdayDowColumnView;
    private final EmotionLogDowColumnView wednesdayDowColumnView;
    private final EmotionLogDowColumnView thursdayDowColumnView;
    private final EmotionLogDowColumnView fridayDowColumnView;
    private final EmotionLogDowColumnView saturdayDowColumnView;
    private final EmotionLogDowColumnView sundayDowColumnView;

    // TODO demo, remove later
    private boolean showWeekends = true;


    public void updateDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        updateDateTime(currentTime);
    }


    public void updateDateTime(Date dateTime) {
        // https://developer.android.com/reference/java/util/Date#toString()
        // dow mon dd hh:mm:ss zzz yyyy
        Log.v(Constants.LOG_TAG, String.format("EmotionLogCalendarView updateDateTime to %s", dateTime.toString()));

        // TODO demo placeholder (set text, days of week)
        mondayDowColumnView.setTextForDate("01/01");
        tuesdayDowColumnView.setTextForDate("02/02");
        wednesdayDowColumnView.setTextForDate("03/03");
        thursdayDowColumnView.setTextForDate("04/04");
        fridayDowColumnView.setTextForDate("05/05");
        saturdayDowColumnView.setTextForDate("06/06");
        sundayDowColumnView.setTextForDate("07/07");

        wednesdayDowColumnView.setDowColumnIsSelected(true);
        thursdayDowColumnView.setDowColumnIsActive(false);
        fridayDowColumnView.setDowColumnIsActive(false);
        saturdayDowColumnView.setDowColumnIsActive(false);
        sundayDowColumnView.setDowColumnIsActive(false);
    }


    public EmotionLogCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);

        this.mondayDowColumnView = findViewById(R.id.mondayDowColumnView);
        this.tuesdayDowColumnView = findViewById(R.id.tuesdayDowColumnView);
        this.wednesdayDowColumnView = findViewById(R.id.wednesdayDowColumnView);
        this.thursdayDowColumnView = findViewById(R.id.thursdayDowColumnView);
        this.fridayDowColumnView = findViewById(R.id.fridayDowColumnView);
        this.saturdayDowColumnView = findViewById(R.id.saturdayDowColumnView);
        this.sundayDowColumnView = findViewById(R.id.sundayDowColumnView);

        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeekends = !showWeekends;
                saturdayDowColumnView.setDowColumnIsVisible(showWeekends);
                sundayDowColumnView.setDowColumnIsVisible(showWeekends);
            }
        });
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log;
    }

}
