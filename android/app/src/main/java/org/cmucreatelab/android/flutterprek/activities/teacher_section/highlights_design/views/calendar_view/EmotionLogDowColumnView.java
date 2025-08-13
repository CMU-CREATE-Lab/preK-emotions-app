package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class EmotionLogDowColumnView extends ConstraintLayout {

    private final TextView textViewTitleDayOfWeek;
    private final TextView textViewTitleDate;


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


    public EmotionLogDowColumnView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getResourceIdForLayout(), this);

        this.textViewTitleDayOfWeek = findViewById(R.id.textViewTitleDayOfWeek);
        this.textViewTitleDate = findViewById(R.id.textViewTitleDate);

        initializeWithAttributeSet(context, attrs);
    }


    public int getResourceIdForLayout() {
        return R.layout._highlights_design__view_calendar_emotion_log_dow_column;
    }

}
