package com.cybemos.uilibrary.views.graph;

/**
 * Represents a Bar in a Bar graph.
 * @see BarGraph
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Bar extends GraphElement {

    /**
     * Position of the bar in the bar graph
     */
    private Position mPosition;

    /**
     * Builder of the class.
     * Initialize the default position at center
     */
    public Bar() {
        super();
        mPosition = Position.CENTER;
    }

    /**
     * Update the current position you want to show the text at.
     * If the position is {@link Position#CENTER}, the text will be at the center of the Bar.
     * If the position is {@link Position#SOUTH}, the text will be below the Bar.
     * If the position is {@link Position#NORTH}, the text will be above the Bar.
     * @param position some position
     */
    @SuppressWarnings("unused")
    public void showTextAt(Position position) {
        mPosition = position;
    }

    /**
     * Return the text position
     * @return the text position
     */
    public Position getTextPosition() {
        return mPosition;
    }

    /**
     * All existing position for the text
     * @see #showTextAt(Position)
     */
    public enum Position {
        SOUTH,
        CENTER,
        NORTH
    }

}
