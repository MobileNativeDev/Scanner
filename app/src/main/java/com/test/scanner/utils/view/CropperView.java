package com.test.scanner.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SizeF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.test.scanner.R;
import com.test.scanner.utils.android.DimensionUtils;

public class CropperView extends View {

    private int color = Color.WHITE;
    private float strokeWidth = DimensionUtils.dpToPixels(4);
    private float cornerRadius = DimensionUtils.dpToPixels(8);
    private Paint paint;
    private final RectF bounds = new RectF();
    private boolean isResizing = false;
    private final SizeF resizeArea = new SizeF(
            DimensionUtils.dpToPixels(16), DimensionUtils.dpToPixels(16)
    );
    private Bitmap resizeIcon;
    private BoundsUpdatedListener boundsUpdatedListener;

    public CropperView(Context context) {
        super(context);
        setupPaint();
    }

    public CropperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        obtainAttributes(context, attrs);
        setupPaint();
        loadResizeIcon(context);
    }

    public CropperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
        setupPaint();
        loadResizeIcon(context);
    }

    public CropperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        obtainAttributes(context, attrs);
        setupPaint();
        loadResizeIcon(context);
    }

    private void obtainAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.getTheme().obtainStyledAttributes(
                attributeSet, R.styleable.CropperView, 0, 0
        );
        try {
            color = attrs.getColor(R.styleable.CropperView_color, Color.WHITE);
            strokeWidth = attrs.getDimension(
                    R.styleable.CropperView_strokeWidth, DimensionUtils.dpToPixels(4)
            );
            cornerRadius = attrs.getDimension(
                    R.styleable.CropperView_cornerRadius, DimensionUtils.dpToPixels(8)
            );
        } finally {
            attrs.recycle();
        }
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
    }

    private void loadResizeIcon(Context context) {
        resizeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_resize);
        resizeIcon = Bitmap.createScaledBitmap(
                resizeIcon,
                (int) resizeArea.getWidth(),
                (int) resizeArea.getHeight(),
                false
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        bounds.left = (float) left;
        bounds.top = (float) top;
        bounds.right = (float) right;
        bounds.bottom = (float) bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float right = bounds.right - bounds.left - cornerRadius / 2;
        float bottom = bounds.bottom - bounds.top - cornerRadius / 2;
        canvas.drawRoundRect(
                cornerRadius / 2,
                cornerRadius / 2,
                right,
                bottom,
                cornerRadius,
                cornerRadius,
                paint
        );
        canvas.drawBitmap(
                resizeIcon,
                right - resizeArea.getWidth(),
                bottom - resizeArea.getHeight(),
                paint
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInResizeArea(event)) {
                    isResizing = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isResizing) {
                    float right = event.getRawX();
                    float bottom = event.getRawY();
                    if (right - 2 * strokeWidth > bounds.left) {
                        bounds.right = right;
                    }
                    if (bottom - 2 * strokeWidth > bounds.top) {
                        bounds.bottom = bottom;
                    }
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = (int) (bounds.right - bounds.left);
                    layoutParams.height = (int) (bounds.bottom - bounds.top);
                    setLayoutParams(layoutParams);
                    invalidate();
                    if (boundsUpdatedListener != null) {
                        boundsUpdatedListener.onBoundsUpdated(bounds);
                    }
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isResizing = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isInResizeArea(MotionEvent motionEvent) {
        return motionEvent.getX() > getWidth() - resizeArea.getWidth()
                && motionEvent.getY() > getHeight() - resizeArea.getHeight();
    }

    public void setBoundsUpdatedListener(BoundsUpdatedListener boundsUpdatedListener) {
        this.boundsUpdatedListener = boundsUpdatedListener;
    }

    public interface BoundsUpdatedListener {
        void onBoundsUpdated(RectF bounds);
    }
}
