package com.cybemos.uilibrary.views.graph;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cybemos.uilibrary.views.AbstractTextView;

import java.util.ArrayList;

/**
 * Represents a graph of Bars.
 * @see Bar
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class BarGraph extends AbstractTextView {

    /**
     * Tag of the class.
     * Usefull for retrieve traces using regex
     */
    @SuppressWarnings("unused")
    private static final String TAG = "BarGraph";

    /**
     * The rectangle used to draw all the bars.
     * It is initialized once and save to prevent memory loss as seen in
     * <a href="https://developer.android.com/training/custom-views/optimizing-view.html>Optimizing view</a>
     */
    private RectF mRect;
    /**
     * Used to set colors and text fonts.
     * It is initialized once and save to prevent memory loss as seen in
     * <a href="https://developer.android.com/training/custom-views/optimizing-view.html>Optimizing view</a>
     */
    private Paint mPaint;

    /**
     * Represents the titles of each bar.
     */
    private ArrayList<String> mLabels;
    /**
     * Data of bars.
     */
    private ArrayList<BarData> bars;
    /**
     * min value of all bars, at max it is equals to zero
     */
    private float mMinValue;
    /**
     * max value of all bars, at min it is equals to zero
     */
    private float mMaxValue;
    /**
     * Index of the last bar the user clicked
     */
    private int mLastBarTouchedIndex;
    /**
     * Listener called when a touch event is detected
     */
    private BarTouchListener mListener;

    public BarGraph(Context context) {
        super(context);
        init(context, null);
    }

    public BarGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BarGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Add a bar to the graph
     * @param bar some bar
     */
    public void addBar(@NonNull Bar bar) {
        bars.add(new BarData(bar));
    }

    /**
     * Remove all bars from the graph
     */
    public void removeAllBars() {
        bars.clear();
    }

    /**
     * Update all the labels
     * @param labels labels for bars
     */
    public void setLabels(String... labels) {
        mLabels.clear();
        if (labels != null) {
            for (String label : labels) {
                if (label != null) {
                    mLabels.add(label);
                }
            }
        }
    }

    /**
     * Update the bar listener
     * @param listener some listener
     */
    public void setBarTouchListener(BarTouchListener listener) {
        mListener = listener;
    }

    /**
     * Called to update the min and max value
     */
    private void barsDataChanged() {
        mMinValue = mMaxValue = 0;
        if (bars != null) {
            for (BarData barData : bars) {
                mMinValue = Math.min(mMinValue, barData.mBar.getValue());
                mMaxValue = Math.max(mMaxValue, barData.mBar.getValue());
                barData.mSelected = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        barsDataChanged();
        int maxHeight, maxWidth;
        int padding = 5;
        maxWidth = canvas.getWidth();
        maxHeight = canvas.getHeight() - 30 - padding * 2;
        float diffY = mMaxValue - mMinValue;
        int len = bars.size();
        float y0 = (diffY != 0) ? maxHeight * mMaxValue / diffY : maxHeight;
        float tx = (len != 0) ? ((float) maxWidth) / len : 0f;

        mRect.left = 0;

        float textSize = 30;
        if (mTextSize != SIZE_NOT_DEFINED) {
            textSize = mTextSize;
        }

        for (BarData barData : bars) {
            if (barData.mSelected) {
                mPaint.setColor(barData.mBar.getSelectedColor());
            } else {
                mPaint.setColor(barData.mBar.getColor());
            }
            mRect.right = mRect.left + tx;
            float value = barData.mBar.getValue();
            if (value >= 0) {
                mRect.bottom = y0;
                mRect.top = maxHeight * (mMaxValue - value) / diffY;
            } else {
                mRect.top = y0;
                mRect.bottom = maxHeight * (mMaxValue - value) / diffY;
            }
            barData.mRect.set(mRect);
            canvas.drawRect(mRect, mPaint);

            String text;
            if ((text = barData.mBar.getText()) != null) {
                Bar.Position position = barData.mBar.getTextPosition();
                mPaint.setTextSize(textSize);
                mPaint.setAntiAlias(true);
                mPaint.setColor(barData.mBar.getTextColor());
                float lenStr = mPaint.measureText(text);
                float x = mRect.centerX() - lenStr / 2;
                float y = 0;
                switch (position) {
                    case CENTER:
                        y = mRect.centerY() + mPaint.getTextSize() / 2;
                        break;
                    case SOUTH:
                        y = mRect.bottom - 10;
                        break;
                    case NORTH:
                        y = mRect.top + mPaint.getTextSize();
                        break;
                }
                canvas.drawText(text, x, y, mPaint);
            }

            mRect.left = mRect.right;
        }


        mRect.left = 0;
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textSize);
        canvas.drawLine(mRect.left, maxHeight + padding, maxWidth, maxHeight + padding, mPaint);
        int index = 0;
        for (String label : mLabels) {
            if (index < bars.size()) {
                mRect.right = mRect.left + tx;
                float lenStr = mPaint.measureText(label);
                float x = mRect.centerX() - lenStr / 2;
                canvas.drawText(label, x, maxHeight + padding * 3 + mPaint.getTextSize() / 2, mPaint);
                mRect.left = mRect.right;
                index++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consume = false;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = 0;
                for (BarData barData : bars) {
                    if (barData.mRect.contains(event.getX(), event.getY())) {
                        barData.mSelected = true;
                        mLastBarTouchedIndex = index;
                        consume = true;
                    } else {
                        barData.mSelected = false;
                    }
                    index++;
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mLastBarTouchedIndex != -1 && mListener != null) {
                    mListener.onGraphClicked(this,
                            bars.get(mLastBarTouchedIndex).mBar);
                    consume = true;
                }
            case MotionEvent.ACTION_CANCEL:
                mLastBarTouchedIndex = -1;
                for (BarData sliceData : bars) {
                    sliceData.mSelected = false;
                }
                postInvalidate();
                break;
            default:
                if (mLastBarTouchedIndex != -1) {
                    consume = true;
                }
        }
        return consume;
    }

    /**
     * Initialize the bar graph
     * @param context not used
     * @param attrs not used
     */
    @SuppressWarnings("unused")
    private void init(Context context, AttributeSet attrs) {
        bars = new ArrayList<>();
        mLabels = new ArrayList<>();
        mMinValue = mMaxValue = 0;
        mLastBarTouchedIndex = -1;
        mListener = null;

        mRect = new RectF();
        mPaint = new Paint();
    }

    /**
     * Graphic data for a Bar.
     * Prevent reallocation.
     */
    private static class BarData {
        private final Bar mBar;
        private final RectF mRect;
        private boolean mSelected;

        BarData(@NonNull Bar bar) {
            mBar = bar;
            mSelected = false;
            mRect = new RectF();
        }
    }

}
