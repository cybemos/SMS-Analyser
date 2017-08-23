package com.cybemos.analyser.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.Settings;
import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.analyser.IAnalyser;
import com.cybemos.analyser.data.statistics.Statistics;
import com.cybemos.uilibrary.views.graph.Bar;
import com.cybemos.uilibrary.views.graph.BarGraph;
import com.cybemos.uilibrary.views.graph.GraphElement;
import com.cybemos.uilibrary.views.graph.BarTouchListener;
import com.cybemos.uilibrary.views.Legend;
import com.cybemos.uilibrary.views.graph.PieGraph;
import com.cybemos.uilibrary.views.graph.Slice;
import com.cybemos.uilibrary.views.graph.SliceTouchListener;

/**
 * @version 1.0
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 */

public class ResultAdapter extends BaseAdapter {

    private static final String TAG = "ResultAdapter";

    private IAnalyser analyser;
    private final Context context;
    private final boolean[] showPercentage;
    private State mState;
    private final static @IdRes int mErrorView = R.id.error_view;

    public ResultAdapter(Context c) {
        super();
        context = c;
        mState = State.OK;
        showPercentage = new boolean[getCount()];
        Settings settings = Util.getSettings();
        boolean showPercent = false;
        int format = settings.getChosenFormat();
        if (format == Settings.FORMAT_PERCENTAGE) {
            showPercent = true;
        }
        for (int i = 0 ; i < showPercentage.length ; i++) {
            showPercentage[i] = showPercent;
        }
    }

    /*public void setErrorView(@IdRes int errorView) {
        mErrorView = errorView;
    }

    public void setWaitingView(@IdRes int waitingView) {
        mWaitingView = waitingView;
    }*/

    public void updateFormatValues() {
        Settings settings = Util.getSettings();
        boolean showPercent = false;
        int format = settings.getChosenFormat();
        if (format == Settings.FORMAT_PERCENTAGE) {
            showPercent = true;
        }
        for (int i = 0 ; i < showPercentage.length ; i++) {
            showPercentage[i] = showPercent;
        }
    }

    public void set(IAnalyser contactAnalyser) {
        this.analyser = contactAnalyser;
    }

    public void setState(State state) {
        mState = state;
    }

    public boolean isWaiting() {
        return mState == State.WAITING;
    }

