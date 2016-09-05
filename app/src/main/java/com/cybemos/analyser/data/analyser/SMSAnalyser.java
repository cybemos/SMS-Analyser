package com.cybemos.analyser.data.analyser;

import android.util.Pair;

import com.cybemos.analyser.data.SMS;
import com.cybemos.analyser.data.statistics.Statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SMSAnalyser implements Serializable {

    private final static long serialVersionUID = 0L;
    private final ArrayList<SMS> smsList;
    private final HashMap<String, Integer> words;
    private int totalChars;
    private long firstSMSDate;

    public SMSAnalyser() {
        smsList = new ArrayList<>();
        words = new HashMap<>();
        totalChars = 0;
        firstSMSDate = -1;
    }

    public synchronized void add(SMS sms) {
        smsList.add(sms);
        long date = sms.getDate();
        if (date < firstSMSDate || firstSMSDate == -1) {
            firstSMSDate = date;
        }
        String smsContent = sms.getBody().toLowerCase();
        @SuppressWarnings("All")
        String[] words = smsContent.split("\\W");
        for (String word : words) {
            if (word != null && word.length() > 0) {
                totalChars += word.length();
                if (this.words.containsKey(word)) {
                    this.words.put(word, this.words.get(word)+1);
                } else {
                    this.words.put(word, 1);
                }
            }
        }
    }

    public synchronized Date getFirstSMSDate() {
        return new Date(firstSMSDate);
    }

    public synchronized double averageNumberSMS() {
        return ((double) totalChars()) / count();
    }

    public synchronized int totalWords() {
        int total = 0;
        Set<String> keys = words.keySet();
        int number;
        for (String key : keys) {
            number = words.get(key);
            total += number;
        }
        return total;
    }

    public synchronized int totalChars() {
        return totalChars;
    }

    public synchronized Iterator<Pair<String, Integer>> getMostUsedWords(int minOccurrence) {
        if (minOccurrence < 0) {
            throw new IllegalArgumentException("minOccurrence mist be >= 0");
        }
        ArrayList<Pair<String, Integer>> list = new ArrayList<>();
        Set<String> keys = words.keySet();
        int number;
        for (String key : keys) {
            number = words.get(key);
            if (number > minOccurrence) {
                list.add(new Pair<>(key, number));
            }
        }
        return list.iterator();
    }

    public synchronized int count() {
        return smsList.size();
    }

    public synchronized double getNumberOfSMSBy(Statistics.TIME time) {
        double total = count();
        double result = -1;
        Date first = getFirstSMSDate();
        Date actual = new Date();
        long diffInMs = actual.getTime() - first.getTime();
        switch (time) {
            case DAY:
                result = total / TimeUnit.MILLISECONDS.toDays(diffInMs);
                break;
            case WEEK:
                result = total * 7.0 / TimeUnit.MILLISECONDS.toDays(diffInMs);
                break;
            case MONTH:
                result = total * 30.5 / TimeUnit.MILLISECONDS.toDays(diffInMs);
                break;
            case YEAR:
                result = total * 365.0 / TimeUnit.MILLISECONDS.toDays(diffInMs);
                break;
        }
        return result;
    }

}
