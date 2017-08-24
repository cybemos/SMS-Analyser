package com.cybemos.uilibrary.adapters;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter where the user can click and/or do a selection of multiples items by long pressing on an item.
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
@SuppressWarnings("HardCodedStringLiteral")
public abstract class SelectableItemsAdapter extends BaseAdapter {

    /**
     * Tag of the class, usefull to retrieve traces using regex
     */
    private static final String TAG = "SelectableItemsAdapter";
    /**
     * Id of the checkbox
     */
    // TODO
    @IdRes
    private final int mCheckboxId;
    /**
     * Current mode of the adapter
     */
    @NonNull
    private MODE mMode;
    /**
     * Listener called when a click event is detected
     */
    private ClickListener mClickListener;
    /**
     * Listener called when a "mode change event" is detected
     */
    private OnSelectionChangeListener mModeChangeListener;
    /**
     * List of checkbox (one per item).
     */
    protected final List<Integer> mCheckedItems;

    /**
     * Builder of the class
     * @param checkboxId TODO
     */
    public SelectableItemsAdapter(@IdRes int checkboxId) {
        mCheckboxId = checkboxId;
        mMode = MODE.MODE_BASE;
        mClickListener = null;
        mModeChangeListener = null;
        mCheckedItems = new ArrayList<>();
    }

    /**
     * Set the mode of the adapter
     * @param mode new mode
     */
    public void setMode(@NonNull MODE mode) {
        MODE oldMode = mMode;
        mMode = mode;
        resetSelectedItems();
        if (mModeChangeListener != null) {
            mModeChangeListener.modeChanged(oldMode, mMode);
        }
    }

    /**
     * Return the current mode
     * @return the current mode
     */
    @NonNull
    public MODE getMode() {
        return mMode;
    }

    /**
     * Update the click listener.
     * It will be called when a click event is detected.
     * @param listener some listener
     */
    public void setClickListener (@Nullable ClickListener listener) {
        mClickListener = listener;
    }

    /**
     * Update the selection change listener.
     * It will be called when a selection change event is detected.
     * @param listener some listener
     */
    public void setSelectionChangeListener(@Nullable OnSelectionChangeListener listener) {
        mModeChangeListener = listener;
    }

    /**
     * Set all items non selected
     */
    public void resetSelectedItems() {
        if (mCheckedItems.size() > 0) {
            mCheckedItems.clear();
            if (mModeChangeListener != null) {
                mModeChangeListener.selectionChanged(mCheckedItems.size());
            }
        }
    }

    /**
     * Set an item selected
     * @param position position of the item
     */
    public void addSelectedItem(int position) {
        mCheckedItems.add(position);
        if (mModeChangeListener != null) {
            mModeChangeListener.selectionChanged(mCheckedItems.size());
        }
    }

    /**
     * Set an item unselected
     * @param position position of the item
     */
    public void removeSelectedItem(int position) {
        Integer integer = position;
        if (mCheckedItems.remove(integer) && mModeChangeListener != null) {
            mModeChangeListener.selectionChanged(mCheckedItems.size());
        }
    }

    /**
     * Return true if an item is selected, else false
     * @param position position of the item
     * @return true if an item is selected, else false
     */
    public boolean isSelected(int position) {
        return mCheckedItems.contains(position);
    }

    /**
     * Children must initialize some content and not in {@link #getView(int, View, ViewGroup)}.
     * @param position position of the item
     * @param convertView old view if present
     * @param parent Group of the view
     * @return the initialized view for the item
     */
    protected abstract View getViewAt(int position, View convertView, ViewGroup parent);

    /**
     * {@inheritDoc}
     */
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View view = getViewAt(position, convertView, parent);
        CheckBox checkBox = (CheckBox) view.findViewById(mCheckboxId);
        if (checkBox != null) {
            switch (mMode) {
                case MODE_BASE:
                    checkBox.setChecked(false);
                    checkBox.setVisibility(View.GONE);
                    break;
                case MODE_SELECTION:
                    checkBox.setChecked(isSelected(position));
                    checkBox.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            Log.e(TAG, "Checkbox with id "+mCheckboxId+" not found");
        }
        MyClickListener listener = new MyClickListener(position);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(listener);
        return view;
    }

    /**
     * Possible modes for {@link SelectableItemsAdapter}
     */
    public enum MODE {
        /**
         * Default mode, nothing special
         */
        MODE_BASE(0),
        /**
         * Mode when the selection has begin
         */
        MODE_SELECTION(1);

        /**
         * Value of the mode
         */
        private final int mValue;

        /**
         * Builder of the class
         * @param value value of the mode
         */
        MODE(int value) {
            mValue = value;
        }

        /**
         * Transform a mode in an int value
         * @param mode some mode
         * @return the int value of the mode
         */
        public static int getValue(@NonNull MODE mode) {
            return mode.mValue;
        }

        /**
         * Get the mode for an int value
         * @return the mode for an int value
         */
        public static MODE getMode(int value) {
            MODE mode = MODE_BASE;
            if (value == MODE_SELECTION.mValue) {
                mode = MODE_SELECTION;
            }
            return mode;
        }
    }

    /**
     * Listener called when a click is detected
     */
    public interface ClickListener {
        /**
         * called when a click is detected
         * @param view view clicked
         * @param index index of the view
         */
        void onClick(View view, int index);
    }

    /**
     * Listener called when the mode change or the selection change
     */
    public interface OnSelectionChangeListener {
        /**
         * called when the mode change
         * @param oldMode old mode
         * @param newMode new mode
         */
        void modeChanged(@NonNull MODE oldMode, @NonNull MODE newMode);

        /**
         * called when the selection change
         * @param nbSelected number of selected items
         */
        void selectionChanged(int nbSelected);
    }

    /**
     * Listener which detect events like a click or long click
     */
    private class MyClickListener implements View.OnClickListener, View.OnLongClickListener {

        /**
         * Index of the associed item
         */
        private final int mIndex;

        /**
         * Builder of the class
         * @param index Index of the associed item
         */
        MyClickListener(int index) {
            mIndex = index;
        }

        /**
         * Called when a click is detected
         * @param view view in which the click is detected
         */
        @Override
        public void onClick(View view) {
            Log.i(TAG, "click on index "+mIndex);
            MODE mode = getMode();
            switch (mode) {
                case MODE_BASE:
                    if (mClickListener != null) {
                        mClickListener.onClick(view, mIndex);
                    }
                    break;
                case MODE_SELECTION:
                    if (isSelected(mIndex)) {
                        removeSelectedItem(mIndex);
                    } else {
                        addSelectedItem(mIndex);
                    }
                    notifyDataSetInvalidated();
                    break;
            }
        }

        /**
         * Called when a long click is detected
         * @param view view in which the long click is detected
         * @return true if consumed else false
         */
        @Override
        public boolean onLongClick(View view) {
            Log.i(TAG, "long click on index "+mIndex);
            boolean consumed = false;
            MODE mode = getMode();
            if (mode == MODE.MODE_BASE) {
                setMode(MODE.MODE_SELECTION);
                addSelectedItem(mIndex);
                notifyDataSetInvalidated();
                consumed = true;
            }
            return consumed;
        }
    }

}
