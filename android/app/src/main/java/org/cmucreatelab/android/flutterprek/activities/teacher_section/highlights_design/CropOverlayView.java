package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class CropOverlayView extends View {

    private Paint circleBorderPaint;
    private Paint dimPaint;
    private Paint squareBorderPaint;
    private Paint cornerPaint;
    private float cornerLength = 40f; // length of each arm of the L shape
    private float cornerStrokeWidth = 6f;
    private RectF cropRect;
    private ImageView imageView;
    private static final float HANDLE_RADIUS = 40f; // size of draggable corner
    private static final float MIN_CROP_SIZE = 100f; // min width/height
    private float max_crop_size = 100;
    private ScaleGestureDetector scaleDetector;
    private TouchArea currentTouch = TouchArea.NONE;
    private OnCropRectChangedListener cropRectChangedListener;
    public void setOnCropRectChangedListener(OnCropRectChangedListener listener) {
        this.cropRectChangedListener = listener;
    }
    private enum TouchArea {
        NONE, INSIDE, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    public interface OnCropRectChangedListener {
        void onCropRectChanged(RectF newRect);
    }

    private float lastTouchX, lastTouchY;

    public CropOverlayView(Context context) {
        super(context);
        init(context);
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setImageView(ImageView iv) {
        this.imageView = iv;
    }

    private void init(Context context) {
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        circleBorderPaint = new Paint();
        circleBorderPaint.setColor(Color.WHITE);
        circleBorderPaint.setStrokeWidth(5);
        circleBorderPaint.setStyle(Paint.Style.STROKE);
        circleBorderPaint.setAntiAlias(true);
        circleBorderPaint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));

        squareBorderPaint = new Paint();
        squareBorderPaint.setColor(Color.WHITE); // or any color you prefer
        squareBorderPaint.setStyle(Paint.Style.STROKE);
        squareBorderPaint.setStrokeWidth(4f);
        squareBorderPaint.setAntiAlias(true);
        squareBorderPaint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));

        cornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornerPaint.setColor(Color.RED); // or any color
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setStrokeWidth(cornerStrokeWidth);

        dimPaint = new Paint();
        dimPaint.setColor(Color.parseColor("#AA000000"));

        //center crop box on image
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        //float boxSize = screenWidth / 2f;

        float side = Math.min(screenWidth, screenHeight);
        side*=0.2f;

        float left = (screenWidth - side) / 2f;
        float top = (screenHeight - side) / 2f;
        float right = left + side;
        float bottom = top + side;

        //resize to smaller

        cropRect = new RectF(left, top+30, right, bottom+30); // initial position

    }
    private boolean isInsideCropRect(float x, float y) {
        return cropRect.contains(x, y);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dim the rest of the screen
        Path path = new Path();
        path.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
        path.addRect(cropRect, Path.Direction.CCW);
        canvas.drawPath(path, dimPaint);

        // Draw circle inside the rect
        float radius = cropRect.width() / 2f;
        canvas.drawCircle(cropRect.centerX(), cropRect.centerY(), radius, circleBorderPaint);
        canvas.drawRect(cropRect, squareBorderPaint);


        // TOP-LEFT corner
        canvas.drawLine(cropRect.left, cropRect.top, cropRect.left + cornerLength, cropRect.top, cornerPaint); // horizontal
        canvas.drawLine(cropRect.left, cropRect.top, cropRect.left, cropRect.top + cornerLength, cornerPaint); // vertical

// TOP-RIGHT corner
        canvas.drawLine(cropRect.right, cropRect.top, cropRect.right - cornerLength, cropRect.top, cornerPaint);
        canvas.drawLine(cropRect.right, cropRect.top, cropRect.right, cropRect.top + cornerLength, cornerPaint);

// BOTTOM-LEFT corner
        canvas.drawLine(cropRect.left, cropRect.bottom, cropRect.left + cornerLength, cropRect.bottom, cornerPaint);
        canvas.drawLine(cropRect.left, cropRect.bottom, cropRect.left, cropRect.bottom - cornerLength, cornerPaint);

// BOTTOM-RIGHT corner
        canvas.drawLine(cropRect.right, cropRect.bottom, cropRect.right - cornerLength, cropRect.bottom, cornerPaint);
        canvas.drawLine(cropRect.right, cropRect.bottom, cropRect.right, cropRect.bottom - cornerLength, cornerPaint);



    }

