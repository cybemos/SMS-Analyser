package com.cybemos.analyser.data.analyser;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;

import com.cybemos.analyser.data.statistics.Statistics;

import java.io.Serializable;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface IAnalyser extends Serializable {

    @WorkerThread void doTheJob();
    String getName();
    void clear();
    boolean jobFinished();
    boolean hasError();
    @StringRes int getLastError();
    @Nullable Statistics toStatistics();

}
