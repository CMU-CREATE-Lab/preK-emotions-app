package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.CalendarUtil;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.Calendar;
import java.util.Date;

public class EmotionLogCalendarView extends ConstraintLayout {

    private final EmotionLogDowColumn mondayDowColumnView;
    private final EmotionLogDowColumn tuesdayDowColumnView;
    private final EmotionLogDowColumn wednesdayDowColumnView;
    private final EmotionLogDowColumn thursdayDowColumnView;
    private final EmotionLogDowColumn fridayDowColumnView;
    private final EmotionLogDowColumn saturdayDowColumnView;
    private final EmotionLogDowColumn sundayDowColumnView;

    // NOTE: bad Java compiler is bad
    private final EmotionLogDowColumn[] weekdaysColumnArray;// = {
//    private final EmotionLogDowColumn[] weekdaysColumnArray = {
//            mondayDowColumnView,
//            tuesdayDowColumnView,
//            wednesdayDowColumnView,
//            thursdayDowColumnView,
//            fridayDowColumnView,
//            saturdayDowColumnView,
//            sundayDowColumnView
//    };

    // TODO demo, remove later
    private boolean showWeekends = true;


    public void requestData(AbstractActivity activity, Student student) {
        // TODO remove?
        this.updateDateTime();

        Log.v(Constants.LOG_TAG, "called requestData");
        Calendar calendar = Calendar.getInstance();

        // we want midnight of the current day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar[] currentWeek = CalendarUtil.generateWeekFromDay(calendar);
        for (int i=0; i<7; i++) {
            weekdaysColumnArray[i].populateWithData(activity, student, currentWeek[i]);
        }
//        mondayDowColumnView.populateWithData(activity, student, currentWeek[0]);
//        tuesdayDowColumnView.populateWithData(activity, student, currentWeek[1]);
//        wednesdayDowColumnView.populateWithData(activity, student, currentWeek[2]);
//        thursdayDowColumnView.populateWithData(activity, student, currentWeek[3]);
//        fridayDowColumnView.populateWithData(activity, student, currentWeek[4]);
//        saturdayDowColumnView.populateWithData(activity, student, currentWeek[5]);
//        sundayDowColumnView.populateWithData(activity, student, currentWeek[6]);

        updateColumnsDisplay(calendar);
    }


    private void updateColumnsDisplay(Calendar calendar) {
        int offset = CalendarUtil.getDayOfWeekOffset(calendar);

        // past days (if any)
        for (int i=0; i<offset; i++) {
            weekdaysColumnArray[i].setDowColumnIsActive(true);
            weekdaysColumnArray[i].setDowColumnIsSelected(false);
        }
        // current day
        weekdaysColumnArray[offset].setDowColumnIsActive(true);
        weekdaysColumnArray[offset].setDowColumnIsSelected(true);
        // future days (of rest of the week, if any)
        for (int i=offset+1; i<7; i++) {
            weekdaysColumnArray[i].setDowColumnIsActive(false);
            weekdaysColumnArray[i].setDowColumnIsSelected(false);
        }
        // TODO @tasota check for display weekends and remove "showWeekends"
    }


    public void updateDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        updateDateTime(currentTime);
    }


    public void updateDateTime(Date dateTime) {
        // https://developer.android.com/reference/java/util/Date#toString()
        // dow mon dd hh:mm:ss zzz yyyy
        Log.v(Constants.LOG_TAG, String.format("EmotionLogCalendarView updateDateTime to %s", dateTime.toString()));
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

        this.weekdaysColumnArray = new EmotionLogDowColumn[]{
                    mondayDowColumnView,
                    tuesdayDowColumnView,
                    wednesdayDowColumnView,
                    thursdayDowColumnView,
                    fridayDowColumnView,
                    saturdayDowColumnView,
                    sundayDowColumnView
        };

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
