package com.cybemos.uilibrary.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cybemos.uilibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Legend extends View {

    private final static String TAG = "Legend";
    private LegendListener mListener;
    private Path path;
    private Paint mPaint;
    private RectF rect;
    private List<Elem> elements;
    private Elem[][] elemList;
    private float[] sizes;
    //private Rect dimensions;
    private boolean drawCompleted;
    @Nullable
    private String title;
    private int numberOfColumns;
    private float spacing;
    private int colorText;

    public Legend(Context context) {
        super(context);
        init(context, null);
    }

    public Legend(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Legend(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public Legend(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    public void setLegendListener(LegendListener listener) {
        mListener = listener;
    }

    @SuppressWarnings("unused")
    public synchronized void setTitle(@Nullable String title) {
        setTitle(title, true);
    }

    private synchronized void setTitle(@Nullable String title, boolean invalidate) {
        this.title = title;
        if (title != null && title.equals("")) {
            this.title = null;
        }
        if (invalidate) {
            postInvalidate();
        }
    }

    @SuppressWarnings("unused")
    public synchronized void setSpacing(float spacing) {
        setSpacing(spacing, true);
    }

    private synchronized void setSpacing(float spacing, boolean invalidate) {
        if (spacing > 0) {
            this.spacing = spacing;
            if (invalidate) {
                postInvalidate();
            }
        }
    }

    @SuppressWarnings("unused")
    public synchronized void setNumberOfColumns(int numberOfColumns) {
        setNumberOfColumns(numberOfColumns, true);
    }

    private synchronized void setNumberOfColumns(int numberOfColumns, boolean invalidate) {
        if (numberOfColumns > 0) {
            this.numberOfColumns = numberOfColumns;
            if (invalidate) {
                postInvalidate();
            }
        }
    }

    @SuppressWarnings("unused")
    public synchronized void setColorText(@ColorInt int colorText) {
        this.colorText = colorText;
        postInvalidate();
    }

    @SuppressWarnings("unused")
    public synchronized void addElement(String text, @ColorInt int color) {
        elements.add(new Elem(text, color));
        postInvalidate();
    }

    @SuppressWarnings("unused")
    public synchronized void resetContent() {
        elements.clear();
        postInvalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        drawCompleted = false;
        title = null;
        numberOfColumns = 1;
        spacing = 5f;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Legend,
                0, 0);
        setNumberOfColumns(a.getInt(R.styleable.Legend_numberOfColumns, 1), false);
        setTitle(a.getString(R.styleable.Legend_titleText), false);
        setSpacing(a.getDimension(R.styleable.Legend_spacing, 5f), false);
        colorText = a.getColor(R.styleable.Legend_textColor, Color.BLACK);
        a.recycle();

        elements = new ArrayList<>();
        path = new Path();
        mPaint = new Paint();
        rect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float takenWidth, takenHeight;
        float lengthColor = widthSize * 0.1f;
        initElements();
        takenWidth = measureWidth(lengthColor, lengthColor);
        takenHeight = measureHeight(lengthColor, lengthColor);
        float percent = takenWidth / takenHeight;

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        height = heightSize;
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        height = (int) (width / percent);
                        break;
                }
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        height = heightSize;
                        width = (int) (height * percent);
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        width = height = (int) measureWidth(20, 20);
                        break;
                }
                break;
        }

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(width, widthSize);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    @SuppressWarnings("HardCodedStringLiteral")
    @Override
    public void onDraw(Canvas canvas) {
        drawCompleted = false;
        int width, height;
        float left, top, takenWidth, takenHeight, baseLeft, baseTop, ratioWidth, ratioHeight;
        width = canvas.getWidth();
        height = canvas.getHeight();
        left = top = spacing;

        float lengthColor = width * 0.1f;

        initElements();

        takenWidth = measureWidth(lengthColor, lengthColor);
        takenHeight = measureHeight(lengthColor, lengthColor);

        float spacingLength = 0;
        if (title != null) {
            spacingLength = spacing * 2;
        }
        if (elemList.length > 0) {
            spacingLength += elemList[0].length * spacing;
        }

        ratioWidth = width / takenWidth;
        ratioHeight = (height - spacingLength) / (takenHeight - spacingLength);

        if (ratioWidth > ratioHeight) {
            lengthColor = lengthColor * ratioHeight;
            takenWidth = measureWidth(lengthColor, lengthColor);
            takenHeight = measureHeight(lengthColor, lengthColor);
            left = (width - takenWidth) / 2;
        } else {
            lengthColor = lengthColor * ratioWidth;
            takenWidth = measureWidth(lengthColor, lengthColor);
            takenHeight = measureHeight(lengthColor, lengthColor);
            top = (height - takenHeight) / 2;
        }

        canvas.drawColor(Color.TRANSPARENT);

        Log.i(TAG, "taken width : " + takenWidth + ", width : " + width);
        Log.i(TAG, "taken height : " + takenHeight + ", height : " + height);

        rect.left = left - spacing;
        rect.top = top - spacing;
        rect.bottom = rect.top + takenHeight + spacing;
        rect.right = rect.left + takenWidth + spacing;
        /*mPaint.setColor(Color.LTGRAY);
        canvas.drawRect(rect, mPaint);

        dimensions.left = rect.left;
        dimensions.top = rect.top;
        dimensions.bottom = rect.bottom;
        dimensions.right = rect.right;*/

        if (title != null) {
            path.reset();
            path.addRect(left, top, width, top + lengthColor, Path.Direction.CW);
            mPaint.reset();
            mPaint.setColor(colorText);
            mPaint.setTextSize(lengthColor);
            mPaint.setAntiAlias(true);
            canvas.drawTextOnPath(title, path, spacing, lengthColor * 0.8f, mPaint);

            top = top + lengthColor + spacing;
        }

        baseLeft = left;
        baseTop = top;

        int i = 0, j;
        for (Elem[] anElemList : elemList) {
            left = baseLeft;
            for (int a = 0; a < i; a++) {
                left += sizes[a];
            }
            j = 0;
            for (Elem elem : anElemList) {
                top = baseTop + j * (lengthColor + spacing);
                mPaint.reset();
                mPaint.setColor(elem.color);
                rect.left = left;
                rect.top = top;
                rect.bottom = rect.top + lengthColor;
                rect.right = rect.left + lengthColor;
                canvas.drawRect(rect, mPaint);

                mPaint.setColor(colorText);
                mPaint.setTextSize(lengthColor);
                mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                mPaint.setAntiAlias(true);
                path.reset();
                rect.right = rect.left + sizes[i];
                path.addRect(rect.left, rect.top, width, rect.bottom, Path.Direction.CW);
                canvas.drawTextOnPath(elem.text, path, lengthColor + spacing, lengthColor * 0.8f, mPaint);

                elem.dimensions.left = rect.left;
                elem.dimensions.top = rect.top;
                elem.dimensions.bottom = rect.bottom;
                elem.dimensions.right = rect.right;

                j++;
            }
            i++;
        }
        drawCompleted = true;
    }

    private void initElements() {
        int size = elements.size();
        if (size % numberOfColumns > 0) {
            size /= numberOfColumns;
            size += 1;
        } else {
            size /= numberOfColumns;
        }
        elemList = new Elem[numberOfColumns][size];
        int i = 0, j = 0;
        for (Elem elem : elements) {
            elemList[i][j++] = elem;
            if (j >= elemList[i].length) {
                j = 0;
                i++;
            }
        }
    }

    private float measureWidth(float colorSize, float textSize) {
        float len = 0;
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        if (title != null) {
            len = mPaint.measureText(title);
        }
        if (elements.size() > 0) {
            int elementsByColumn = elemList[0].length;
            sizes = new float[numberOfColumns];
            float l, tmp;
            Elem elem;
            for (int j = 0 ; j < elementsByColumn ; j++) {
                l = 0;
                for (int i = 0 ; i < numberOfColumns ; i++) {
                    elem = elemList[i][j];
                    if (elem != null) {
                        tmp = colorSize + spacing * 3 + mPaint.measureText(elem.text);
                        sizes[i] = Math.max(tmp, sizes[i]);
                        l += tmp;
                    }
                }
                len = Math.max(l, len);
            }
        } else {
            sizes = new float[0];
        }
        return len;
    }

    private float measureHeight(float colorSize, float textSize) {
        float len;
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        float max = Math.max(colorSize, textSize);
        //len = elements.size() * (max + spacing);
        len = 0f;
        if (elemList.length > 0) {
            len = elemList[0].length * (max + spacing);
        }
        if (title != null) {
            len += textSize + spacing * 2;
        }
        return len;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consume = false;
        if (drawCompleted) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mListener != null) {
                        int index = 0;
                        for (Elem elem : elements) {
                            if (elem.dimensions.contains(event.getX(), event.getY())) {
                                mListener.onLegendClick(index, elem);
                                consume = true;
                            }
                            index++;
                        }
                    }
                    break;
            }
        }
        return consume;
    }

    public interface LegendListener {
        void onLegendClick(int index, LegendEvent event);
    }

    public interface LegendEvent {
        @NonNull String getText();
        @ColorInt int getColor();
        void setText(@NonNull String text);
        void setColor(@ColorInt int color);
    }

    private class Elem implements LegendEvent {

        private String text;
        @ColorInt
        private int color;
        private final RectF dimensions;

        Elem(String text, @ColorInt int color) {
            this.text = text;
            this.color = color;
            dimensions = new RectF();
        }

        @Override
        @NonNull
        public String getText() {
            return text;
        }

        @Override
        @ColorInt
        public int getColor() {
            return color;
        }

        @Override
        public void setText(@NonNull String text) {
            this.text = text;
            invalidate();
        }

        @Override
        public void setColor(@ColorInt int color) {
            this.color = color;
            invalidate();
        }
    }

}
