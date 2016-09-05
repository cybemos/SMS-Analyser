package com.cybemos.uilibrary.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cybemos.uilibrary.R;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class DeterminateProgressBar extends View {

    private Path path;
    private RectF rect;
    private Paint mPaint;
    private float mMax;
    private float mValue;
    private float textSize;
    private int colorProgressBar;
    private int colorText;
    private boolean showText;
    private boolean mNoInvalidate;

    public DeterminateProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public DeterminateProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeterminateProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DeterminateProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;

        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxHeight, maxWidth;
        maxWidth = canvas.getWidth();
        maxHeight = canvas.getHeight();
        int min = Math.min(maxHeight, maxWidth);
        rect.top = rect.left = 0;
        rect.bottom = rect.right = min;

        float ratio = min * 20f / 200f;

        int arc_sweep = (int) (mValue * 360 / mMax);
        int arc_offset = 270;

        if (arc_sweep == 360) {
            path.addArc(rect, arc_offset, arc_sweep);
        } else {
            path.arcTo(rect, arc_offset, arc_sweep);
        }
        rect.top = rect.left = ratio;
        rect.bottom = rect.right = min - ratio;
        if (arc_sweep == 360) {
            path.addArc(rect, arc_offset + arc_sweep, -arc_sweep);
        } else {
            path.arcTo(rect, arc_offset + arc_sweep, -arc_sweep);
        }
        path.close();

        mPaint.setColor(colorProgressBar);
        canvas.drawPath(path, mPaint);

        if (showText) { // show text in percent at the center
            mPaint.setTextSize(textSize);
            mPaint.setAntiAlias(true);
            String str = Integer.toString((int) (mValue * 100 / mMax));
            str += " %";
            mPaint.setColor(colorText);
            float len = mPaint.measureText(str);
            float x = rect.centerX() - len / 2;
            float y = rect.centerY() + mPaint.getTextSize() / 2;
            canvas.drawText(str, x, y, mPaint);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();
        rect = new RectF();
        mPaint = new Paint();

        mNoInvalidate = true;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DeterminateProgressBar,
                0, 0);
        mMax = a.getFloat(R.styleable.DeterminateProgressBar_max, 100f);
        mValue = Math.min(a.getFloat(R.styleable.DeterminateProgressBar_value, 0f), mMax);
        textSize = a.getDimension(R.styleable.DeterminateProgressBar_textSize, 22f);
        colorProgressBar = a.getColor(R.styleable.DeterminateProgressBar_colorProgressBar, Color.BLACK);
        colorText = a.getColor(R.styleable.DeterminateProgressBar_colorText, Color.BLACK);
        showText = a.getBoolean(R.styleable.DeterminateProgressBar_textShown, true);

        a.recycle();
        mNoInvalidate = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setValue(mValue+1);
        return true;
    }

    @Override
    public void postInvalidate() {
        if (!mNoInvalidate) {
            super.postInvalidate();
        }
    }

    public void setMax(@FloatRange(from = 0, fromInclusive = false) float max) {
        float oldValue = mMax;
        mMax = max;
        if (oldValue != mMax) {
            postInvalidate();
        }
    }

    public void setValue(@FloatRange(from = 0) float value) {
        float oldValue = mValue;
        mValue = Math.min(value, mMax);
        if (oldValue != mValue) {
            postInvalidate();
        }
    }

}
