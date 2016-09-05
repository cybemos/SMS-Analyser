package com.cybemos.uilibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public abstract class SwipeAdapter extends BaseAdapter {

    protected Context mContext;

    public SwipeAdapter(Context context) {
        mContext = context;
    }

    protected abstract View getViewAt(int position, View convertView, ViewGroup parent);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.swipe_item, parent, false);
        } else {
            view = convertView;
        }
        final View childView = getViewAt(position, null, parent);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        SwipeFragmentAdapter.SwipeElement element = new SwipeFragmentAdapter.SwipeElement() {

            @Override
            public boolean hasLeftView() {
                return false;
            }

            @Override
            public boolean hasRightView() {
                return false;
            }

            @NonNull
            @Override
            public View getPrincipalView(/*LayoutInflater inflater, */ViewGroup container) {
                return childView;
            }

            @Nullable
            @Override
            public View getLeftView(/*LayoutInflater inflater, */ViewGroup container) {
                return null;
            }

            @Nullable
            @Override
            public View getRightView(/*LayoutInflater inflater, */ViewGroup container) {
                return null;
            }
        };
        //viewPager.setAdapter(PagerAdapter);
        viewPager.setAdapter(new SwipeFragmentAdapter(/*((AppCompatActivity) mContext).getSupportFragmentManager(),*/ element));
        return view;
        //return getViewAt(position, convertView, parent);
    }

}