@Override
public boolean onTouchEvent(MotionEvent event) {

    max_crop_size = getMaxCropSize();
    scaleDetector.onTouchEvent(event);

    float x = event.getX();
    float y = event.getY();

    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:

            currentTouch = getTouchedCorner(x, y);
            if (currentTouch == TouchArea.NONE && isInsideCropRect(x, y)) {
                currentTouch = TouchArea.INSIDE; // drag whole box
            }
            lastTouchX = x;
            lastTouchY = y;
            return currentTouch != TouchArea.NONE;

        case MotionEvent.ACTION_MOVE:
            RectF oldRect = new RectF(cropRect);

            if (currentTouch == TouchArea.NONE) return false;

            float dx = x - lastTouchX;
            float dy = y - lastTouchY;

            if (currentTouch == TouchArea.INSIDE) {
                // Move the entire cropRect
                cropRect.offset(dx, dy);

            } else {
                // Resize square based on corner drag (use earlier logic)
                float delta = Math.abs(dx) > Math.abs(dy) ? dx : dy;

                switch(currentTouch) {
                    case TOP_LEFT:
                        float sideTL = cropRect.right - (cropRect.left + delta);
                        sideTL = Math.max(MIN_CROP_SIZE, Math.min(max_crop_size,sideTL));
                        cropRect.left = cropRect.right - sideTL;
                        cropRect.top = cropRect.bottom - sideTL;
                        break;

                    case TOP_RIGHT:
                        float sideTR = (cropRect.right + delta) - cropRect.left;
                        sideTR = Math.max(MIN_CROP_SIZE, Math.min(max_crop_size,sideTR));
                        cropRect.right = cropRect.left + sideTR;
                        cropRect.top = cropRect.bottom - sideTR;
                        break;

                    case BOTTOM_LEFT:
                        float sideBL = cropRect.right - (cropRect.left + delta);
                        sideBL = Math.max(MIN_CROP_SIZE, Math.min(max_crop_size,sideBL));
                        cropRect.left = cropRect.right - sideBL;
                        cropRect.bottom = cropRect.top + sideBL;
                        break;

                    case BOTTOM_RIGHT:
                        float sideBR = (cropRect.right + delta) - cropRect.left;
                        sideBR = Math.max(MIN_CROP_SIZE, Math.min(max_crop_size,sideBR));
                        cropRect.right = cropRect.left + sideBR;
                        cropRect.bottom = cropRect.top + sideBR;
                        break;
                }
            }

            clampRect();
            invalidate();

            //detecting change
            if (cropRectChangedListener != null && !cropRect.equals(oldRect)) {
                cropRectChangedListener.onCropRectChanged(new RectF(cropRect));
            }

            lastTouchX = x;
            lastTouchY = y;
            return true;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            currentTouch = TouchArea.NONE;
            return true;
    }



    return super.onTouchEvent(event);
}

    private float getMaxCropSize() {
        RectF imageBounds = getImageBounds();
        if (imageBounds == null) return 100f;
        return imageBounds.height();
    }


    private void clampRect(){

        RectF imageBounds = getImageBounds();
        if (imageBounds == null) return;

        // Clamp size first: if cropRect is bigger than image bounds, resize it
        float width = cropRect.width();
        float height = cropRect.height();

        if (width > imageBounds.width()) {
            cropRect.left = imageBounds.left;
            cropRect.right = imageBounds.right;
        }
        if (height > imageBounds.height()) {
            cropRect.top = imageBounds.top;
            cropRect.bottom = imageBounds.bottom;
        }

        // Clamp edges individually to image bounds, keep size fixed if possible
        if (cropRect.left < imageBounds.left) {
            float diff = imageBounds.left - cropRect.left;
            cropRect.left += diff;
            cropRect.right += diff;
        }
        if (cropRect.right > imageBounds.right) {
            float diff = cropRect.right - imageBounds.right;
            cropRect.left -= diff;
            cropRect.right -= diff;
        }
        if (cropRect.top < imageBounds.top) {
            float diff = imageBounds.top - cropRect.top;
            cropRect.top += diff;
            cropRect.bottom += diff;
        }
        if (cropRect.bottom > imageBounds.bottom) {
            float diff = cropRect.bottom - imageBounds.bottom;
            cropRect.top -= diff;
            cropRect.bottom -= diff;
        }
    }
    private TouchArea getTouchedCorner(float x, float y) {
        if (distance(x, y, cropRect.left, cropRect.top) < HANDLE_RADIUS) {
            return TouchArea.TOP_LEFT;
        } else if (distance(x, y, cropRect.right, cropRect.top) < HANDLE_RADIUS) {
            return TouchArea.TOP_RIGHT;
        } else if (distance(x, y, cropRect.left, cropRect.bottom) < HANDLE_RADIUS) {
            return TouchArea.BOTTOM_LEFT;
        } else if (distance(x, y, cropRect.right, cropRect.bottom) < HANDLE_RADIUS) {
            return TouchArea.BOTTOM_RIGHT;
        }
        return TouchArea.NONE;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x2 - x1, y2 - y1);
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();

            // Calculate new size based on scale factor
            float newWidth = cropRect.width() * scale;
            float newHeight = cropRect.height() * scale;

            // Enforce minimum size
            float minSize = MIN_CROP_SIZE;
            float maxSize = getMaxCropSize();

            if (newWidth < minSize) newWidth = minSize;
            if (newHeight < minSize) newHeight = minSize;
            if (newWidth > maxSize) newWidth = maxSize;
            if (newHeight > maxSize) newHeight = maxSize;

            // Keep square box centered on current center
            float centerX = cropRect.centerX();
            float centerY = cropRect.centerY();

            float halfSize = newWidth / 2f;

            cropRect.left = centerX - halfSize;
            cropRect.right = centerX + halfSize;
            cropRect.top = centerY - halfSize;
            cropRect.bottom = centerY + halfSize;

            clampRect();
            invalidate();
            return true;

        }
    }

}

