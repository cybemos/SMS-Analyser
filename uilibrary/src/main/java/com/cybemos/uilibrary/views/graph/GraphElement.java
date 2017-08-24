package com.cybemos.uilibrary.views.graph;

import android.graphics.Color;

/**
 * Represents a Graph Element in which you can show values
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class GraphElement {

    /**
     * Current value
     */
    private float mValue;
    /**
     * Current color
     */
    private int mColor;
    /**
     * Current text
     */
    private String mText;
    /**
     * Current text color
     */
    private int mTextColor;

    /**
     * Builder of the class
     */
    public GraphElement() {
        mValue = 0;
        mColor = Color.BLUE;
        mText = null;
        mTextColor = Color.BLACK;
    }

    /**
     * Update the current text
     * @param text some text
     */
    public void setText(String text) {
        mText = text;
    }

    /**
     * Return the current text
     * @return the current text
     */
    public String getText() {
        return mText;
    }

    /**
     * Update the text color
     * @param textColor some color
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    /**
     * Return the text color
     * @return the text color
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Return the value
     * @return the value
     */
    public float getValue() {
        return mValue;
    }

    /**
     * Update the value
     * @param value some value
     */
    public void setValue(float value) {
        mValue = value;
    }

    /**
     * Return the graph color
     * @return the graph color
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Return the selection color.
     * By default, it's a little darker than the current graph color
     * @return the selection color
     */
    public int getSelectedColor() {
        return darkenColor(mColor);
    }

    /**
     * Update the graph color
     * @param color some solor
     */
    public void setColor(int color) {
        mColor = color;
    }

    /**
     * Return a darker color than the parameter
     * @param color some color
     * @return the int value of a darker color
     */
    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

}
