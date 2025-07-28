package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ArcViewOverlay extends View {

    private Paint arcPaint;
    private Paint textPaint;
    private List<Integer> segmentColors = new ArrayList<>();
    private List<Float> segmentAngles = new ArrayList<>();
    private float arcWidth = 60f;
    private String centerText = null;

    public ArcViewOverlay(Context context) {
        super(context);
        init();
    }

    public ArcViewOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcViewOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.BUTT); // Hard edges

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(48f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Proper radius that ensures arc is centered and not clipped
        float radius = Math.min(centerX, centerY) - (arcWidth / 2f);

        RectF arcRect = new RectF(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        );

        float startAngle = -90f;

        for (int i = 0; i < segmentAngles.size(); i++) {
            arcPaint.setColor(segmentColors.get(i % segmentColors.size()));
            arcPaint.setStrokeWidth(arcWidth);
            canvas.drawArc(arcRect, startAngle, segmentAngles.get(i), false, arcPaint);
            startAngle += segmentAngles.get(i);
        }

        if (centerText != null) {
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float textY = centerY - (fm.ascent + fm.descent) / 2;
            canvas.drawText(centerText, centerX, textY, textPaint);
        }
    }

    // Public setters
    public void setSegmentColors(List<Integer> colors) {
        this.segmentColors = colors;
        invalidate();
    }

    public void setSegmentAngles(List<Float> angles) {
        this.segmentAngles = angles;
        invalidate();
    }

    public void setArcWidth(float width) {
        this.arcWidth = width;
        invalidate();
    }

    public void setCenterText(String text) {
        this.centerText = text;
        invalidate();
    }

    public void setTextSize(float size) {
        this.textPaint.setTextSize(size);
        invalidate();
    }
}