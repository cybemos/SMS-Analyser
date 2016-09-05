package com.cybemos.uilibrary;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

public class SwipeTouchListener implements OnTouchListener {

    private final static String TAG = "SwipeTouchListener";

    private ListView mListView;
    private SwipeListener mListener;

    public SwipeTouchListener(@NonNull ListView listView) {
        mListView = listView;
    }

    public enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    /*public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }

    public Action getAction() {
        return mSwipeDetected;
    }*/

    public boolean onTouch(View v, MotionEvent event) {
        boolean consumed = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                break;
            case MotionEvent.ACTION_MOVE:
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // horizontal swipe detection
                //if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        if (mListener != null) {
                            mListener.onRightSwipe(downX, upX);
                        }
                        return true;
                    }
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        if (mListener != null) {
                            mListener.onLeftSwipe(downX, upX);
                        }
                        return true;
                    }
                /*} else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        Log.i(TAG, "Swipe Top to Bottom");
                        mSwipeDetected = Action.TB;
                        return false;
                    }
                    if (deltaY > 0) {
                        Log.i(TAG, "Swipe Bottom to Top");
                        mSwipeDetected = Action.BT;
                        return false;
                    }
                }*/
                consumed = true;
                break;
        }
        return consumed;
    }

    interface SwipeListener {
        void onRightSwipe(float baseX, float x);
        void onLeftSwipe(float baseX, float x);
    }

}
