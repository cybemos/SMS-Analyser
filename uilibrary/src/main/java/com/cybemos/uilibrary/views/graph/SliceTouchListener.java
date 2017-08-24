package com.cybemos.uilibrary.views.graph;

/**
 * Listener which handle events when a slice is clicked
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface SliceTouchListener {
    /**
     * Called when a slice is clicked
     * @param owner owner of the slice
     * @param slice slice clicked
     */
    void onSliceClicked(PieGraph owner, Slice slice);
}
