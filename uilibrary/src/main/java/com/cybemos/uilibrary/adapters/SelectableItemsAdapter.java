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
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
@SuppressWarnings("HardCodedStringLiteral")
public abstract class SelectableItemsAdapter extends BaseAdapter {

    private static final String TAG = "SelectableItemsAdapter";
    @IdRes
    private final int mCheckboxId;
    @NonNull
    private MODE mMode;
    private ClickListener mClickListener;
    private OnSelectionChangeListener mModeChangeListener;
    protected final List<Integer> mCheckedItems;

    public SelectableItemsAdapter(@IdRes int checkboxId) {
        mCheckboxId = checkboxId;
        mMode = MODE.MODE_BASE;
        mClickListener = null;
        mModeChangeListener = null;
        mCheckedItems = new ArrayList<>();
    }

    public void setMode(@NonNull MODE mode) {
        MODE oldMode = mMode;
        mMode = mode;
        resetSelectedItems();
        if (mModeChangeListener != null) {
            mModeChangeListener.modeChanged(oldMode, mMode);
        }
    }

    @NonNull
    public MODE getMode() {
        return mMode;
    }

    public void setClickListener (@Nullable ClickListener listener) {
        mClickListener = listener;
    }

    public void setSelectionChangeListener(@Nullable OnSelectionChangeListener listener) {
        mModeChangeListener = listener;
    }

    public void resetSelectedItems() {
        if (mCheckedItems.size() > 0) {
            mCheckedItems.clear();
            if (mModeChangeListener != null) {
                mModeChangeListener.selectionChanged(mCheckedItems.size());
            }
        }
    }

    public void addSelectedItem(int position) {
        mCheckedItems.add(position);
        if (mModeChangeListener != null) {
            mModeChangeListener.selectionChanged(mCheckedItems.size());
        }
    }

    public void removeSelectedItem(int position) {
        Integer integer = position;
        if (mCheckedItems.remove(integer) && mModeChangeListener != null) {
            mModeChangeListener.selectionChanged(mCheckedItems.size());
        }
    }

    public boolean isSelected(int position) {
        return mCheckedItems.contains(position);
    }

    protected abstract View getViewAt(int position, View convertView, ViewGroup parent);

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

    public enum MODE {
        MODE_BASE(0),
        MODE_SELECTION(1);

        private final int mValue;

        MODE(int value) {
            mValue = value;
        }

        public static int getValue(@NonNull MODE mode) {
            return mode.mValue;
        }

        public static MODE getMode(int value) {
            MODE mode = MODE_BASE;
            if (value == MODE_SELECTION.mValue) {
                mode = MODE_SELECTION;
            }
            return mode;
        }
    }

    public interface ClickListener {
        void onClick(View view, int index);
    }

    public interface OnSelectionChangeListener {
        void modeChanged(@NonNull MODE oldMode, @NonNull MODE newMode);
        void selectionChanged(int nbSelected);
    }

    private class MyClickListener implements View.OnClickListener, View.OnLongClickListener {

        private final int mIndex;

        MyClickListener(int index) {
            mIndex = index;
        }

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
