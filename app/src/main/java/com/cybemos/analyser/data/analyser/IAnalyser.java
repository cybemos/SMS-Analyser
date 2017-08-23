package com.cybemos.analyser.data.analyser;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;

import com.cybemos.analyser.data.statistics.Statistics;

import java.io.Serializable;

/**
 * Interface to analyse data on the phone or in a file
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface IAnalyser extends Serializable {

    /**
     * Method that can take a lot a time
     */
    @WorkerThread void doTheJob();

    /**
     * Name of the analyser
     */
    String getName();

    /**
     * clear the analyser
     */
    void clear();

    /**
     * @return true if the job is finished else false
     */
    boolean jobFinished();

    /**
     * @return true if there is an error else false
     */
    boolean hasError();

    /**
     * @return a string res which represents the last error
     */
    @StringRes int getLastError();

    /**
     * When the job is done, generate statistics
     */
    @Nullable Statistics toStatistics();

}
