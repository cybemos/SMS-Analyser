package com.cybemos.analyser.data.statistics;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Statistics between the user and a contact
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface Statistics extends Serializable {

    /**
     * @return the nice name of the contact
     */
    @NonNull String getName();

    /**
     * @return the number of the contact
     */
    @NonNull String getNumber();

    /**
     * @return the date of the last update of those statistics
     */
    @NonNull Date getDate();

    /**
     * @return the date of the first SMS sent and/or received
     */
    @NonNull Date getFirstDateSMS(@NonNull Request request);

    /**
     * @return the number of SMS sent and/or received
     */
    int getNumberOfSMS(@NonNull Request request);
    /**
     * @return the average number of character in all SMS sent and/or received
     */
    double getAverageCharactersBySMS(@NonNull Request request);
    /**
     * @return the number of words in all SMS sent and/or received
     */
    int getTotalWords(@NonNull Request request);
    /**
     * @return the number of characters in all SMS sent and/or received
     */
    int getTotalCharacters(@NonNull Request request);
    /**
     * @return the number of SMS sent and/or received by day, week, month or year
     */
    double getNumberOfSMSBy(@NonNull Request request, TIME time);

    enum Request {
        SENT,
        RECEIVED,
        ALL
    }

    enum TIME {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

}
