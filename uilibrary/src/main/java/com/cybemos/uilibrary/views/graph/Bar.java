package com.cybemos.uilibrary.views.graph;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Bar extends GraphElement {

    private Position mPosition;

    public Bar() {
        super();
        mPosition = Position.CENTER;
    }

    @SuppressWarnings("unused")
    public void showTextAt(Position position) {
        mPosition = position;
    }

    public Position getTextPosition() {
        return mPosition;
    }

    public enum Position {
        SOUTH,
        CENTER,
        NORTH
    }

}
