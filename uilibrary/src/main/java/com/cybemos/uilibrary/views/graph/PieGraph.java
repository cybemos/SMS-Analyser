package com.cybemos.uilibrary.views.graph;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cybemos.uilibrary.R;

import java.util.ArrayList;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class PieGraph extends View {

    private static final String TAG = "PieGraph";
    private Path path;
    private RectF rect;
    private Paint mPaint;
    private float mPercentCircleRatio;
    private ArrayList<SliceData> slices;
    private float total;
    private SliceTouchListener mSliceTouchListener;
    private int mLastSliceTouchedIndex;

    public PieGraph(Context context) {
        super(context);
        init(context, null);
    }

    public PieGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PieGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        updateSlicesData();
        int maxHeight, maxWidth;
        maxWidth = canvas.getWidth();
        maxHeight = canvas.getHeight();
        int min = Math.min(maxHeight, maxWidth);

        float ratio = min * mPercentCircleRatio / 200f;

        float arc_sweep, arc_offset;

        arc_offset = 270;
        for (SliceData sliceData : slices) {
            rect.top = rect.left = 0;
            rect.bottom = rect.right = min;
            sliceData.mRegion.set(0, 0, min, min);
            //Log.i(TAG, "degrees : "+sliceData.mDegrees);
            arc_sweep = sliceData.mDegrees;

            path.reset();
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
            sliceData.mRegion.setPath(path, sliceData.mRegion);
            if (sliceData.mSelected) {
                mPaint.setColor(sliceData.mSlice.getSelectedColor());
            } else {
                mPaint.setColor(sliceData.mSlice.getColor());
            }
            canvas.drawPath(path, mPaint);


            String text;
            if ((text = sliceData.mSlice.getText()) != null) {
                mPaint.setTextSize(30);
                mPaint.setAntiAlias(true);
                mPaint.setColor(sliceData.mSlice.getTextColor());
                float len = mPaint.measureText(text);
                rect.top = rect.left = 0;
                rect.bottom = rect.right = min;
                float centerX, centerY;
                centerX = rect.centerX();
                centerY = rect.centerY();
                float distance = min / 4;
                float degrees = arc_offset + sliceData.mDegrees / 2;
                degrees %= 360;
                float ty = (float) Math.sin(toRadian(degrees));
                float tx = (float) Math.cos(toRadian(degrees));
                float x = centerX + tx * distance - len / 2;
                float y = centerY + ty * distance + mPaint.getTextSize() / 2;
                canvas.drawText(text, x, y, mPaint);
            }

            arc_offset += arc_sweep;
            arc_offset %= 360;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();
        rect = new RectF();
        mPaint = new Paint();
        slices = new ArrayList<>();
        total = 0;
        mLastSliceTouchedIndex = -1;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieGraph,
                0, 0);
        mPercentCircleRatio = Math.max(0f, (Math.min(a.getFloat(R.styleable.PieGraph_percentCircleRatio, 100f), 100f)));
        a.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consume = false;
        int action = event.getAction();
        //Log.i(TAG, action+"");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = 0;
                for (SliceData sliceData : slices) {
                    if (sliceData.mSlice.canBeClicked()
                            && sliceData.mRegion.contains((int) event.getX(), (int) event.getY())) {
                        sliceData.mSelected = true;
                        mLastSliceTouchedIndex = index;
                        consume = true;
                    } else {
                        sliceData.mSelected = false;
                    }
                    index++;
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mLastSliceTouchedIndex != -1 && mSliceTouchListener != null) {
                    mSliceTouchListener.onSliceClicked(this,
                            slices.get(mLastSliceTouchedIndex).mSlice);
                    consume = true;
                }
            case MotionEvent.ACTION_CANCEL:
                mLastSliceTouchedIndex = -1;
                for (SliceData sliceData : slices) {
                    sliceData.mSelected = false;
                }
                postInvalidate();
                break;
            default:
                if (mLastSliceTouchedIndex != -1) {
                    consume = true;
                }
        }
        return consume;
    }

    public void addSlice(@NonNull Slice slice) {
        slices.add(new SliceData(slice));
    }

    public void removeAllSlices() {
        slices.clear();
    }

    public void setSliceTouchListener(SliceTouchListener listener) {
        mSliceTouchListener = listener;
    }

    private void updateSlicesData() {
        total = 0;
        for (SliceData sliceData : slices) {
            total += sliceData.mSlice.getValue();
        }
        if (total != 0) {
            for (SliceData sliceData : slices) {
                sliceData.mDegrees = sliceData.mSlice.getValue() * 360f / total;
                sliceData.mRegion.setEmpty();
            }
        }
    }

    private static float toRadian(float degrees) {
       return degrees / 57.296f;
    }

    private static class SliceData {
        private final Slice mSlice;
        private float mDegrees;
        private final Region mRegion;
        private boolean mSelected;

        SliceData(@NonNull Slice slice) {
            mSlice = slice;
            mDegrees = 0f;
            mRegion = new Region();
            mSelected = false;
        }
    }

}
