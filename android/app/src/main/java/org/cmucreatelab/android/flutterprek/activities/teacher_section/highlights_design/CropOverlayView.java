package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CropOverlayView extends View {

    private Paint borderPaint;
    private Paint dimPaint;
    private RectF cropRect;
    private float lastX, lastY;
    private boolean isDragging = false;
    private ImageView imageView;

    public CropOverlayView(Context context) {
        super(context);
        init();
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setImageView(ImageView iv) {
        this.imageView = iv;
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStrokeWidth(5);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        dimPaint = new Paint();
        dimPaint.setColor(Color.parseColor("#AA000000"));

        cropRect = new RectF(300, 500, 800, 1000); // initial position
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dim the rest of the screen
        Path path = new Path();
        path.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
        path.addOval(cropRect, Path.Direction.CCW);
        canvas.drawPath(path, dimPaint);

        // Draw circle inside the rect
        float radius = cropRect.width() / 2f;
        canvas.drawCircle(cropRect.centerX(), cropRect.centerY(), radius, borderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (cropRect.contains(x, y)) {
                    lastX = x;
                    lastY = y;
                    isDragging = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = x - lastX;
                    float dy = y - lastY;
                    moveRect(dx, dy);
                    lastX = x;
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                break;
        }

        return true;
    }

    private void moveRect(float dx, float dy) {
        // Constrain movement within the image bounds
        if (imageView == null || imageView.getDrawable() == null) return;

        RectF imageBounds = getImageBounds();
        if (imageBounds == null) return;

        RectF newRect = new RectF(cropRect);
        newRect.offset(dx, dy);

        if (imageBounds.contains(newRect)) {
            cropRect = newRect;
            invalidate();
        }
    }

    private RectF getImageBounds() {
        Drawable d = imageView.getDrawable();
        if (d == null) return null;

        RectF bounds = new RectF();
        Matrix m = imageView.getImageMatrix();
        float[] values = new float[9];
        m.getValues(values);

        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        int intrinsicWidth = d.getIntrinsicWidth();
        int intrinsicHeight = d.getIntrinsicHeight();

        float width = intrinsicWidth * scaleX;
        float height = intrinsicHeight * scaleY;

        bounds.left = transX;
        bounds.top = transY;
        bounds.right = transX + width;
        bounds.bottom = transY + height;

        return bounds;
    }

    public RectF getCropRect() {
        return cropRect;
    }
}

