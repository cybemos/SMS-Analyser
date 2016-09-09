package com.cybemos.uilibrary.views.graph;

import android.graphics.Color;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class GraphElement {

    private float mValue;
    private int mColor;
    private String mText;
    private int mTextColor;

    public GraphElement() {
        mValue = 0;
        mColor = Color.BLUE;
        mText = null;
        mTextColor = Color.BLACK;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
    }

    public int getColor() {
        return mColor;
    }

    public int getSelectedColor() {
        return darkenColor(mColor);
    }

    public void setColor(int color) {
        mColor = color;
    }

    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

}
