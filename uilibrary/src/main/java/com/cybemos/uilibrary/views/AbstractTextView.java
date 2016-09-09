package com.cybemos.uilibrary.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.cybemos.uilibrary.R;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public abstract class AbstractTextView extends View {

    protected static final float SIZE_NOT_DEFINED = -1;

    protected float mTextSize;

    public AbstractTextView(Context context) {
        super(context);
        init(context, null);
    }

    public AbstractTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AbstractTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AbstractTextView,
                0, 0);
        mTextSize = a.getDimension(R.styleable.AbstractTextView_sizeText, SIZE_NOT_DEFINED);
        a.recycle();
    }

    @SuppressWarnings("unused")
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        postInvalidate();
    }

}
