package com.cybemos.analyser.data.analyser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cybemos.analyser.data.statistics.Statistics;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
@SuppressWarnings("unused")
public class StatsAnalyser implements IAnalyser {

    private final static long serialVersionUID = 0L;
    @NonNull
    private final Statistics statistics;

    public StatsAnalyser(@NonNull Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void doTheJob() {
        // nothing to do
    }

    @Override
    public String getName() {
        return statistics.getName();
    }

    @Override
    public void clear() {
        // nothing to do
    }

    @Override
    public boolean jobFinished() {
        return true;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public int getLastError() {
        return -1;
    }

    @Nullable
    @Override
    public Statistics toStatistics() {
        return statistics;
    }
}