    private @StringRes int getTitle(int position) {
        @StringRes int title = R.string.no_subtitle_result;
        switch (position) {
            case 0:
                title = R.string.subtitle_result_1;
                break;
            case 1:
                title = R.string.subtitle_result_2;
                break;
            case 2:
                title = R.string.subtitle_result_3;
                break;
            case 3:
                title = R.string.subtitle_result_4;
                break;
            case 4:
                title = R.string.subtitle_result_5;
                break;
            case 5:
                title = R.string.subtitle_result_6;
                break;
            case 6:
                title = R.string.subtitle_result_7;
                break;
            case 7:
                title = R.string.subtitle_result_8;
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view, oldGraph;
        TextView title;
        PieGraph pieGraph = null;
        BarGraph barGraph = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        Settings settings = Util.getSettings();
        int chosenGraph = settings.getChosenGraph();
        if(convertView == null) {
            if (chosenGraph == Settings.PIE_GRAPH) {
                view = inflater.inflate(R.layout.item_graph, parent, false);
                pieGraph = (PieGraph) view.findViewById(R.id.graph);
            } else {
                view = inflater.inflate(R.layout.item_bar_graph, parent, false);
                barGraph = (BarGraph) view.findViewById(R.id.barGraph);
            }
        } else {
            view = convertView;
            if (chosenGraph == Settings.PIE_GRAPH) {
                oldGraph =  view.findViewById(R.id.graph);
                if (!(oldGraph instanceof PieGraph)) {
                    view = inflater.inflate(R.layout.item_graph, parent, false);
                    pieGraph = (PieGraph) view.findViewById(R.id.graph);
                } else {
                    pieGraph = (PieGraph) oldGraph;
                }
            } else if (chosenGraph == Settings.BAR_GRAPH) {
                oldGraph =  view.findViewById(R.id.barGraph);
                if (!(oldGraph instanceof BarGraph)) {
                    view = inflater.inflate(R.layout.item_bar_graph, parent, false);
                    barGraph = (BarGraph) view.findViewById(R.id.barGraph);
                } else {
                    barGraph = (BarGraph) oldGraph;
                }
            }
        }
        title = (TextView) view.findViewById(R.id.title);
        Legend legend = (Legend) view.findViewById(R.id.legend);

        if (analyser != null && analyser.hasError()) {
            setState(State.ERROR);
        }

        title.setText(getTitle(position));

        final Statistics statistics = (analyser != null && analyser.jobFinished())
                ? analyser.toStatistics() : null;

        if (analyser == null || !analyser.jobFinished()) {
            setState(State.WAITING);
        }

        Log.i(TAG, "state : "+mState);

        @ColorInt int color_received = settings.getColor_received();
        @ColorInt int color_sent = settings.getColor_sent();

        legend.resetContent();
        legend.addElement(context.getString(R.string.received), color_received);
        legend.addElement(context.getString(R.string.sent), color_sent);

        Slice slice1, slice2;
        Bar bar1, bar2;

        GraphElement e1, e2;
        e1 = e2 = null;

        if (pieGraph != null) {
            slice1 = new Slice();
            slice1.setTextColor(Color.WHITE);
            slice1.setColor(color_received);
            slice1.setText(null);
            slice2 = new Slice();
            slice2.setTextColor(Color.WHITE);
            slice2.setColor(color_sent);
            slice2.setText(null);

            slice1.setValue(0);
            slice2.setValue(0);

            pieGraph.setSliceTouchListener(new MyListener(position));
            pieGraph.removeAllSlices();
            e1 = slice1;
            e2 = slice2;
        } else if (barGraph != null) {
            barGraph.setBarTouchListener(new MyListener(position));
            barGraph.removeAllBars();
            bar1 = new Bar();
            bar1.setValue(0);
            bar1.setColor(color_received);
            bar1.setTextColor(Color.WHITE);
            bar2 = new Bar();
            bar2.setValue(0);
            bar2.setTextColor(Color.WHITE);
            bar2.setColor(color_sent);
            barGraph.setLabels(context.getString(R.string.received),
                    context.getString(R.string.sent));
            e1 = bar1;
            e2 = bar2;
        }

        //data1.setBackgroundColor(color_received);
        //data2.setBackgroundColor(color_sent);

        TextView errorView = (TextView) view.findViewById(mErrorView);

        if (mState == State.WAITING) {
            legend.setVisibility(View.INVISIBLE);
            if (pieGraph != null) {
                pieGraph.setVisibility(View.INVISIBLE);
            }
            if (barGraph != null) {
                barGraph.setVisibility(View.INVISIBLE);
            }
            errorView.setText(R.string.waiting_message);
            errorView.setVisibility(View.VISIBLE);
        } else if (mState == State.ERROR) {
            legend.setVisibility(View.INVISIBLE);
            if (pieGraph != null) {
                pieGraph.setVisibility(View.INVISIBLE);
            }
            if (barGraph != null) {
                barGraph.setVisibility(View.INVISIBLE);
            }
            errorView.setText(R.string.error_message);
            errorView.setVisibility(View.VISIBLE);
        } else {
            errorView.setVisibility(View.INVISIBLE);
            if (pieGraph != null) {
                pieGraph.setVisibility(View.VISIBLE);
            }
            if (barGraph != null) {
                barGraph.setVisibility(View.VISIBLE);
            }
            legend.setVisibility(View.VISIBLE);
            assert statistics != null;
            assert e1 != null;
            int number1, number2;
            double total, d1, d2;
            if (mState == State.OK) {
                if (pieGraph != null) {
                    pieGraph.addSlice((Slice) e2);
                    pieGraph.addSlice((Slice) e1);
                } else {
                    barGraph.addBar((Bar) e1);
                    barGraph.addBar((Bar) e2);
                }
            }
            @StringRes int res;
            switch (position) {
                case 0:
                    number1 = statistics.getNumberOfSMS(Statistics.Request.RECEIVED);
                    number2 = statistics.getNumberOfSMS(Statistics.Request.SENT);
                    //data1.setText(context.getString(R.string.format_integer, number1));
                    //data2.setText(context.getString(R.string.format_integer, number2));
                    if (showPercentage[position]) {
                        total  = number1 + number2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, number1 * 100 / total));
                        e2.setText(context.getString(res, number2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_integer, number1));
                        e2.setText(context.getString(R.string.format_integer, number2));
                    }
                    e1.setValue(number1);
                    e2.setValue(number2);
                    break;
                case 1:
                    d1 = statistics.getAverageCharactersBySMS(Statistics.Request.RECEIVED);
                    d2 = statistics.getAverageCharactersBySMS(Statistics.Request.SENT);
                    //data1.setText(context.getString(R.string.format_double, d1));
                    //data2.setText(context.getString(R.string.format_double, d2));
                    if (showPercentage[position]) {
                        total  = d1 + d2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, d1 * 100 / total));
                        e2.setText(context.getString(res, d2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_double, d1));
                        e2.setText(context.getString(R.string.format_double, d2));
                    }
                    e1.setValue((float) d1);
                    e2.setValue((float) d2);
                    break;
                case 2:
                    number1 = statistics.getTotalWords(Statistics.Request.RECEIVED);
                    number2 = statistics.getTotalWords(Statistics.Request.SENT);
                    //data1.setText(context.getString(R.string.format_integer, number1));
                    //data2.setText(context.getString(R.string.format_integer, number2));
                    if (showPercentage[position]) {
                        total  = number1 + number2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, number1 * 100 / total));
                        e2.setText(context.getString(res, number2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_integer, number1));
                        e2.setText(context.getString(R.string.format_integer, number2));
                    }
                    e1.setValue(number1);
                    e2.setValue(number2);
                    break;
                case 3:
                    number1 = statistics.getTotalCharacters(Statistics.Request.RECEIVED);
                    number2 = statistics.getTotalCharacters(Statistics.Request.SENT);
                    //data1.setText(context.getString(R.string.format_integer, number1));
                    //data2.setText(context.getString(R.string.format_integer, number2));
                    if (showPercentage[position]) {
                        total  = number1 + number2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, number1 * 100 / total));
                        e2.setText(context.getString(res, number2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_integer, number1));
                        e2.setText(context.getString(R.string.format_integer, number2));
                    }
                    e1.setValue(number1);
                    e2.setValue(number2);
                    break;
                case 4:
                    d1 = statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.DAY);
                    d2 = statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.DAY);
                    //data1.setText(context.getString(R.string.format_double, d1));
                    //data2.setText(context.getString(R.string.format_double, d2));
                    if (showPercentage[position]) {
                        total  = d1 + d2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, d1 * 100 / total));
                        e2.setText(context.getString(res, d2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_double, d1));
                        e2.setText(context.getString(R.string.format_double, d2));
                    }
                    e1.setValue((float) d1);
                    e2.setValue((float) d2);
                    break;
                case 5:
                    d1 = statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.WEEK);
                    d2 = statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.WEEK);
                    //data1.setText(context.getString(R.string.format_double, d1));
                    //data2.setText(context.getString(R.string.format_double, d2));
                    if (showPercentage[position]) {
                        total  = d1 + d2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, d1 * 100 / total));
                        e2.setText(context.getString(res, d2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_double, d1));
                        e2.setText(context.getString(R.string.format_double, d2));
                    }
                    e1.setValue((float) d1);
                    e2.setValue((float) d2);
                    break;
                case 6:
                    d1 = statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.MONTH);
                    d2 = statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.MONTH);
                    //data1.setText(context.getString(R.string.format_double, d1));
                    //data2.setText(context.getString(R.string.format_double, d2));
                    if (showPercentage[position]) {
                        total  = d1 + d2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, d1 * 100 / total));
                        e2.setText(context.getString(res, d2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_double, d1));
                        e2.setText(context.getString(R.string.format_double, d2));
                    }
                    e1.setValue((float) d1);
                    e2.setValue((float) d2);
                    break;
                case 7:
                    d1 = statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.YEAR);
                    d2 = statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.YEAR);
                    //data1.setText(context.getString(R.string.format_double, d1));
                    //data2.setText(context.getString(R.string.format_double, d2));
                    if (showPercentage[position]) {
                        total  = d1 + d2;
                        res = getPercentFormat(settings);
                        e1.setText(context.getString(res, d1 * 100 / total));
                        e2.setText(context.getString(res, d2 * 100 / total));
                    } else {
                        e1.setText(context.getString(R.string.format_double, d1));
                        e2.setText(context.getString(R.string.format_double, d2));
                    }
                    e1.setValue((float) d1);
                    e2.setValue((float) d2);
                    break;
            }
        }
        if (pieGraph != null) {
            pieGraph.postInvalidate();
        } else if (barGraph != null) {
            barGraph.postInvalidate();
        }
        return view;
    }

    @StringRes
    private int getPercentFormat(Settings settings) {
        @StringRes int res = R.string.format_double_percent_0;
        int number = settings.getNumberOfFractionDigits();
        switch (number) {
            case 1:
                res = R.string.format_double_percent_1;
                break;
            case 2:
                res = R.string.format_double_percent_2;
                break;
            case 3:
                res = R.string.format_double_percent_3;
                break;
        }
        return res;
    }

    public enum State {
        OK,
        WAITING,
        ERROR
    }

    private class MyListener implements SliceTouchListener, BarTouchListener {

        private final int mIndex;

        MyListener(int index) {
            mIndex = index;
        }

        @Override
        public void onSliceClicked(PieGraph owner, Slice slice) {
            toggle();
        }

        @Override
        public void onGraphClicked(BarGraph owner, Bar bar) {
            toggle();
        }

        private void toggle() {
            if (mIndex >= 0 && mIndex < showPercentage.length) {
                boolean old = showPercentage[mIndex];
                showPercentage[mIndex] = !old;
                notifyDataSetChanged();
            }
        }
    }

}
