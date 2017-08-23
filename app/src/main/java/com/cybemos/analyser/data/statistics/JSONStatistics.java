package com.cybemos.analyser.data.statistics;

import android.support.annotation.NonNull;

import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.analyser.SMSAnalyser;
import com.cybemos.analyser.data.exceptions.NumberAccessException;

import java.util.Date;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */

public class JSONStatistics {

    public String fromPhone;
    public String toPhone;
    public Date date;
    public Date firstSMSDateSent;
    public Date firstSMSDateReceived;
    public int numberSMSSent;
    public int numberSMSReceived;
    public int numberWordsSent;
    public int numberWordsReceived;
    public int numberCharactersSent;
    public int numberCharactersReceived;

    public JSONStatistics() {}

    public JSONStatistics(@NonNull Statistics statistics) throws NumberAccessException {
        fromPhone = Util.getMyNumber(Util.context);
        toPhone = statistics.getNumber();
        date = statistics.getDate();
        firstSMSDateSent = statistics.getFirstDateSMS(Statistics.Request.SENT);
        firstSMSDateReceived = statistics.getFirstDateSMS(Statistics.Request.RECEIVED);
        numberSMSSent = statistics.getNumberOfSMS(Statistics.Request.SENT);
        numberSMSReceived = statistics.getNumberOfSMS(Statistics.Request.RECEIVED);
        numberWordsSent = statistics.getTotalWords(Statistics.Request.SENT);
        numberWordsReceived = statistics.getTotalWords(Statistics.Request.RECEIVED);
        numberCharactersSent = statistics.getTotalCharacters(Statistics.Request.SENT);
        numberCharactersReceived = statistics.getTotalCharacters(Statistics.Request.RECEIVED);
    }

    public Statistics toStatistics() {
        ConcreteStatistics statistics = new ConcreteStatistics();
        statistics.setName(toPhone);
        statistics.setNumber(toPhone);
        statistics.setDate(date);
        statistics.setFirstDateSMS(Statistics.Request.RECEIVED, firstSMSDateReceived);
        statistics.setFirstDateSMS(Statistics.Request.SENT, firstSMSDateSent);
        statistics.setNumberOfSMS(Statistics.Request.SENT, numberSMSSent);
        statistics.setNumberOfSMS(Statistics.Request.RECEIVED, numberSMSReceived);
        statistics.setAverageCharactersBySMS(Statistics.Request.SENT,
                ((double) numberCharactersSent) / numberSMSSent);
        statistics.setAverageCharactersBySMS(Statistics.Request.RECEIVED,
                ((double) numberCharactersReceived) / numberSMSReceived);
        statistics.setTotalWords(Statistics.Request.SENT, numberWordsSent);
        statistics.setTotalWords(Statistics.Request.RECEIVED, numberWordsReceived);
        statistics.setTotalCharacters(Statistics.Request.SENT, numberCharactersSent);
        statistics.setTotalCharacters(Statistics.Request.RECEIVED, numberCharactersReceived);
        Date actual = new Date();
        for (Statistics.TIME time : Statistics.TIME.values()) {
            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, time,
                    SMSAnalyser.getNumberOfSMSBy(actual.getTime() - firstSMSDateReceived.getTime(), numberSMSReceived, time));
            statistics.setNumberOfSMSBy(Statistics.Request.SENT, time,
                    SMSAnalyser.getNumberOfSMSBy(actual.getTime() - firstSMSDateSent.getTime(), numberSMSSent, time));
        }
        statistics.validate();
        return statistics;
    }

}
