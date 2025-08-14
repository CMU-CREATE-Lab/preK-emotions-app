package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.calendar_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.ColorConstants;

public class EmotionLogSessionCellView extends ConstraintLayout {

    private final ConstraintLayout sessionContainerConstraintLayout;
    private final ConstraintLayout sessionHeaderConstraintLayout;
    private final TextView textViewSessionTimestamp;
    private final TextView textViewSessionDuration;

    enum CellViewEmotion {
        HAPPY,
        EXCITED,
        SAD,
        MAD,
        SCARED
    }


    // TODO demo only for generating rows
    public static EmotionLogSessionCellView generate(Context context, CellViewEmotion cellViewEmotion) {
        EmotionLogSessionCellView emotionLogSessionCellView = new EmotionLogSessionCellView(context, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(2, 2, 2, 2);
        emotionLogSessionCellView.setLayoutParams(params);
        emotionLogSessionCellView.setBackgroundColor(cellViewEmotion);
        return emotionLogSessionCellView;
    }


    public void setBackgroundColor(CellViewEmotion cellViewEmotion) {
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


    public EmotionLogSessionCellView(@NonNull Context context, @Nullable AttributeSet attrs) {
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
