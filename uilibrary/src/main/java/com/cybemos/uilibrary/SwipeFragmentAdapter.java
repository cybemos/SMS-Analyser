package com.cybemos.uilibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Hashtable;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SwipeFragmentAdapter extends PagerAdapter {

    //private static Hashtable<Integer, SwipeElement> elements = new Hashtable<>();
    //private static int maxId = 0;
    private SwipeElement mElement;
    //private int id;

    public static final int LAYOUT_LEFT = 1;
    public static final int LAYOUT_PRINCIPAL = 2;
    public static final int LAYOUT_RIGHT = 3;

    public SwipeFragmentAdapter(/*FragmentManager fm, */@NonNull SwipeElement element) {
        //super(fm);
        mElement = element;
        //id = maxId++;
        //elements.put(id, element);
    }

    /*public View getView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.swipe_item, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(this);
        return view;
    }*/

    /*@Override
    public Fragment getItem(int position) {
        SwipeFragment fragment;
        fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putInt(SwipeFragment.EXTRA_ID, id);
        switch (position) {
            case 0:
                if (mElement.hasLeftView()) {
                    args.putInt(SwipeFragment.EXTRA_LAYOUT, SwipeFragment.LAYOUT_LEFT);
                } else {
                    args.putInt(SwipeFragment.EXTRA_LAYOUT, SwipeFragment.LAYOUT_PRINCIPAL);
                }
                break;
            case 1:
                if (mElement.hasLeftView()) {
                    args.putInt(SwipeFragment.EXTRA_LAYOUT, SwipeFragment.LAYOUT_PRINCIPAL);
                } else {
                    args.putInt(SwipeFragment.EXTRA_LAYOUT, SwipeFragment.LAYOUT_RIGHT);
                }
                break;
            case 2:
                args.putInt(SwipeFragment.EXTRA_LAYOUT, SwipeFragment.LAYOUT_RIGHT);
                break;
            default:
                fragment = null;
                break;
        }
        if (fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }*/

    @Override
    public int getCount() {
        int count = 1;
        if (mElement.hasLeftView()) {
            count++;
        }
        if (mElement.hasRightView()) {
            count++;
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return true;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        int layout = -1;
        switch (position) {
            case 0:
                if (mElement.hasLeftView()) {
                    layout = LAYOUT_LEFT;
                } else {
                    layout =  LAYOUT_PRINCIPAL;
                }
                break;
            case 1:
                if (mElement.hasLeftView()) {
                    layout =  LAYOUT_PRINCIPAL;
                } else {
                    layout = LAYOUT_RIGHT;
                }
                break;
            case 2:
                layout = LAYOUT_RIGHT;
                break;
            default:
                break;
        }
        switch (layout) {
            case LAYOUT_LEFT:
                view = mElement.getLeftView(/*inflater, */container);
                break;
            case LAYOUT_PRINCIPAL:
                view = mElement.getPrincipalView(/*inflater, */container);
                break;
            case LAYOUT_RIGHT:
                view = mElement.getRightView(/*inflater, */container);
                break;
        }
        return view;
        //return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        view.destroyDrawingCache();
    }

    public interface SwipeElement {
        boolean hasLeftView();
        boolean hasRightView();
        @NonNull View getPrincipalView(/*LayoutInflater inflater, */ViewGroup container);
        @Nullable View getLeftView(/*LayoutInflater inflater, */ViewGroup container);
        @Nullable View getRightView(/*LayoutInflater inflater, */ViewGroup container);
    }

    /*public static class SwipeFragment extends Fragment {

        public static final String EXTRA_LAYOUT = "SwipeFragment.EXTRA_LAYOUT";
        public static final String EXTRA_ID = "SwipeFragment.EXTRA_ID";

        public static final int LAYOUT_LEFT = 1;
        public static final int LAYOUT_PRINCIPAL = 2;
        public static final int LAYOUT_RIGHT = 3;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.

            //View rootView = inflater.inflate(R.layout.swipe_item, container, false);
            Bundle args = getArguments();
            int id = args.getInt(EXTRA_ID, -1);
            int layout = args.getInt(EXTRA_LAYOUT, -1);
            SwipeElement element = elements.get(id);
            View view = null;
            switch (layout) {
                case LAYOUT_LEFT:
                    view = element.getLeftView(inflater, container);
                    break;
                case LAYOUT_PRINCIPAL:
                    view = element.getPrincipalView(inflater, container);
                    break;
                case LAYOUT_RIGHT:
                    view = element.getRightView(inflater, container);
                    break;
            }
            return view;
        }


    }*/

}
