package com.cybemos.analyser.data.analyser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.statistics.ConcreteStatistics;
import com.cybemos.analyser.data.statistics.Statistics;
import com.cybemos.analyser.data.Util;

import java.io.Serializable;
import java.util.Date;

/**
 * Analyse all the sms of a contact or all contacts
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class ContactAnalyser implements Serializable, IAnalyser {

    private final static long serialVersionUID = 0L;
    private final static String REQUEST = "address = ?";
    private final String name;
    private final String number;
    private final AnalyserRequest request1, request2;
    @StringRes
    private int lastErrorId;

    /**
     * All contact Analyser
     */
    public ContactAnalyser(Context context) {
        number = name = context.getString(R.string.all);
        lastErrorId = -1;
        request1 = new AnalyserRequest(Statistics.Request.RECEIVED);
        request2 = new AnalyserRequest(Statistics.Request.SENT);
    }

    /**
     * One contact Analyser
     */
    public ContactAnalyser(@Nullable String name, @NonNull String number) {
        this.number = Util.sanitizeNumber(number);
        this.name = (name == null) ? this.number : name;
        request1 = new AnalyserRequest(Statistics.Request.RECEIVED,
                REQUEST, new String[]{this.number});
        request2 = new AnalyserRequest(Statistics.Request.SENT,
                REQUEST, new String[]{this.number});
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean jobFinished() {
        return request1.isFinished() && request2.isFinished();
    }

    @Override
    public boolean hasError() {
        return (request1.isFinished() && request1.getGeneratedAnalyser() == null)
                || (request2.isFinished() && request2.getGeneratedAnalyser() == null);
    }

    @Override
    @StringRes
    public int getLastError() {
        return lastErrorId;
    }

    @WorkerThread
    public void doTheJob() {
        request1.generateAnalyser();
        request2.generateAnalyser();
        @StringRes int errorId;
        errorId = request1.getLastErrorId();
        if (errorId != -1) {
            lastErrorId = errorId;
        }
        errorId = request2.getLastErrorId();
        if (errorId != -1) {
            lastErrorId = errorId;
        }
    }

    @Override
    public synchronized void clear() {
        request1.clear();
        request2.clear();
    }

    @Nullable
    @Override
    public Statistics toStatistics() {
        ConcreteStatistics statistics = null;
        if (jobFinished() && !hasError()) {
            statistics = new ConcreteStatistics();
            SMSAnalyser analyser1 = request1.getGeneratedAnalyser();
            SMSAnalyser analyser2 = request2.getGeneratedAnalyser();

            statistics.setName(name);
            statistics.setNumber(number);
            statistics.setDate(new Date());
            statistics.setFirstDateSMS(Statistics.Request.RECEIVED, analyser1.getFirstSMSDate());
            statistics.setFirstDateSMS(Statistics.Request.SENT, analyser2.getFirstSMSDate());
            statistics.setNumberOfSMS(Statistics.Request.RECEIVED, analyser1.count());
            statistics.setNumberOfSMS(Statistics.Request.SENT, analyser2.count());
            statistics.setAverageCharactersBySMS(Statistics.Request.RECEIVED, analyser1.averageNumberSMS());
            statistics.setAverageCharactersBySMS(Statistics.Request.SENT, analyser2.averageNumberSMS());
            statistics.setTotalWords(Statistics.Request.RECEIVED, analyser1.totalWords());
            statistics.setTotalWords(Statistics.Request.SENT, analyser2.totalWords());
            statistics.setTotalCharacters(Statistics.Request.RECEIVED, analyser1.totalChars());
            statistics.setTotalCharacters(Statistics.Request.SENT, analyser2.totalChars());
            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.DAY,
                    analyser1.getNumberOfSMSBy(Statistics.TIME.DAY));
            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.DAY,
                    analyser2.getNumberOfSMSBy(Statistics.TIME.DAY));
            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.WEEK,
                    analyser1.getNumberOfSMSBy(Statistics.TIME.WEEK));
            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.WEEK,
                    analyser2.getNumberOfSMSBy(Statistics.TIME.WEEK));
            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.MONTH,
                    analyser1.getNumberOfSMSBy(Statistics.TIME.MONTH));
            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.MONTH,
                    analyser2.getNumberOfSMSBy(Statistics.TIME.MONTH));
            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.YEAR,
                    analyser1.getNumberOfSMSBy(Statistics.TIME.YEAR));
            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.YEAR,
                    analyser2.getNumberOfSMSBy(Statistics.TIME.YEAR));

            statistics.validate();
        }
        return statistics;
    }

}
