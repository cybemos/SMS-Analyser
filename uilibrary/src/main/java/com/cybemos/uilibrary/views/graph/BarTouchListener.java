package com.cybemos.uilibrary.views.graph;

/**
 * Listener which handle events when a bar is clicked
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface BarTouchListener {
    /**
     * Called when a bar is clicked
     * @param owner owner of the bar
     * @param bar bar clicked
     */
    void onGraphClicked(BarGraph owner, Bar bar);
}
